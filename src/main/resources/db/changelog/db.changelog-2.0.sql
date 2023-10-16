--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE FUNCTION get_total_segment(segment1 VARCHAR,
                                             segment2 VARCHAR,
                                             segment3 VARCHAR) RETURNS INT AS
$$
DECLARE
    "check"   INT;
frequency INT;
churn     INT;
BEGIN
    IF segment1 = 'Low' THEN
        "check" := 0;
ELSIF segment1 = 'Medium' THEN
        "check" := 9;
ELSIF segment1 = 'High' THEN
        "check" := 18;
END IF;

IF segment2 = 'Rarely' THEN
        frequency := 0;
ELSIF segment2 = 'Occasionally' THEN
        frequency := 3;
ELSIF segment2 = 'Often' THEN
        frequency := 6;
END IF;

IF segment3 = 'Low' THEN
        churn := 1;
ELSIF segment3 = 'Medium' THEN
        churn := 2;
ELSIF segment3 = 'High' THEN
        churn := 3;
END IF;

RETURN churn + frequency + "check";
END;
$$
    LANGUAGE plpgsql;

CREATE VIEW customers AS
    -- Получаем средний чек клиента и нумеруем от большего к меньшему
WITH customer_avg_check AS (SELECT p.customer_id,
                                   avg(transaction_summ)                                   AS Customer_Average_Check,
                                   row_number() over (order by avg(transaction_summ) desc) AS counter
                            FROM personal_data p
                                     LEFT JOIN cards c ON p.customer_id = c.customer_id
                                     LEFT JOIN transactions t on c.customer_card_id = t.customer_card_id
                            WHERE transaction_summ IS NOT NULL
                            GROUP BY p.customer_id),
     -- Находим сегмент для среднего чека
     check_segment_info AS (SELECT c.customer_id,
                                   CASE
                                       WHEN c.counter * 100.0 / (SELECT max(counter) FROM customer_avg_check) <= 10
                                           THEN 'High'
                                       WHEN c.counter * 100.0 / (SELECT max(counter) FROM customer_avg_check) <= 35
                                           THEN 'Medium'
                                       ELSE 'Low'
                                       END AS Customer_Average_Check_Segment
                            FROM customer_avg_check AS c),
     -- Находим Customer_Frequency, нумеруем от меньшего к большему, находим Customer_Inactive_Period
     customer_time_info AS (SELECT p.customer_id,
                                   EXTRACT(EPOCH FROM
                                           (max(transaction_datetime) - min(transaction_datetime)) /
                                           count(transaction_id)) /
                                   86400                              AS Customer_Frequency,
                                   row_number() over (ORDER BY EXTRACT(EPOCH FROM
                                                                       (max(transaction_datetime) - min(transaction_datetime)) /
                                                                       count(transaction_id)) /
                                                               86400) AS freq_counter,
                                        EXTRACT(EPOCH FROM (SELECT max(analysis_formation) FROM date_of_analysis_formation) - max(transaction_datetime)) /
                                        86400                              AS Customer_Inactive_Period
                            FROM personal_data p
                                     LEFT JOIN cards c ON p.customer_id = c.customer_id
                                     LEFT JOIN transactions t on c.customer_card_id = t.customer_card_id
                            WHERE transaction_summ IS NOT NULL
                            GROUP BY p.customer_id),
     frequency_churn_segment_info AS (SELECT c.customer_id,
                                             CASE
                                                 WHEN c.Customer_Frequency = 0
                                                     THEN NULL
                                                 WHEN c.freq_counter * 100.0 /
                                                      (SELECT max(freq_counter) FROM customer_time_info) <= 10
                                                     THEN 'Often'
                                                 WHEN c.freq_counter * 100.0 /
                                                      (SELECT max(freq_counter) FROM customer_time_info) <= 35
                                                     THEN 'Occasionally'
                                                 ELSE 'Rarely'
                                                 END                                                       AS Customer_Frequency_Segment,
                                             CASE
                                                 WHEN c.Customer_Frequency = 0
                                                     THEN NULL
                                                 ELSE
                                                         c.Customer_Inactive_Period / c.Customer_Frequency END AS Customer_Churn_Rate,
                                             CASE
                                                 WHEN c.Customer_Frequency = 0
                                                     THEN NULL
                                                 WHEN c.Customer_Inactive_Period / c.Customer_Frequency BETWEEN 0 AND 2
                                                     THEN 'Low'
                                                 WHEN c.Customer_Inactive_Period / c.Customer_Frequency BETWEEN 2 AND 5
                                                     THEN 'Medium'
                                                 ELSE 'High'
                                                 END                                                       AS Customer_Churn_Segment
                                      FROM customer_time_info AS c),
     total_segment AS (SELECT c.customer_id,
                              c.Customer_Average_Check_Segment,
                              f.Customer_Frequency_Segment,
                              f.Customer_Churn_Rate,
                              f.Customer_Churn_Segment,
                              get_total_segment(Customer_Average_Check_Segment, Customer_Frequency_Segment,
                                                Customer_Churn_Segment) AS Customer_Segment
                       FROM frequency_churn_segment_info AS f
                                LEFT JOIN check_segment_info c ON c.customer_id = f.customer_id),
     -- Получаем в каких магазинах и сколько раз закупался клиент и дату последнего посещения
     customers_shop AS (SELECT p.customer_id,
                               t.transaction_store_id,
                               max(t.transaction_datetime)   AS last_tr,
                               count(t.transaction_store_id) AS transaction_count
                        FROM personal_data p
                                 LEFT JOIN cards c ON p.customer_id = c.customer_id
                                 LEFT JOIN transactions t on c.customer_card_id = t.customer_card_id
                        WHERE t.transaction_summ IS NOT NULL
                        GROUP BY p.customer_id, t.transaction_store_id),
     -- Получаем информацию в каком магазине и когда закупался клиент, каждая строка для клиента пронумерована (каждая группа отсортирована по убыванию даты
     -- Затем из этой информации для каждой группы оставляем по 3 первых записей и проверяем одинаковый ли там магазин (остальные отсеиваются)
     main_store_case1 AS (SELECT t.customer_id, t.transaction_store_id as Customer_Primary_Store
                          FROM (SELECT pd.customer_id,
                                       td.transaction_datetime,
                                       td.transaction_store_id,
                                       row_number()
                                           over (partition by pd.customer_id ORDER BY td.transaction_datetime DESC) AS flag
                                FROM personal_data pd
                                         LEFT JOIN cards cd ON pd.customer_id = cd.customer_id
                                         LEFT JOIN transactions td on cd.customer_card_id = td.customer_card_id
                                WHERE transaction_summ IS NOT NULL) AS t
                          WHERE t.flag BETWEEN 1 AND 3
                          GROUP BY t.customer_id, t.transaction_store_id
                          HAVING count(t.transaction_store_id) = 3),
     -- В таблице customers_shop группируем по клиенту, сортируем вначале по кол-ву транзакций, затем по дате и нумеруем
     -- Так запись под №1 будет соответствовать наибольшему числу транзакций у клиента и при этом транзакция была последней
     -- Получаем эти данные
     main_store_case2 AS (SELECT t.customer_id, t.transaction_store_id AS Customer_Primary_Store
                          FROM (SELECT *,
                                       row_number()
                                           over (partition by customer_id ORDER BY transaction_count DESC, last_tr DESC) AS flag
                                FROM customers_shop) AS t
                          WHERE t.flag = 1),
     -- Получаем данные из main_store_case2 за исключением клиентов указанных в main_store_case1 и добавляем клиентов из main_store_case1
     main_store AS (SELECT *
                    FROM main_store_case2
                    EXCEPT
                    SELECT *
                    FROM main_store_case1
                    UNION
                    SELECT *
                    FROM main_store_case1)
SELECT p.customer_id,
       Customer_Average_Check,
       Customer_Average_Check_Segment,
       Customer_Frequency,
       Customer_Frequency_Segment,
       Customer_Inactive_Period,
       Customer_Churn_Rate,
       Customer_Churn_Segment,
       Customer_Segment,
       Customer_Primary_Store
FROM personal_data p
         LEFT JOIN customer_avg_check AS c ON c.customer_id = p.customer_id
         LEFT JOIN total_segment t ON t.customer_id = p.customer_id
         LEFT JOIN customer_time_info ct ON ct.customer_id = p.customer_id
         LEFT JOIN main_store m ON m.customer_id = p.customer_id
WHERE Customer_Average_Check IS NOT NULL;
