package ru.school.retailanalitycs_web_java.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.school.retailanalitycs_web_java.entities.functionResults.PersonalOffer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PersonalOfferGrowthProcedure {
    private static final String PERSONAL_OFFER_QUERY_1 = "SELECT * FROM get_personal_offer_growth_of_the_average_check(1, ?::timestamp, ?::timestamp, ?, ?, ?, ?)";
    private static final String PERSONAL_OFFER_QUERY_2 = "SELECT * FROM get_personal_offer_growth_of_the_average_check(2, ?, ?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public PersonalOfferGrowthProcedure(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PersonalOffer> getPersonalOfferGrowthOfTheAverageCheck(LocalDate firstDate,
                                                                       LocalDate lastDate,
                                                                       Double coefficientOfAverageCheckIncrease,
                                                                       Double maxChurnIndex,
                                                                       Integer maxShareOfTransactionsWithDiscount,
                                                                       Integer allowableShareOfMargin) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(PERSONAL_OFFER_QUERY_1,
                firstDate,
                lastDate,
                coefficientOfAverageCheckIncrease,
                maxChurnIndex,
                maxShareOfTransactionsWithDiscount,
                allowableShareOfMargin);
        return map(maps);
    }

    public List<PersonalOffer> getPersonalOfferGrowthOfTheAverageCheck(Integer numberOfTransactions,
                                                                       Double coefficientOfAverageCheckIncrease,
                                                                       Double maxChurnIndex,
                                                                       Integer maxShareOfTransactionsWithDiscount,
                                                                       Integer allowableShareOfMargin) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(PERSONAL_OFFER_QUERY_2,
                numberOfTransactions,
                coefficientOfAverageCheckIncrease,
                maxChurnIndex,
                maxShareOfTransactionsWithDiscount,
                allowableShareOfMargin);
        return map(maps);
    }

    private List<PersonalOffer> map(List<Map<String, Object>> maps) {
        List<PersonalOffer> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            PersonalOffer personalOffer = new PersonalOffer();
            personalOffer.setCustomerId((Long) map.get("customer_id"));
            personalOffer.setRequiredCheckMeasure((Double) map.get("required_check_measure"));
            personalOffer.setGroupName((String) map.get("group_name"));
            personalOffer.setOfferDiscountDepth((Double) map.get("offer_discount_depth"));
            result.add(personalOffer);
        }
        return result;

    }
}
