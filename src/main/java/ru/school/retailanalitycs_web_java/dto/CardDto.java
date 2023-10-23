package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class CardDto extends BaseDto<Integer> {

    @CsvBindByName(column = "customer_card_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 1)
    private Integer customer;
}
