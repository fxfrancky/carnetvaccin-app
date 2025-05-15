-- File generated with SQLiteStudio v3.4.17 on jeu. mai 15 21:51:08 2025
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;


INSERT INTO utilisateur (
                            utilisateur_id,
                            created_on,
                            date_naiss,
                            email,
                            password,
                            first_name,
                            is_admin,
                            last_name,
                            roles,
                            token,
                            updated_on,
                            user_name,
                            is_active
                        )
                        VALUES (
                            388,
                            1746259559022,
                            '2025-04-30',
                            'fxowen@yahoo.com',
                            '$2a$10$XlRYZ/8NUPDrI6/0M2oybOEFtt8P8wwR5LEcgQiRPlSFVHV4ApqwC',
                            'Francois Xavier',
                            1,
                            'OWONA',
                            'User,Admin',
                            '4a12f6af-e685-40e8-bb6d-072d5a7b1d32',
                            1747338071425,
                            'fxowen',
                            1
                        );

INSERT INTO utilisateur (
                            utilisateur_id,
                            created_on,
                            date_naiss,
                            email,
                            password,
                            first_name,
                            is_admin,
                            last_name,
                            roles,
                            token,
                            updated_on,
                            user_name,
                            is_active
                        )
                        VALUES (
                            389,
                            1746280073671,
                            '2025-04-29',
                            'kevindur@gmail.com',
                            '$2a$10$c/SvwF69PxX9dHUUfIaQYer4N/yHlam0oBS22tHMhhCyhjNH5/sTm',
                            'Kevin',
                            0,
                            'Durant',
                            'User',
                            'ae32a498-cf68-4d0d-be90-f11bf7d598fa',
                            NULL,
                            'kevindur',
                            1
                        );

PRAGMA foreign_keys = on;
