--
-- File generated with SQLiteStudio v3.4.17 on jeu. mai 1 21:19:10 2025
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: utilisateur
CREATE TABLE IF NOT EXISTS utilisateur (utilisateur_id NUMBER(19) NOT NULL, created_on TIMESTAMP, date_naiss VARCHAR, email VARCHAR UNIQUE, password VARCHAR, first_name VARCHAR, is_admin NUMBER(1), last_name VARCHAR, token VARCHAR, updated_on TIMESTAMP, user_name VARCHAR UNIQUE, PRIMARY KEY (utilisateur_id));
INSERT INTO utilisateur (utilisateur_id, created_on, date_naiss, email, password, first_name, is_admin, last_name, token, updated_on, user_name) VALUES (1, 1745940638552, '2025-04-28', 'fxowen@yahoo.com', 'HXB4EZiAacp2CCaGHW1joQ6MO38XHERBpkcupYwRcRs=', 'Francois Xavier', 1, 'OWONA', 'eeedde98-f285-4c13-95b3-8f2380054856', 1746125740410, 'fxowene');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
