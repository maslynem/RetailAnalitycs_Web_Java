package ru.school.retailanalitycs_web_java.controllers.viewControllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.viewDto.PurchaseHistoryViewDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryViewId;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.PurchaseHistoryViewNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.PurchaseHistoryViewMapper;
import ru.school.retailanalitycs_web_java.services.viewServices.PurchaseHistoryViewService;

import java.util.List;

@RestController
@RequestMapping("api/v1/views/purchase-histories")
public class PurchaseHistoryViewController {
    private final PurchaseHistoryViewService purchaseHistoryViewService;
    private final PurchaseHistoryViewMapper purchaseHistoryViewMapper;

    public PurchaseHistoryViewController(PurchaseHistoryViewService purchaseHistoryViewService, PurchaseHistoryViewMapper purchaseHistoryViewMapper) {
        this.purchaseHistoryViewService = purchaseHistoryViewService;
        this.purchaseHistoryViewMapper = purchaseHistoryViewMapper;
    }

    @GetMapping
    public List<PurchaseHistoryViewDto> findAllPurchaseHistorys() {
        return purchaseHistoryViewService.findAll().stream().map(purchaseHistoryViewMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<PurchaseHistoryViewDto> findAllPurchaseHistorysByPage(@RequestParam("page") int page,
                                                                      @RequestParam("size") int size) {
        return purchaseHistoryViewService.findAllByPage(page, size).map(purchaseHistoryViewMapper::toDto);
    }

    @GetMapping("/{customerId}/{transactionId}")
    public PurchaseHistoryViewDto findPurchaseHistoryById(@PathVariable Long customerId,
                                                          @PathVariable Long transactionId) {
        PurchaseHistoryViewId id = new PurchaseHistoryViewId(Customer.builder().id(customerId).build(), Transaction.builder().id(transactionId).build());
        return purchaseHistoryViewService.findById(id).map(purchaseHistoryViewMapper::toDto).orElseThrow(() -> new PurchaseHistoryViewNotFoundException(id));
    }
}
