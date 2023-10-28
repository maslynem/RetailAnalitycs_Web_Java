--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE FUNCTION get_reward(max_churn_index DOUBLE PRECISION,
                           max_share_of_transactions_with_a_discount INT,
                           allowable_share_of_margin INT)
    RETURNS TABLE
            (
                customer_id          BIGINT,
                group_name           TEXT,
                offer_discount_depth DOUBLE PRECISION
            )
AS
$$
BEGIN
    RETURN QUERY (WITH avg_margin AS (SELECT sum(stores.sku_retail_price - stores.sku_purchase_price) /
                                             sum(stores.sku_retail_price) AS avg,
                                             sku.group_id                 AS group_id
                                      FROM sku
                                               JOIN stores ON stores.sku_id = sku.sku_id
                                      GROUP BY sku.group_id),
                       groups_with_max_dis AS (SELECT p.customer_id,
                                                      g."Group_ID",
                                                      g."Group_Affinity_Index",
                                                      avg_margin.avg * allowable_share_of_margin                                AS max_dis,
                                                      row_number()
                                                      OVER (PARTITION BY p.customer_id ORDER BY g."Group_Affinity_Index" DESC ) AS flag
                                               FROM personal_data AS p
                                                        LEFT JOIN groups g on p.customer_id = g."Customer_ID"
                                                        LEFT JOIN avg_margin ON g."Group_ID" = avg_margin.group_id
                                               WHERE g."Group_Churn_Rate" < max_churn_index
                                                 AND g."Group_Discount_Share" * 100 <
                                                     max_share_of_transactions_with_a_discount),
                       temp AS (SELECT t.customer_id,
                                       t."Group_ID",
                                       t."Group_Affinity_Index",
                                       t.flag,
                                       t.max_dis,
                                       ceil(g."Group_Minimum_Discount" * 20) * 5                      AS new_discount,
                                       row_number() OVER (PARTITION BY g."Customer_ID" ORDER BY flag) AS flag2
                                FROM groups_with_max_dis AS t
                                         LEFT JOIN groups AS g
                                                   ON t.customer_id = g."Customer_ID" AND t."Group_ID" = g."Group_ID"
                                WHERE g."Group_Minimum_Discount" != 0
                                  AND ceil(g."Group_Minimum_Discount" * 20) * 5 < t.max_dis)
                  SELECT t.customer_id, g.group_name, t.new_discount
                  FROM temp AS t
                           LEFT JOIN groups_sku g ON t."Group_ID" = g.group_id
                  WHERE t.flag2 = 1);
END ;
$$ LANGUAGE plpgsql;

CREATE FUNCTION get_personal_offer_growth_of_the_average_check(
    average_check_calculation_method INT,
    first_date TIMESTAMP,
    last_date TIMESTAMP,
    coefficient_of_average_check_increase DOUBLE PRECISION,
    max_churn_index DOUBLE PRECISION,
    max_share_of_transactions_with_a_discount INT,
    allowable_share_of_margin INT)
    RETURNS TABLE
            (
                customer_id            BIGINT,
                required_check_measure DOUBLE PRECISION,
                group_name             TEXT,
                offer_discount_depth   DOUBLE PRECISION
            )
AS
$$
BEGIN
    IF average_check_calculation_method = 1 THEN
        IF first_date IS NULL
            OR first_date < (SELECT min(transaction_datetime) FROM transactions)
            OR first_date > last_date THEN
            first_date = (SELECT min(transaction_datetime) FROM transactions);
        END IF;

        IF last_date IS NULL
            OR last_date > (SELECT max(transaction_datetime) FROM transactions)
            OR last_date < first_date THEN
            last_date = (SELECT max(transaction_datetime) FROM transactions);
        END IF;

        RETURN QUERY
            WITH required_check_measure AS (SELECT p.customer_id,
                                                   CAST(avg(transaction_summ) * coefficient_of_average_check_increase AS DOUBLE PRECISION) AS required_check_measure
                                            FROM personal_data AS p
                                                     LEFT JOIN cards c on p.customer_id = c.customer_id
                                                     LEFT OUTER JOIN transactions t on c.customer_card_id = t.customer_card_id
                                            WHERE transaction_datetime BETWEEN first_date AND last_date
                                            GROUP BY p.customer_id)
            SELECT c.customer_id, c.required_check_measure, r.group_name, r.offer_discount_depth
            FROM required_check_measure AS c
                     JOIN get_reward(max_churn_index, max_share_of_transactions_with_a_discount,
                                     allowable_share_of_margin) AS r ON c.customer_id = r.customer_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION get_personal_offer_growth_of_the_average_check(
    average_check_calculation_method INT,
    number_of_transactions INT,
    coefficient_of_average_check_increase DOUBLE PRECISION,
    max_churn_index DOUBLE PRECISION,
    max_share_of_transactions_with_a_discount INT,
    allowable_share_of_margin INT)
    RETURNS TABLE
            (
                customer_id            BIGINT,
                required_check_measure DOUBLE PRECISION,
                group_name             TEXT,
                offer_discount_depth   DOUBLE PRECISION
            )
AS
$$
BEGIN
    IF average_check_calculation_method = 2 THEN
        RETURN QUERY
            WITH all_tr AS (SELECT p.customer_id,
                                   transaction_summ,
                                   transaction_datetime,
                                   row_number()
                                   OVER (PARTITION BY p.customer_id ORDER BY transaction_datetime DESC ) AS flag
                            FROM personal_data AS p
                                     LEFT JOIN cards c on p.customer_id = c.customer_id
                                     LEFT OUTER JOIN transactions t on c.customer_card_id = t.customer_card_id
                            WHERE transaction_summ IS NOT NULL),
                 required_check_measure AS (SELECT t.customer_id,
                                                   CAST(avg(transaction_summ) * coefficient_of_average_check_increase AS DOUBLE PRECISION) AS required_check_measure
                                            FROM all_tr AS t
                                            WHERE t.flag <= number_of_transactions
                                            GROUP BY t.customer_id)
            SELECT c.customer_id, c.required_check_measure, r.group_name, r.offer_discount_depth
            FROM required_check_measure AS c
                     JOIN get_reward(max_churn_index, max_share_of_transactions_with_a_discount,
                                     allowable_share_of_margin) AS r ON c.customer_id = r.customer_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

SELECT *
FROM get_personal_offer_growth_of_the_average_check(1,
                                                    '2016-01-01',
                                                    '2023-01-01',
                                                    1,
                                                    1.4,
                                                    80,
                                                    50);
-- SELECT *
-- FROM get_personal_offer_growth_of_the_average_check(2,
--                                                     100,
--                                                     1.15,
--                                                     3,
--                                                     70,
--                                                     30);
