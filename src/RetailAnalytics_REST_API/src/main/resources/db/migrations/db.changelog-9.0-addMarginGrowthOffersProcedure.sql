--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE OR REPLACE FUNCTION sku_share_in_group(customer_id_ BIGINT, group_id_ BIGINT, sku_id_ BIGINT)
    RETURNS DOUBLE PRECISION AS
$$
BEGIN
    RETURN
                100 * CAST((SELECT count(DISTINCT t.transaction_id)
                            FROM transactions t
                                     JOIN cards c ON t.customer_card_id = c.customer_card_id
                                     JOIN checks ch ON t.transaction_id = ch.transaction_id
                                     JOIN sku ON ch.sku_id = sku.sku_id
                            WHERE customer_id = customer_id_
                              AND sku.group_id = group_id_
                              AND ch.sku_id = sku_id_) AS DOUBLE PRECISION) /
                (SELECT count(DISTINCT t.transaction_id)
                 FROM transactions t
                          JOIN cards c ON t.customer_card_id = c.customer_card_id
                          JOIN checks ch ON t.transaction_id = ch.transaction_id
                          JOIN sku ON ch.sku_id = sku.sku_id
                 WHERE customer_id = customer_id_
                   AND sku.group_id = group_id_);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION margin_growth_offers(groups_number BIGINT,
                                                max_churn_index DOUBLE PRECISION,
                                                max_stability_index DOUBLE PRECISION,
                                                max_sku_share DOUBLE PRECISION,
                                                allowable_margin_share DOUBLE PRECISION)
    RETURNS TABLE
            (
                Customer_ID          BIGINT,
                SKU_Name             TEXT,
                Offer_Discount_Depth DOUBLE PRECISION
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT tmp1.Customer_ID, tmp1.SKU_Name, Min_discount_round AS Offer_Discount_Depth
        FROM (WITH top_affin_groups AS
                       (SELECT aff.Customer_ID, Group_ID
                        FROM (SELECT g.Customer_ID,
                                     g.Group_ID,
                                     g.Group_Affinity_Index,
                                     rank()
                                     OVER (PARTITION BY g.Customer_ID ORDER BY Group_Affinity_Index DESC ) AS rk
                              FROM groups g) AS aff
                        WHERE rk <= groups_number),

                   margin_sku AS (SELECT tmp.Customer_ID, tmp.Group_ID, tmp.SKU_Name
                                  FROM (SELECT g.Customer_ID,
                                               g.Group_ID,
                                               sku.sku_name                                               AS SKU_Name,
                                               SKU_Retail_Price - SKU_Purchase_Price                      AS mar,
                                               rank()
                                               OVER (PARTITION BY g.Customer_ID, g.Group_ID
                                                   ORDER BY (SKU_Retail_Price - SKU_Purchase_Price) DESC) AS rk
                                        FROM groups g
                                                 JOIN sku ON g.Group_ID = sku.group_id
                                                 JOIN customers c ON c.customer_id = g.Customer_ID
                                                 JOIN stores s ON s.transaction_store_id = c.customer_primary_store AND
                                                                  sku.sku_id = s.sku_id) AS tmp
                                  WHERE rk <= 1)

              SELECT g.Customer_ID,
                     sku.sku_name                            AS SKU_Name,
                     (s.SKU_Retail_Price - s.SKU_Purchase_Price) * allowable_margin_share /
                     SKU_Retail_Price                        AS acceptable,
                     ceil(g.Group_Minimum_Discount * 20) * 5 AS Min_discount_round
              FROM groups g
                       JOIN sku ON g.Group_ID = sku.group_id
                       JOIN customers c ON c.customer_id = g.Customer_ID
                       JOIN stores s ON s.transaction_store_id = c.customer_primary_store AND sku.sku_id = s.sku_id
                       JOIN top_affin_groups ag ON ag.Customer_ID = g.Customer_ID AND ag.Group_ID = g.Group_ID
                       JOIN margin_sku ms ON g.Customer_ID = ms.Customer_ID AND g.Group_ID = ms.Group_ID AND
                                             sku.sku_name = ms.SKU_Name
              WHERE Group_Churn_Rate <= max_churn_index
                AND Group_Stability_Index < max_stability_index
                AND sku_share_in_group(g.Customer_ID, g.Group_ID, s.sku_id) <=
                    max_sku_share) tmp1
        WHERE Min_discount_round < tmp1.acceptable;
END;
$$ LANGUAGE plpgsql;
--
SELECT *
FROM margin_growth_offers(5, 3, 0.5, 100, 30);
