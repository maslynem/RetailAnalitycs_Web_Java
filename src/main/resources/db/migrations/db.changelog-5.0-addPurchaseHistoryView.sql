--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE OR REPLACE VIEW purchase_history AS
SELECT cards.Customer_ID                                  AS Customer_ID,
       transactions.transaction_id                        AS Transaction_ID,
       transactions.transaction_datetime                  AS Transaction_DateTime,
       sku.group_id                                       AS Group_ID,
       SUM(stores.sku_purchase_price * checks.sku_amount) AS Group_Cost,
       SUM(checks.sku_summ)                               AS Group_Summ,
       SUM(checks.sku_summ_paid)                          AS Group_Summ_Paid
FROM cards
         JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
         JOIN checks ON transactions.transaction_id = checks.transaction_id
         JOIN sku ON sku.sku_id = checks.sku_id
         JOIN stores ON sku.sku_id = stores.sku_id AND stores.transaction_store_id = transactions.transaction_store_id
GROUP BY cards.Customer_ID, transactions.transaction_id, transactions.transaction_datetime, sku.group_id;
