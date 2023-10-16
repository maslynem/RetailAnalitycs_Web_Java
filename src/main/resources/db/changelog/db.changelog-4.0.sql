--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE VIEW periods AS
WITH t AS (SELECT cards.Customer_ID                           AS "Customer_ID",
                  COUNT(DISTINCT transactions.transaction_id) AS "Group_Purchase",
                  MIN(transactions.transaction_datetime)      AS first_group_purchase_date,
                  MAX(transactions.transaction_datetime)      AS last_group_purchase_date,
                  sku.group_id                                AS "Group_ID"
           FROM cards
                    JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
                    JOIN checks ON transactions.transaction_id = checks.transaction_id
                    JOIN sku ON sku.sku_id = checks.sku_id
           GROUP BY cards.Customer_ID, sku.group_id),
     t1 AS (SELECT cards.customer_id                                       AS customer_id,
                   sku.group_id                                            AS group_id,
                   min(checks.sku_discount / checks.sku_summ) AS min_discount
            FROM cards
                     JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
                     JOIN checks ON transactions.transaction_id = checks.transaction_id AND checks.sku_discount > 0
                     JOIN sku ON sku.sku_id = checks.sku_id
            GROUP BY cards.customer_id, sku.group_id)
SELECT t."Customer_ID",
       t."Group_ID",
       first_group_purchase_date AS "First_Group_Purchase_Date",
       last_group_purchase_date  AS "Last_Group_Purchase_Date",
       "Group_Purchase",
       (abs(EXTRACT(EPOCH FROM first_group_purchase_date - last_group_purchase_date) / 86400) + 1) /
       "Group_Purchase"          AS "Group_Frequency",
       coalesce(min_discount,0)              AS "Group_Min_Discount"
FROM t
         LEFT JOIN t1 ON t1.customer_id = t."Customer_ID" AND t1.group_id = t."Group_ID";
