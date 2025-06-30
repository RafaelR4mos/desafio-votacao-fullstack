--liquibase formatted sql
--changeset rafael.ramos:3 dbms:postgresql

--------- SEEDING ------------

-- Inserção de agendas
INSERT INTO agenda (title, slug, description)
VALUES
  ('Revisão de Estatuto', 'revisao-estatuto', 'Discussão e aprovação do novo estatuto da associação'),
  ('Assembleia Geral', 'assembleia-geral', 'Votação sobre as novas diretrizes administrativas'),
   ('Aprovacao do Orcamento Anual 2025', 'aprovação-do-orçamento-anual-2025', 'Votação sobre as novas de orçamento anual');

-- Inserção de associados
INSERT INTO associate (id_associate, first_name, last_name, cpf, email, active)
VALUES
  ('3f35c3c7-91d0-42f7-a1d0-e1d05773e1c1', 'João', 'Silva', '12345678901', 'joao.silva@example.com', true),
  ('aad10cf4-b14a-48ef-9330-7b03487ab792', 'Maria', 'Souza', '98765432100', 'maria.souza@example.com', true),
  ('cfdeee74-2d83-4d3e-b3d4-f660dcaa29ea', 'Carlos', 'Pereira', '11122233344', 'carlos.pereira@example.com', true);

-- Inserção de sessões
INSERT INTO session (id_session, id_agenda, status, duration_seconds, started_at)
VALUES
  ('fd3f70f2-cb95-449e-91e8-00d2278a4cfa', 1, 'CLOSED', 300, CURRENT_TIMESTAMP),
  ('aef3f202-c95b-4111-9b90-11cde7989aaf', 2, 'DRAFT', 90, NULL),
   ('aef3f202-c95b-4111-9b90-11cde7989bbb', 3, 'DRAFT', 180, NULL);

-- Inserção de votos
INSERT INTO votes (id_session, id_associate, vote)
VALUES
  ('fd3f70f2-cb95-449e-91e8-00d2278a4cfa', '3f35c3c7-91d0-42f7-a1d0-e1d05773e1c1', 'SIM'),
  ('fd3f70f2-cb95-449e-91e8-00d2278a4cfa', 'aad10cf4-b14a-48ef-9330-7b03487ab792', 'NAO'),
  ('fd3f70f2-cb95-449e-91e8-00d2278a4cfa', 'cfdeee74-2d83-4d3e-b3d4-f660dcaa29ea', 'SIM');