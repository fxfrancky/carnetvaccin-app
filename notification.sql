--
-- File generated with SQLiteStudio v3.4.17 on sam. mai 3 09:18:19 2025
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: notification
CREATE TABLE IF NOT EXISTS "notification" (
	notification_id NUMBER(19) NOT NULL,
	created_on TIMESTAMP,
	date_notification DATE,
	message VARCHAR,
	updated_on TIMESTAMP,
	utilisateur_id NUMBER(19),
	vaccin_id NUMBER(19),
	PRIMARY KEY (notification_id),
	FOREIGN KEY (vaccin_id) REFERENCES vaccin(vaccin_id) ON DELETE CASCADE
);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
