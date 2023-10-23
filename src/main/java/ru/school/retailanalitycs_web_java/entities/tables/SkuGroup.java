package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "groups_sku")
public class SkuGroup {
    @Id
    @Column(name = "group_id", nullable = false)
    private Integer id;

    @Column(name = "group_name", length = Integer.MAX_VALUE)
    private String groupName;

}