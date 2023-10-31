package ru.school.retailanalitycs_web_java.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.school.retailanalitycs_web_java.exceptions.LoadSqlFileException;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MaterializedViewsUpdateDao {
    private static final String DROP_ALL_VIEWS_FILE = "classpath:db/dropAllMaterializeViews.sql";
    private static final String CUSTOMER_VIEW_FILE = "classpath:db/migrations/db.changelog-3.1-addCustomerView.sql";
    private static final String PERIODS_VIEW_FILE = "classpath:db/migrations/db.changelog-4.0-addPeriodsView.sql";
    private static final String PURCHASE_HISTORY_VIEW_FILE = "classpath:db/migrations/db.changelog-5.0-addPurchaseHistoryView.sql";
    private static final String GROUPS_HISTORY_VIEW_FILE = "classpath:db/migrations/db.changelog-6.1-addGroupsView.sql";
    private static final String FUNCTIONS_VIEW_FILE = "classpath:db/migrations/db.changelog-7.0-addViewsForFrequencyOfVisitsProcedure.sql";

    private final JdbcTemplate jdbcTemplate;

    public void updateMaterializeViews() {
        jdbcTemplate.update(loadQuery(DROP_ALL_VIEWS_FILE));
        jdbcTemplate.update(loadQuery(CUSTOMER_VIEW_FILE));
        jdbcTemplate.update(loadQuery(PERIODS_VIEW_FILE));
        jdbcTemplate.update(loadQuery(PURCHASE_HISTORY_VIEW_FILE));
        jdbcTemplate.update(loadQuery(GROUPS_HISTORY_VIEW_FILE));
        jdbcTemplate.update(loadQuery(FUNCTIONS_VIEW_FILE));
    }

    private String loadQuery(String path) {
        try {
            File file = ResourceUtils.getFile(path);
            return Files.readAllLines(file.toPath())
                    .stream()
                    .filter(s -> !s.startsWith("--"))
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            log.error("Failed to load query {}: {}", path, e.getMessage(), e);
            throw new LoadSqlFileException(String.format("Failed to load query. Message: [%s]", e.getMessage()));
        }
    }
}
