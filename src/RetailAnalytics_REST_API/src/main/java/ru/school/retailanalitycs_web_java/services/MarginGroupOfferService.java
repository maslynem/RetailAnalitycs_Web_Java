package ru.school.retailanalitycs_web_java.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.MarginGrowthOfferRequest;
import ru.school.retailanalitycs_web_java.entities.functionResults.MarginGrowthOffer;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupView;
import ru.school.retailanalitycs_web_java.services.entityServices.StoreService;
import ru.school.retailanalitycs_web_java.services.entityServices.TransactionService;
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
    private final TransactionService transactionService;

    @Transactional(readOnly = true)
    public List<MarginGrowthOffer> getMarginGroup(MarginGrowthOfferRequest request) {
        List<MarginGrowthOffer> marginGrowthOffers = new ArrayList<>();

        // Выбрали группы, где Индекс оттока по группе не более заданного пользователем значения и Индекс стабильности потребления группы составляет менее заданного пользователем значения.
        List<GroupView> groupSelection = getSelectionGroup(request.getMaxChurnIndex(), request.getMaxStabilityIndex());

        // Выбрали группы с максимальным индексом востребованности, где кол-во групп равно заданному пользователем значению
        Map<Customer, List<GroupView>> customersWithGroup = getCustomersWithLimitGroup(groupSelection, request.getGroupsNumber());

        for (Entry<Customer, List<GroupView>> entry : customersWithGroup.entrySet()) {
            Customer customer = entry.getKey();

            // Для каждого покупателя находим основной магазин
            List<Store> customerPrimaryStores = getCustomerPrimaryStores(customer);

            // Составляем мапу где ключ группа SKU а значение - список магазинов
            Map<SkuGroup, List<Store>> skuGroupStoresMap = getSkuGroupStoresMap(customerPrimaryStores);

            List<GroupView> groups = entry.getValue();
            // Составляем мапу с maxMarginSku и группой к которой относится данная SKU
            Map<Sku, GroupView> skusWithMaxMargin = getSkusWithMaxMargin(groups, skuGroupStoresMap, request.getGroupsNumber());

            for (Entry<Sku, GroupView> skuGroupViewEntry : skusWithMaxMargin.entrySet()) {
                Sku sku = skuGroupViewEntry.getKey();
                // Находим магазин в котором продается эта SKU
                List<Store> stores = skuGroupStoresMap.get(sku.getSkuGroup());
                Store store = stores.stream().filter(s -> s.getId().getSku().equals(sku)).findAny().orElseThrow();

                // Находим минимальную скидку в группе
                double groupMinDiscount = getMinimumDiscountRoundedUpInIncrementsOfFive(skuGroupViewEntry.getValue());

                // Рассчитываем скидку
                double offerDiscountDepth =
                        (request.getAllowableMarginShare() *
                                (store.getSkuRetailPrice() - store.getSkuPurchasePrice())) / store.getSkuRetailPrice();

                if (offerDiscountDepth != 0 && Double.compare(offerDiscountDepth, groupMinDiscount) >= 0) {
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

    private double getMinimumDiscountRoundedUpInIncrementsOfFive(GroupView groupView) {
        Double groupMinimumDiscount = groupView.getGroupMinimumDiscount();
        double groupMinDiscount = groupMinimumDiscount == null ? 0 : groupMinimumDiscount;
        return Math.ceil(groupMinDiscount * 20) * 5;
    }

    private Map<Sku, GroupView> getSkusWithMaxMargin(List<GroupView> groups, Map<SkuGroup, List<Store>> skuGroupStoresMap, double maxSkuShare) {
        Map<Sku, GroupView> skusWithMaxMargin = new HashMap<>();

        // Для каждой группы
        for (GroupView group : groups) {
            SkuGroup skuGroup = group.getId().getSkuGroup();

            // Проверяем продается ли данная группа в основном магазине пользователя
            if (skuGroupStoresMap.containsKey(skuGroup)) {

                // Если продается, то находим SKU с максимальной маржой
                Sku skuWithMaxMargin = getSkuWithMaxMargin(skuGroupStoresMap.get(skuGroup));

                // Определение доли SKU в группе
                double skuShareInGroup = getSkuShareInGroup(skuWithMaxMargin, skuGroup);


                // Если получившееся значение не превышает заданного пользователем значения то SKU используется для формирования предложения
                if (skuShareInGroup <= maxSkuShare) {
                    skusWithMaxMargin.put(skuWithMaxMargin, group);
                }
            }
        }
        return skusWithMaxMargin;
    }

    private Map<SkuGroup, List<Store>> getSkuGroupStoresMap(List<Store> customerPrimaryStores) {
        Map<SkuGroup, List<Store>> skuGroupStoresMap = new HashMap<>();
        for (Store store : customerPrimaryStores) {
            SkuGroup skuGroup = store.getId().getSku().getSkuGroup();
            List<Store> stores = skuGroupStoresMap.computeIfAbsent(skuGroup, k -> new ArrayList<>());
            stores.add(store);
        }
        return skuGroupStoresMap;
    }

    private List<Store> getCustomerPrimaryStores(Customer customer) {
        CustomerView customerView = customerViewService.findById(customer.getId()).orElseThrow();
        return storeService.getStoresWithTransactionalStoreId(customerView.getCustomerPrimaryStore());
    }

    private List<GroupView> getSelectionGroup(double maxChurnIndex, double maxStabilityIndex) {
        return groupViewService.findAllSortByCustomerId().stream()
                .filter(groupView -> groupView.getGroupChurnRate() < maxChurnIndex && groupView.getGroupStabilityIndex() < maxStabilityIndex)
                .sorted(Comparator.comparingDouble(GroupView::getGroupAffinityIndex)).toList();
    }

    private Map<Customer, List<GroupView>> getCustomersWithLimitGroup(List<GroupView> groupSelection, Long groupsNumber) {
        Map<Customer, List<GroupView>> customersWithGroup = new HashMap<>();
        for (GroupView groupView : groupSelection) {
            Customer customer = groupView.getId().getCustomer();
            List<GroupView> customerGroup = customersWithGroup.computeIfAbsent(customer, k -> new ArrayList<>());
            if (customerGroup.size() != groupsNumber) {
                customerGroup.add(groupView);
            }
        }
        return customersWithGroup;
    }


    private Sku getSkuWithMaxMargin(List<Store> stores) {
        Sku skuWithMaxMargin = null;
        double maxMargin = Double.MIN_VALUE;
        for (Store store : stores) {
            double margin = store.getSkuRetailPrice() - store.getSkuPurchasePrice();
            if (margin > maxMargin) {
                maxMargin = margin;
                skuWithMaxMargin = store.getId().getSku();
            }
        }
        return skuWithMaxMargin;
    }

    private long getSkuShareInGroup(Sku sku, SkuGroup skuGroup) {
        long countByChecksSku = transactionService.countBySku(sku);
        long countByChecksSkuSkuGroup = transactionService.countBySkuGroup(skuGroup);
        return countByChecksSku / countByChecksSkuSkuGroup;
    }

}
