--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
DROP MATERIALIZED VIEW IF EXISTS average_margin;
CREATE MATERIALIZED VIEW average_margin AS
SELECT sum(stores.sku_retail_price - stores.sku_purchase_price) / sum(stores.sku_retail_price) AS avg_margin,
       sku.group_id                                                                            AS group_id
FROM sku
         JOIN stores ON stores.sku_id = sku.sku_id
GROUP BY sku.group_id;

CREATE INDEX IF NOT EXISTS average_margin_idx ON average_margin USING btree (group_id);

DROP MATERIALIZED VIEW IF EXISTS groups_sorted_by_affinity_index;
CREATE MATERIALIZED VIEW groups_sorted_by_affinity_index AS
SELECT *
FROM groups as g
ORDER BY g.Customer_ID, g.Group_Affinity_Index DESC;

SELECT *
FROM groups_sorted_by_affinity_index;

DROP FUNCTION IF EXISTS get_frequency_of_visits CASCADE;
CREATE OR REPLACE FUNCTION get_frequency_of_visits(first_date TIMESTAMP, last_date TIMESTAMP, added_transactions BIGINT,
                                                   maximum_churn_index DOUBLE PRECISION,
                                                   maximum_share_of_transactions DOUBLE PRECISION,
                                                   allowable_margin_share DOUBLE PRECISION)
    RETURNS TABLE
            (
                Customer_ID                 BIGINT,
                Start_Date                  TIMESTAMP,
                End_Date                    TIMESTAMP,
                Required_Transactions_Count DOUBLE PRECISION,
                Group_Name                  TEXT,
                Offer_Discount_Depth        DOUBLE PRECISION
            )
AS
$$
BEGIN
    IF first_date > last_date THEN
        RAISE EXCEPTION 'first_date cannot be greater than last_date';
    END IF;

    RETURN QUERY
        SELECT DISTINCT groups.Customer_ID,
                        first_date,
                        last_date,
                        round((abs(EXTRACT(EPOCH FROM first_date - last_date) / 86400)) /
                              (SELECT customer_frequency
                               FROM customers
                               WHERE customers.customer_id = groups.Customer_ID))::DOUBLE PRECISION +
                        added_transactions,
                        groups_sku.group_name,
                        ceil(groups.Group_Minimum_Discount * 20) * 5
        FROM groups
                 JOIN groups_sku ON groups_sku.group_id = groups.Group_ID
            AND groups.Group_Churn_Rate <= maximum_churn_index
            AND groups.Group_Discount_Share * 100 < maximum_share_of_transactions
            AND ceil(groups.Group_Minimum_Discount * 20) * 5
                                        <
                ((SELECT avg_margin FROM average_margin WHERE average_margin.group_id = groups_sku.group_id) *
                 allowable_margin_share);
END;
$$ LANGUAGE plpgsql;

SET datestyle = 'european';
--
-- SELECT *
-- FROM get_frequency_of_visits('2022-08-18 00:00:00', '2022-08-18 00:00:00', 1, 3, 70, 30);
