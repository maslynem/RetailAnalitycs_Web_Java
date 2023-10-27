package ru.school.retailanalitycs_web_java.services.viewServices;

import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryView;
import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryViewId;
import ru.school.retailanalitycs_web_java.repositories.viewRepositories.PurchaseHistoryViewRepository;

@Service
public class PurchaseHistoryViewService extends ReadOnlyBaseService<PurchaseHistoryView, PurchaseHistoryViewId> {
    protected PurchaseHistoryViewService(PurchaseHistoryViewRepository repository) {
        super(repository);
    }
}
