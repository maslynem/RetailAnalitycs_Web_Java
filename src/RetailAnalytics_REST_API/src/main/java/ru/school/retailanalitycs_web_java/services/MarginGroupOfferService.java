package ru.school.retailanalitycs_web_java.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.MarginGrowthOfferRequest;
import ru.school.retailanalitycs_web_java.entities.functionResults.MarginGrowthOffer;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupView;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.TransactionRepository;
import ru.school.retailanalitycs_web_java.services.entityServices.StoreService;
import ru.school.retailanalitycs_web_java.services.viewServices.CustomerViewService;
import ru.school.retailanalitycs_web_java.services.viewServices.GroupViewService;

import java.util.*;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class MarginGroupOfferService {
    private final GroupViewService groupViewService;
    private final StoreService storeService;
    private final CustomerViewService customerViewService;
    private final TransactionRepository transactionRepository;

    public List<MarginGrowthOffer> getMarginGroup(MarginGrowthOfferRequest request) {
        long groupsNumber = request.getGroupsNumber();
        double maxChurnIndex = request.getMaxChurnIndex();
        double maxStabilityIndex = request.getMaxStabilityIndex();
        double maxSkuShare = request.getMaxSkuShare();
        double allowableMarginShare = request.getAllowableMarginShare();
        List<MarginGrowthOffer> marginGrowthOffers = new ArrayList<>();


        // Выбрали группы, где Индекс оттока по группе не более заданного пользователем значения и Индекс стабильности потребления группы составляет менее заданного пользователем значения.
        List<GroupView> groupSelection = groupViewService.findAllSortByCustomerId().parallelStream()
                .filter(groupView -> groupView.getGroupChurnRate() < maxChurnIndex && groupView.getGroupStabilityIndex() < maxStabilityIndex)
                .sorted(Comparator.comparingDouble(GroupView::getGroupAffinityIndex)).toList();


        Map<Customer, List<GroupView>> customersWithGroup = new HashMap<>();
        // Выбрали группы с максимальным индексом востребованности, где кол-во групп равно заданному пользователем значению
        for (GroupView groupView : groupSelection) {
            Customer customer = groupView.getId().getCustomer();
            List<GroupView> customerGroup = customersWithGroup.computeIfAbsent(customer, k -> new ArrayList<>());
            if (customerGroup.size() != groupsNumber) {
                customerGroup.add(groupView);
            }
        }

        for (Entry<Customer, List<GroupView>> entry : customersWithGroup.entrySet()) {
            Customer customer = entry.getKey();
            CustomerView customerView = customerViewService.findById(customer.getId()).orElseThrow();

            // Для каждого покупателя находим основной магазин
            List<Store> customerPrimaryStores = storeService.getStoresWithTransactionalStoreId(customerView.getCustomerPrimaryStore());

            // Составляем мапу где ключ группа SKU а значение - список магазинов
            Map<SkuGroup, List<Store>> skuStoresMap = new HashMap<>();
            for (Store store : customerPrimaryStores) {
                SkuGroup skuGroup = store.getSku().getSkuGroup();
                List<Store> stores = skuStoresMap.computeIfAbsent(skuGroup, k -> new ArrayList<>());
                stores.add(store);
            }

            List<GroupView> groups = entry.getValue();
            Set<Sku> skusWithMaxMargin = new HashSet<>();
            // Для каждой группы
            for (GroupView group : groups) {
                SkuGroup skuGroup = group.getId().getSkuGroup();
                // Проверяем продается ли данная группа в основном магазине пользователя
                if (skuStoresMap.containsKey(skuGroup)) {
                    // Если продается, то находим SKU с максимальной маржой
                    Sku skuWithMaxMargin = getSkuWithMaxMargin(skuStoresMap.get(skuGroup));
                    // Определение доли SKU в группе
                    double skuShareInGroup = getSkuShareInGroup(skuWithMaxMargin, skuGroup);
                    // Если получившееся значение не превышает заданного пользователем значения то SKU используется для формирования предложения
                    if (skuShareInGroup <= maxSkuShare) {
                        skusWithMaxMargin.add(skuWithMaxMargin);
                    }
                }
            }
            // Для всех отобранных SKU
            for (Sku sku : skusWithMaxMargin) {
                Store store = null;
                // Находим магазин в котором продается эта SKU
                List<Store> stores = skuStoresMap.get(sku.getSkuGroup());
                for (Store storeWithSkuGroup : stores) {
                    if (storeWithSkuGroup.getSku().equals(sku)) {
                        store = storeWithSkuGroup;
                        break;
                    }
                }

                double groupMinDiscount = Double.MIN_VALUE;
                // Находим минимальную скидку в группе
                for (GroupView groupView : groupSelection) {
                    if (groupView.getId().getSkuGroup().equals(sku.getSkuGroup())) {
                        if (groupView.getGroupMinimumDiscount() == null) {
                            continue;
                        }
                        groupMinDiscount = groupView.getGroupMinimumDiscount();
                        break;
                    }
                }
                groupMinDiscount = Math.ceil(groupMinDiscount * 20) * 5;
                // Рассчитываем скидку
                double offerDiscountDepth = (allowableMarginShare * (store.getSkuRetailPrice() - store.getSkuPurchasePrice())) / store.getSkuRetailPrice();
                if (Double.compare(offerDiscountDepth, groupMinDiscount) >= 0) {
                    marginGrowthOffers.add(MarginGrowthOffer.builder()
                            .customerId(customer.getId())
                            .skuName(sku.getSkuName())
                            .offerDiscountDepth(groupMinDiscount)
                            .build());
                }

            }
        }
        return marginGrowthOffers;
    }

    private Sku getSkuWithMaxMargin(List<Store> stores) {
        Sku skuWithMaxMargin = null;
        double maxMargin = Double.MIN_VALUE;
        for (Store store : stores) {
            double margin = store.getSkuRetailPrice() - store.getSkuPurchasePrice();
            if (margin > maxMargin) {
                maxMargin = margin;
                skuWithMaxMargin = store.getSku();
            }
        }
        return skuWithMaxMargin;
    }

    private long getSkuShareInGroup(Sku sku, SkuGroup skuGroup) {
        long countByChecksSku = transactionRepository.countByChecks_Sku(sku);
        long countByChecksSkuSkuGroup = transactionRepository.countByChecks_Sku_SkuGroup(skuGroup);
        return countByChecksSku / countByChecksSkuSkuGroup;
    }


}
