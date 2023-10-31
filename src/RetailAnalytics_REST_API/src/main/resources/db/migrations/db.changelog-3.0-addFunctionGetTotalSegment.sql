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

