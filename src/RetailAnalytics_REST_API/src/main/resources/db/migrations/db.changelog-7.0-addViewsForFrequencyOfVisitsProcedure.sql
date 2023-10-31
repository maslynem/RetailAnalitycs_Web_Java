--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE MATERIALIZED VIEW average_margin AS
SELECT sum(stores.sku_retail_price - stores.sku_purchase_price) / sum(stores.sku_retail_price) AS avg_margin,
       sku.group_id                                                                            AS group_id
FROM sku
         JOIN stores ON stores.sku_id = sku.sku_id
GROUP BY sku.group_id;

CREATE INDEX IF NOT EXISTS average_margin_idx ON average_margin USING btree (group_id);

CREATE MATERIALIZED VIEW groups_sorted_by_affinity_index AS
SELECT *
FROM groups as g
ORDER BY g.Customer_ID, g.Group_Affinity_Index DESC;


