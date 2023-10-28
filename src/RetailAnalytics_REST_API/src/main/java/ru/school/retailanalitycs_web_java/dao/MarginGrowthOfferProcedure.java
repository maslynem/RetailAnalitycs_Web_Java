package ru.school.retailanalitycs_web_java.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.school.retailanalitycs_web_java.entities.functionResults.MarginGrowthOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MarginGrowthOfferProcedure {
    private static final String MARGIN_GROWTH_OFFER_QUERY = "SELECT * FROM margin_growth_offers(?, ?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public List<MarginGrowthOffer> getFrequencyOfVisit(Long groupsNumber,
                                                       Double maxChurnIndex,
                                                       Double maxStabilityIndex,
                                                       Double maxSkuShare,
                                                       Double allowableMarginShare) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(MARGIN_GROWTH_OFFER_QUERY,
                groupsNumber, maxChurnIndex,
                maxStabilityIndex, maxSkuShare,
                allowableMarginShare);
        return map(maps);
    }

    private List<MarginGrowthOffer> map(List<Map<String, Object>> maps) {
        List<MarginGrowthOffer> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            MarginGrowthOffer marginGrowthOffer = new MarginGrowthOffer();
            marginGrowthOffer.setCustomerId((Long) map.get("customer_id"));
            marginGrowthOffer.setSkuName((String) map.get("sku_name"));
            marginGrowthOffer.setOfferDiscountDepth((Double) map.get("offer_discount_depth"));
            result.add(marginGrowthOffer);
        }
        return result;
    }
}
