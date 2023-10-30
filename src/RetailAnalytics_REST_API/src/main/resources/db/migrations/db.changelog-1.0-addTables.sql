--liquibase formatted sql

--changeset maslynem:1
CREATE TABLE IF NOT EXISTS personal_data
(
    Customer_ID            BIGSERIAL PRIMARY KEY,
    Customer_Name          TEXT,
    Customer_Surname       TEXT,
    Customer_Primary_Email TEXT,
    Customer_Primary_Phone TEXT
);

CREATE TABLE IF NOT EXISTS cards
(
    Customer_Card_ID BIGSERIAL PRIMARY KEY,
    Customer_ID      BIGINT NOT NULL REFERENCES personal_data (Customer_ID)
);

CREATE INDEX ON cards (customer_id);

CREATE TABLE IF NOT EXISTS transactions
(
    Transaction_ID       BIGSERIAL PRIMARY KEY,
    Customer_Card_ID     BIGINT           NOT NULL REFERENCES cards (Customer_Card_ID),
    Transaction_Summ     DOUBLE PRECISION NOT NULL,
    Transaction_DateTime timestamp WITHOUT TIME ZONE,
    Transaction_Store_ID BIGINT
);

CREATE TABLE groups_sku
(
    Group_ID   BIGSERIAL PRIMARY KEY,
    Group_Name TEXT UNIQUE
);

CREATE TABLE sku
(
    SKU_ID   BIGSERIAL PRIMARY KEY,
    SKU_Name TEXT,
    Group_ID BIGINT NOT NULL REFERENCES groups_sku (Group_ID)
);

CREATE INDEX ON sku (Group_ID);

CREATE TABLE stores
(
    Transaction_Store_ID BIGINT,
    SKU_ID               BIGINT           NOT NULL REFERENCES sku (SKU_ID),
    SKU_Purchase_Price   DOUBLE PRECISION NOT NULL,
    SKU_Retail_Price     DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (Transaction_Store_ID, SKU_ID)
);

CREATE TABLE IF NOT EXISTS checks
(
    Transaction_ID BIGINT           NOT NULL REFERENCES transactions (Transaction_ID),
    SKU_ID         BIGINT           NOT NULL REFERENCES sku (SKU_ID),
    SKU_Amount     DOUBLE PRECISION NOT NULL,
    SKU_Summ       DOUBLE PRECISION NOT NULL,
    SKU_Summ_Paid  DOUBLE PRECISION NOT NULL,
    SKU_Discount   DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (Transaction_ID, SKU_ID)
);

CREATE TABLE Date_Of_Analysis_Formation
(
    Analysis_Formation TIMESTAMP WITHOUT TIME ZONE PRIMARY KEY
);


SET datestyle = 'european';
