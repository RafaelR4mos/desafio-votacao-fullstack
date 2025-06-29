--liquibase formatted sql
--changeset rafael.ramos:1 dbms:postgresql

--------- UPDATE TABLE ASSOCIATE ------------
ALTER TABLE session
DROP COLUMN total_associates;