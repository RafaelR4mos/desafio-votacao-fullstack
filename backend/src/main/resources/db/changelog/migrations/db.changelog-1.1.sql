--liquibase formatted sql
--changeset rafael.ramos:1 dbms:postgresql

--------- UPDATE TABLE ASSOCIATE ------------
ALTER TABLE associate
ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE associate SET active = TRUE;