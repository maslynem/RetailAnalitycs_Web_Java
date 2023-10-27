package ru.school.retailanalitycs_web_java.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.school.retailanalitycs_web_java.entities.functionResults.FrequencyOfVisit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class FrequencyOfVisitProcedure {
    private final JdbcTemplate jdbcTemplate;
    private static final String FREQUENCY_QUERY = "SELECT * from get_frequency_of_visits(?::timestamp, ?::timestamp, ?, ?, ?, ?)";

    public FrequencyOfVisitProcedure(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FrequencyOfVisit> getFrequencyOfVisit(LocalDateTime firstDate,
                                                      LocalDateTime lastDate,
                                                      Long addedTransactions,
                                                      Double maxChurnIndex,
                                                      Double maxShareOfTransactions,
                                                      Double allowableMarginShare) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(FREQUENCY_QUERY, firstDate, lastDate, addedTransactions, maxChurnIndex, maxShareOfTransactions, allowableMarginShare);
        return map(maps);
    }

    private List<FrequencyOfVisit> map(List<Map<String, Object>> maps) {
        List<FrequencyOfVisit> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            FrequencyOfVisit frequencyOfVisit = new FrequencyOfVisit();
            frequencyOfVisit.setCustomerId((Long) map.get("customer_id"));
            frequencyOfVisit.setStartDate(((Timestamp) map.get("start_date")).toLocalDateTime());
            frequencyOfVisit.setEndDate(((Timestamp) map.get("end_date")).toLocalDateTime());
            frequencyOfVisit.setRequiredTransactionsCount((Double) map.get("required_transactions_count"));
            frequencyOfVisit.setGroupName((String) map.get("group_name"));
            frequencyOfVisit.setOfferDiscountDepth((Double) map.get("offer_discount_depth"));
            result.add(frequencyOfVisit);
        }
        return result;

    }
}
