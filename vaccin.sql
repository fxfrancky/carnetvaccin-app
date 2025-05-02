--
-- File generated with SQLiteStudio v3.4.17 on jeu. mai 1 21:17:08 2025
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: vaccin
CREATE TABLE IF NOT EXISTS vaccin (vaccin_id NUMBER(19) NOT NULL, created_on TIMESTAMP, nbre_months_dose NUMBER(10), num_dose NUMBER(10), vaccin_type VARCHAR, updated_on TIMESTAMP, vaccin_description VARCHAR, PRIMARY KEY (vaccin_id));
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (2, 1745945986976, 0, 1, 'RSV', NULL, ' RSV (virus respiratoire syncytial) : administration d�un traitementpr�ventif (produit d�immunisation passive) qui prot�ge contre labronchiolite, de pr�f�rence avant la sortie de la maternit�, en p�-riode de haute circulation du virus, de septembre � f�vrier. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (3, 1745945987015, 6, 2, 'RSV', NULL, ' RSV : pour les nourrissons de moins de 6 mois ne l�ayant pasre�u avant la sortie de la maternit�, administration du traitementpr�ventif (produit d�immunisation passive) qui prot�ge contre labronchiolite, en p�riode de haute circulation du virus, de sep-tembre � f�vrier. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (4, 1745945987028, 2, 1, 'COMBINE', NULL, ' 1�re dose du vaccin combin� (D, T, aP, Hib, IPV, Hep B) quiprot�ge contre :- la dipht�rie,- le t�tanos,- la coqueluche,- les infections invasives � Haemophilus In?uenzae de type b(m�ningite, �piglottite et arthrite),- la poliomy�lite,- l�h�patite B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (5, 1745945987039, 4, 2, 'COMBINE', NULL, ' 2�me dose du vaccin combin� (D, T, aP, Hib, IPV, Hep B) quiprot�ge contre :- la dipht�rie,- le t�tanos,- la coqueluche,- les infections invasives � Haemophilus In?uenzae de type b(m�ningite, �piglottite et arthrite),- la poliomy�lite,- l�h�patite B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (6, 1745945987049, 11, 3, 'COMBINE', NULL, ' 3�me dose du vaccin combin� (D, T, aP, Hib, IPV, Hep B) quiprot�ge contre :- la dipht�rie,- le t�tanos,- la coqueluche,- les infections invasives � Haemophilus In?uenzae de type b(m�ningite, �piglottite et arthrite),- la poliomy�lite,- l�h�patite B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (7, 1745945987057, 12, 1, 'COMBINE_RORV', NULL, ' 1�re dose du vaccin combin� (RORV) qui prot�ge contre :- la rougeole,- les oreillons,- la rub�ole,- la varicelle. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (8, 1745945987062, 2, 1, 'ROTAROVIRUS', NULL, ' Rotavirus (1�re dose) : Vaccination contre la gastro-ent�rite � ro-tavirus. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (9, 1745945987074, 3, 2, 'ROTAROVIRUS', NULL, ' Rotavirus (2 i�me dose) : Vaccination contre la gastro-ent�rite � ro-tavirus. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (10, 1745945987085, 4, 3, 'ROTAROVIRUS', NULL, ' Rotavirus (3 i�me dose) : Vaccination contre la gastro-ent�rite � ro-tavirus. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (11, 1745945987094, 2, 1, 'PNEUMONOCOQUES', NULL, ' Pneumocoques (1�re dose) : vaccination contre les infections in-vasives � pneumocoques. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (12, 1745945987097, 4, 2, 'PNEUMONOCOQUES', NULL, ' Pneumocoques (2i�me dose) : vaccination contre les infections in-vasives � pneumocoques. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (13, 1745945987112, 11, 3, 'PNEUMONOCOQUES', NULL, ' Pneumocoques (3i�me dose) : vaccination contre les infections in-vasives � pneumocoques. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (14, 1745945987119, 3, 1, 'MENINGOCOQUE_B', NULL, ' M�ningocoque B (1 �re dose) : vaccination contre les infections invasives � m�ningocoque B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (15, 1745945987131, 5, 2, 'MENINGOCOQUE_B', NULL, ' M�ningocoque B (2 i�me dose) : vaccination contre les infections invasives � m�ningocoque B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (16, 1745945987139, 12, 3, 'MENINGOCOQUE_B', NULL, ' M�ningocoque B (3 i�me dose) : vaccination contre les infections invasives � m�ningocoque B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (17, 1745945987149, 13, 1, 'MENINGOCOQUE_ACWY', NULL, ' M�ningocoques ACWY (1�re dose) : vaccination contre les infections invasives � m�ningocoques A, C, W et Y. ');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
