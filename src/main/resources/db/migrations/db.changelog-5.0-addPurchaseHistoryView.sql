--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE VIEW purchase_history AS
SELECT cards.Customer_ID                                  AS customer_id,
       transactions.transaction_id                        AS transaction_id,
       transactions.transaction_datetime                  AS transaction_datetime,
       sku.group_id                                       AS group_id,
       SUM(stores.sku_purchase_price * checks.sku_amount) AS group_cost,
       SUM(checks.sku_summ)                               AS group_summ,
       SUM(checks.sku_summ_paid)                          AS group_summ_paid
FROM cards
         JOIN transactions ON cards.customer_card_id = transactions.customer_card_id
         JOIN checks ON transactions.transaction_id = checks.transaction_id
         JOIN sku ON sku.sku_id = checks.sku_id
         JOIN stores ON sku.sku_id = stores.sku_id AND stores.transaction_store_id = transactions.transaction_store_id
GROUP BY cards.Customer_ID, transactions.transaction_id, transactions.transaction_datetime, sku.group_id;

