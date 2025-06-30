--liquibase formatted sql
--changeset rafael.ramos:2 dbms:postgresql

--------- UPDATE TABLE session ------------
ALTER TABLE agenda
ALTER COLUMN description SET NOT NULL;

UPDATE agenda SET description = 'Sem descrição elaborada.';
