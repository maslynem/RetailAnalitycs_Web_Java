--liquibase formatted sql

--changeset maslynem:1 splitStatements:false
CREATE OR REPLACE FUNCTION get_margin(method VARCHAR DEFAULT 'default ', count BIGINT DEFAULT 0)
    RETURNS TABLE
            (
                Customer_id  BIGINT,
                Group_ID     BIGINT,
                Group_Margin DOUBLE PRECISION
            )
AS
$$
DECLARE
    date TIMESTAMP;
BEGIN
    IF method = 'default' THEN
        RETURN QUERY SELECT ph.Customer_ID,
                            ph.Group_ID,
                            CAST(sum(ph.Group_Summ_Paid - ph.Group_Cost) AS DOUBLE PRECISION) AS Group_Margin
                     FROM purchase_history as ph
                     GROUP BY 1, 2;
    ELSIF method = 'period' THEN
        date := (SELECT analysis_formation FROM date_of_analysis_formation)::date - count;
        RETURN QUERY SELECT ph.Customer_ID,
                            ph.Group_ID,
                            CAST(sum(ph.Group_Summ_Paid - ph.Group_Cost) AS DOUBLE PRECISION) AS Group_Margin
                     FROM purchase_history as ph
                     WHERE ph.Transaction_DateTime >= date
                     GROUP BY 1, 2;
    ELSIF method = 'number of transactions' THEN
        RETURN QUERY SELECT t.Customer_ID,
                            t.Group_ID,
                            CAST(sum(Group_Summ_Paid - Group_Cost) AS DOUBLE PRECISION) AS Group_Margin
                     FROM (SELECT ph.Customer_ID,
                                  ph.Group_ID,
                                  ph.Group_Summ_Paid,
                                  ph.Group_Cost
                           FROM purchase_history as ph
                           ORDER BY Transaction_DateTime DESC
                           LIMIT count) AS t
                     GROUP BY 1, 2;
    END IF;
END;
$$ LANGUAGE plpgsql;

