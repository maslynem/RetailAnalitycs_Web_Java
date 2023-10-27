--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
SELECT setval('personal_data_customer_id_seq', count(*))
FROM personal_data;
SELECT setval('groups_sku_group_id_seq', count(*))
FROM groups_sku;
SELECT setval('cards_customer_card_id_seq', count(*))
FROM cards;
SELECT setval('sku_sku_id_seq', count(*))
FROM sku;
SELECT setval('transactions_transaction_id_seq', count(*))
FROM transactions;
