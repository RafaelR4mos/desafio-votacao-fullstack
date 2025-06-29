--liquibase formatted sql
--changeset rafael.ramos:1 dbms:postgresql

--------- CREATE TABLES ------------

-- Tabela: agenda
CREATE TABLE agenda (
  id_agenda SERIAL PRIMARY KEY,
  title VARCHAR(150) NOT NULL,
  slug VARCHAR(200) NOT NULL UNIQUE,
  description VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ
);

-- Tabela: associate
CREATE TABLE associate (
  id_associate VARCHAR(40) PRIMARY KEY,
  first_name VARCHAR(60) NOT NULL,
  last_name VARCHAR(60) NOT NULL,
  cpf VARCHAR(14) NOT NULL UNIQUE,
  email VARCHAR(200) NOT NULL UNIQUE,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ
);

-- Tabela: session
CREATE TABLE session (
  id_session VARCHAR(40) PRIMARY KEY,
  id_agenda INTEGER NOT NULL REFERENCES agenda(id_agenda),
  status VARCHAR(30) NOT NULL DEFAULT 'draft',
  total_associates INTEGER,
  duration_seconds INTEGER NOT NULL DEFAULT 60,
  started_at TIMESTAMPTZ,
  finished_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela: votes
CREATE TABLE votes (
  id_session VARCHAR(40) NOT NULL,
  id_associate VARCHAR(40) NOT NULL,
  vote VARCHAR(20) NOT NULL,
  voted_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_session, id_associate),
  FOREIGN KEY (id_session) REFERENCES session(id_session),
  FOREIGN KEY (id_associate) REFERENCES associate(id_associate)
);