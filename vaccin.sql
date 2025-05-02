--
-- File generated with SQLiteStudio v3.4.17 on jeu. mai 1 21:17:08 2025
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: vaccin
CREATE TABLE IF NOT EXISTS vaccin (vaccin_id NUMBER(19) NOT NULL, created_on TIMESTAMP, nbre_months_dose NUMBER(10), num_dose NUMBER(10), vaccin_type VARCHAR, updated_on TIMESTAMP, vaccin_description VARCHAR, PRIMARY KEY (vaccin_id));
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (2, 1745945986976, 0, 1, 'RSV', NULL, ' RSV (virus respiratoire syncytial) : administration d’un traitementpréventif (produit d’immunisation passive) qui protège contre labronchiolite, de préférence avant la sortie de la maternité, en pé-riode de haute circulation du virus, de septembre à février. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (3, 1745945987015, 6, 2, 'RSV', NULL, ' RSV : pour les nourrissons de moins de 6 mois ne l’ayant pasreçu avant la sortie de la maternité, administration du traitementpréventif (produit d’immunisation passive) qui protège contre labronchiolite, en période de haute circulation du virus, de sep-tembre à février. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (4, 1745945987028, 2, 1, 'COMBINE', NULL, ' 1ère dose du vaccin combiné (D, T, aP, Hib, IPV, Hep B) quiprotège contre :- la diphtérie,- le tétanos,- la coqueluche,- les infections invasives à Haemophilus In?uenzae de type b(méningite, épiglottite et arthrite),- la poliomyélite,- l’hépatite B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (5, 1745945987039, 4, 2, 'COMBINE', NULL, ' 2ème dose du vaccin combiné (D, T, aP, Hib, IPV, Hep B) quiprotège contre :- la diphtérie,- le tétanos,- la coqueluche,- les infections invasives à Haemophilus In?uenzae de type b(méningite, épiglottite et arthrite),- la poliomyélite,- l’hépatite B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (6, 1745945987049, 11, 3, 'COMBINE', NULL, ' 3ème dose du vaccin combiné (D, T, aP, Hib, IPV, Hep B) quiprotège contre :- la diphtérie,- le tétanos,- la coqueluche,- les infections invasives à Haemophilus In?uenzae de type b(méningite, épiglottite et arthrite),- la poliomyélite,- l’hépatite B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (7, 1745945987057, 12, 1, 'COMBINE_RORV', NULL, ' 1ère dose du vaccin combiné (RORV) qui protège contre :- la rougeole,- les oreillons,- la rubéole,- la varicelle. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (8, 1745945987062, 2, 1, 'ROTAROVIRUS', NULL, ' Rotavirus (1ère dose) : Vaccination contre la gastro-entérite à ro-tavirus. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (9, 1745945987074, 3, 2, 'ROTAROVIRUS', NULL, ' Rotavirus (2 ième dose) : Vaccination contre la gastro-entérite à ro-tavirus. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (10, 1745945987085, 4, 3, 'ROTAROVIRUS', NULL, ' Rotavirus (3 ième dose) : Vaccination contre la gastro-entérite à ro-tavirus. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (11, 1745945987094, 2, 1, 'PNEUMONOCOQUES', NULL, ' Pneumocoques (1ère dose) : vaccination contre les infections in-vasives à pneumocoques. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (12, 1745945987097, 4, 2, 'PNEUMONOCOQUES', NULL, ' Pneumocoques (2ième dose) : vaccination contre les infections in-vasives à pneumocoques. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (13, 1745945987112, 11, 3, 'PNEUMONOCOQUES', NULL, ' Pneumocoques (3ième dose) : vaccination contre les infections in-vasives à pneumocoques. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (14, 1745945987119, 3, 1, 'MENINGOCOQUE_B', NULL, ' Méningocoque B (1 ère dose) : vaccination contre les infections invasives à méningocoque B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (15, 1745945987131, 5, 2, 'MENINGOCOQUE_B', NULL, ' Méningocoque B (2 ième dose) : vaccination contre les infections invasives à méningocoque B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (16, 1745945987139, 12, 3, 'MENINGOCOQUE_B', NULL, ' Méningocoque B (3 ième dose) : vaccination contre les infections invasives à méningocoque B. ');
INSERT INTO vaccin (vaccin_id, created_on, nbre_months_dose, num_dose, vaccin_type, updated_on, vaccin_description) VALUES (17, 1745945987149, 13, 1, 'MENINGOCOQUE_ACWY', NULL, ' Méningocoques ACWY (1ère dose) : vaccination contre les infections invasives à méningocoques A, C, W et Y. ');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
