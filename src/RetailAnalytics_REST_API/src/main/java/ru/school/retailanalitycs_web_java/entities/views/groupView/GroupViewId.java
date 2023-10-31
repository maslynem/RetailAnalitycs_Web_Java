package ru.school.retailanalitycs_web_java.entities.views.groupView;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GroupViewId implements Serializable {
    @Serial
    private static final long serialVersionUID = -1663153295275842058L;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

//    @Column(name = "customer_id")
//    private Long customerId;

    //    @Column(name = "group_id")
    @ManyToOne
    @JoinColumn(name = "group_id")
    private SkuGroup skuGroup;

}