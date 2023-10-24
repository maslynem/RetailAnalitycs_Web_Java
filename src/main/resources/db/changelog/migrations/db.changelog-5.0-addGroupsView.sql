--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE FUNCTION get_margin(method VARCHAR DEFAULT 'default ', count INT DEFAULT 0)
    RETURNS TABLE
(
    Customer_ID  INT,
    Group_ID     INT,
    Group_Margin NUMERIC
)
AS
$$
DECLARE
date TIMESTAMP;
BEGIN
    IF method = 'default' THEN
        RETURN QUERY SELECT "Customer_ID",
                            "Group_ID",
                            CAST(sum("Group_Summ_Paid" - "Group_Cost") AS NUMERIC) AS "Group_Margin"
                     FROM purchase_history
                     GROUP BY 1, 2;
ELSIF method = 'period' THEN
        date := (SELECT analysis_formation FROM date_of_analysis_formation)::date - count;
RETURN QUERY SELECT "Customer_ID",
                    "Group_ID",
                    CAST(sum("Group_Summ_Paid" - "Group_Cost") AS NUMERIC) AS "Group_Margin"
             FROM purchase_history
             WHERE "Transaction_DateTime" >= date
             GROUP BY 1, 2;
ELSIF method = 'number of transactions' THEN
        RETURN QUERY SELECT t."Customer_ID",
                            "Group_ID",
                            CAST(sum("Group_Summ_Paid" - "Group_Cost") AS NUMERIC) AS "Group_Margin"
                     FROM (SELECT "Customer_ID",
                                  "Group_ID",
                                  "Group_Summ_Paid",
                                  "Group_Cost"
                           FROM purchase_history
                           ORDER BY "Transaction_DateTime" DESC
                           LIMIT count) AS t
                     GROUP BY 1, 2;
END IF;
END;
$$ LANGUAGE plpgsql;

CREATE VIEW "groups" AS
WITH new_purchase_history AS (SELECT cards.Customer_ID                 AS "Customer_ID",
                                     transactions.transaction_id       AS "Transaction_ID",
                                     transactions.transaction_datetime AS "Transaction_DateTime",
                                     sku.group_id                      AS "Group_ID"
                              FROM cards
                                       JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
                                       JOIN checks ON transactions.transaction_id = checks.transaction_id
                                       JOIN sku ON sku.sku_id = checks.sku_id
                              GROUP BY cards.Customer_ID, transactions.transaction_id,
                                       transactions.transaction_datetime, sku.group_id),
     aff_ind AS (SELECT DISTINCT ph."Customer_ID",
                                 p."Group_ID",
                                 count(DISTINCT ph."Transaction_ID") AS tr_count
                 FROM new_purchase_history ph
                          JOIN periods p ON ph."Customer_ID" = p."Customer_ID"
                 WHERE ph."Transaction_DateTime" BETWEEN p."First_Group_Purchase_Date" AND p."Last_Group_Purchase_Date"
                 GROUP BY ph."Customer_ID", p."Group_ID"),

     stab AS (SELECT tmp."Customer_ID",
                     tmp."Group_ID",
                     avg(abs(diff - p2."Group_Frequency") / p2."Group_Frequency") AS "Group_Stability_Index"
              FROM (SELECT "Customer_ID",
                           "Group_ID",
                           "Transaction_ID",
                           "Transaction_DateTime",
                           EXTRACT(EPOCH FROM ("Transaction_DateTime" - lag("Transaction_DateTime", 1)
                               OVER (PARTITION BY "Customer_ID", "Group_ID" ORDER BY "Transaction_DateTime"))) /
                           86400::int AS diff
                    FROM new_purchase_history
                    ORDER BY 1, 2, 4) AS tmp
                       JOIN periods p2 ON p2."Group_ID" = tmp."Group_ID" AND p2."Customer_ID" = tmp."Customer_ID"
              WHERE diff IS NOT NULL
              GROUP BY 1, 2),
     dshare AS (SELECT customer_id,
                       sku.group_id,
                       CAST(count(CASE WHEN sku_discount <> 0 THEN t.transaction_id END) AS NUMERIC) /
                       count(t.transaction_id)
                           AS "Group_Discount_Share"
                FROM cards c
                         JOIN transactions t ON c.customer_card_id = t.customer_card_id
                         JOIN checks ch ON t.transaction_id = ch.transaction_id
                         JOIN sku ON ch.sku_id = sku.sku_id
                GROUP BY 1, 2),

     avgd AS (SELECT "Customer_ID",
                     "Group_ID",
                     1 - sum(sku_discount) / sum(sku_summ) AS "Group_Average_Discount"
              FROM (SELECT cards.Customer_ID AS "Customer_ID",
                           sku.group_id      AS "Group_ID",
                           sku_summ,
                           sku_discount
                    FROM cards
                             JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
                             JOIN checks ON transactions.transaction_id = checks.transaction_id
                             JOIN sku ON sku.sku_id = checks.sku_id) AS ph2
              WHERE sku_discount > 0
              GROUP BY 1, 2)
SELECT p."Customer_ID",
       p."Group_ID",
       CAST(p."Group_Purchase" AS NUMERIC) / aff_ind.tr_count               AS "Group_Affinity_Index",
       (SELECT EXTRACT(epoch FROM (SELECT max(analysis_formation) FROM date_of_analysis_formation) -
                                  p."Last_Group_Purchase_Date")) / 86400::int /
       p."Group_Frequency"                                                  AS "Group_Churn_Rate",
        "Group_Stability_Index",
       gm.Group_Margin                                                      AS "Group_Margin",
       "Group_Discount_Share",
       CASE WHEN p."Group_Min_Discount" > 0 THEN p."Group_Min_Discount" END AS "Group_Minimum_Discount",
       "Group_Average_Discount"

FROM periods p
         LEFT JOIN aff_ind ON p."Customer_ID" = aff_ind."Customer_ID" AND p."Group_ID" = aff_ind."Group_ID"
         LEFT JOIN stab ON p."Group_ID" = stab."Group_ID" AND p."Customer_ID" = stab."Customer_ID"
         LEFT JOIN dshare ON p."Customer_ID" = dshare.customer_id AND p."Group_ID" = dshare.group_id
         LEFT JOIN avgd ON p."Customer_ID" = avgd."Customer_ID" AND p."Group_ID" = avgd."Group_ID"
         LEFT JOIN get_margin(method := 'default', count := null) AS gm
ON p."Customer_ID" = gm.Customer_ID AND p."Group_ID" = gm.Group_ID;

