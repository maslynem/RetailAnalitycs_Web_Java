--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE FUNCTION get_margin(method VARCHAR DEFAULT 'default ', count INT DEFAULT 0)
    RETURNS TABLE
            (
                Customer_ID  INT,
                Group_ID     INT,
                Group_Margin DOUBLE PRECISION
            )
AS
$$
DECLARE
    date TIMESTAMP;
BEGIN
    IF method = 'default' THEN
        RETURN QUERY SELECT customer_id,
                            group_id,
                            CAST(sum(Group_Summ_Paid - Group_Cost) AS DOUBLE PRECISION) AS group_margin
                     FROM purchase_history
                     GROUP BY 1, 2;
    ELSIF method = 'period' THEN
        date := (SELECT analysis_formation FROM date_of_analysis_formation)::date - count;
        RETURN QUERY SELECT customer_id,
                            group_id,
                            CAST(sum(Group_Summ_Paid - Group_Cost) AS DOUBLE PRECISION) AS group_margin
                     FROM purchase_history
                     WHERE transaction_datetime >= date
                     GROUP BY 1, 2;
    ELSIF method = 'number of transactions' THEN
        RETURN QUERY SELECT t.customer_id,
                            group_id,
                            CAST(sum(Group_Summ_Paid - Group_Cost) AS DOUBLE PRECISION) AS group_margin
                     FROM (SELECT customer_id,
                                  group_id,
                                  Group_Summ_Paid,
                                  Group_Cost
                           FROM purchase_history
                           ORDER BY transaction_datetime DESC
                           LIMIT count) AS t
                     GROUP BY 1, 2;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE VIEW groups AS
WITH new_purchase_history AS (SELECT cards.Customer_ID                 AS customer_id,
                                     transactions.transaction_id       AS transaction_id,
                                     transactions.transaction_datetime AS transaction_datetime,
                                     sku.group_id                      AS group_id
                              FROM cards
                                       JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
                                       JOIN checks ON transactions.transaction_id = checks.transaction_id
                                       JOIN sku ON sku.sku_id = checks.sku_id
                              GROUP BY cards.Customer_ID, transactions.transaction_id,
                                       transactions.transaction_datetime, sku.group_id),
     aff_ind AS (SELECT DISTINCT ph.customer_id,
                                 p.group_id,
                                 count(DISTINCT ph.transaction_id) AS tr_count
                 FROM new_purchase_history ph
                          JOIN periods p ON ph.customer_id = p.customer_id
                 WHERE ph.transaction_datetime BETWEEN p.first_group_purchase_date AND p.Last_Group_Purchase_Date
                 GROUP BY ph.customer_id, p.group_id),

     stab AS (SELECT tmp.customer_id,
                     tmp.group_id,
                     avg(abs(diff - p2.Group_Frequency) / p2.Group_Frequency) AS Group_Stability_Index
              FROM (SELECT customer_id,
                           group_id,
                           transaction_id,
                           transaction_datetime,
                           EXTRACT(EPOCH FROM (transaction_datetime - lag(transaction_datetime, 1)
                                                                       OVER (PARTITION BY customer_id, group_id ORDER BY transaction_datetime))) /
                           86400::int AS diff
                    FROM new_purchase_history
                    ORDER BY 1, 2, 4) AS tmp
                       JOIN periods p2 ON p2.group_id = tmp.group_id AND p2.customer_id = tmp.customer_id
              WHERE diff IS NOT NULL
              GROUP BY 1, 2),
     dshare AS (SELECT customer_id,
                       sku.group_id,
                       CAST(count(CASE WHEN sku_discount <> 0 THEN t.transaction_id END) AS DOUBLE PRECISION) /
                       count(t.transaction_id)
                           AS Group_Discount_Share
                FROM cards c
                         JOIN transactions t ON c.customer_card_id = t.customer_card_id
                         JOIN checks ch ON t.transaction_id = ch.transaction_id
                         JOIN sku ON ch.sku_id = sku.sku_id
                GROUP BY 1, 2),

     avgd AS (SELECT customer_id,
                     group_id,
                     1 - sum(sku_discount) / sum(sku_summ) AS Group_Average_Discount
              FROM (SELECT cards.Customer_ID AS customer_id,
                           sku.group_id      AS group_id,
                           sku_summ,
                           sku_discount
                    FROM cards
                             JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
                             JOIN checks ON transactions.transaction_id = checks.transaction_id
                             JOIN sku ON sku.sku_id = checks.sku_id) AS ph2
              WHERE sku_discount > 0
              GROUP BY 1, 2)
SELECT p.customer_id,
       p.group_id,
       p.group_purchase::DOUBLE PRECISION / aff_ind.tr_count                AS group_affinity_index,
       (SELECT EXTRACT(epoch FROM (SELECT max(analysis_formation) FROM date_of_analysis_formation) -
                                  p.Last_Group_Purchase_Date)) / 86400::int /
       p.Group_Frequency                                                 AS Group_Churn_Rate,
       Group_Stability_Index,
       gm.Group_Margin                                                      AS group_margin,
       Group_Discount_Share,
       CASE WHEN p.Group_Min_Discount > 0 THEN p.Group_Min_Discount END AS Group_Minimum_Discount,
       Group_Average_Discount

FROM periods p
         LEFT JOIN aff_ind ON p.customer_id = aff_ind.customer_id AND p.group_id = aff_ind.group_id
         LEFT JOIN stab ON p.group_id = stab.group_id AND p.customer_id = stab.customer_id
         LEFT JOIN dshare ON p.customer_id = dshare.customer_id AND p.group_id = dshare.group_id
         LEFT JOIN avgd ON p.customer_id = avgd.customer_id AND p.group_id = avgd.group_id
         LEFT JOIN get_margin(method := 'default', count := null) AS gm
                   ON p.customer_id = gm.Customer_ID AND p.group_id = gm.Group_ID;

