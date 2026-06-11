-- ============================================================
--  SEED — Jeu de données bibliothèque
--  Ordre d'insertion respectant les contraintes FK
-- ============================================================


-- ------------------------------------------------------------
--  ROLES
-- ------------------------------------------------------------
INSERT INTO role_utilisateur (id, libelle) VALUES
    (1, 'Bibliothécaire'),
    (2, 'Emprunteur');


-- ------------------------------------------------------------
--  TYPES D'AUTEURS
-- ------------------------------------------------------------
INSERT INTO type_auteur (id, label) VALUES
    (1, 'Écrivain'),
    (2, 'Réalisateur'),
    (3, 'Compositeur'),
    (4, 'Scénariste'),
    (5, 'Illustrateur');


-- ------------------------------------------------------------
--  FORMATS  (longueur x largeur en cm, poids en g)
--  Livre poche, Livre grand format, CD audio, DVD, Blu-ray
-- ------------------------------------------------------------
INSERT INTO format (id, longueur, largeur, poids) VALUES
    (1, 17.5, 10.8, 180),   -- Livre poche
    (2, 24.0, 16.0, 420),   -- Livre grand format
    (3, 14.2, 12.5,  80),   -- CD audio
    (4, 19.0, 13.5, 100),   -- DVD
    (5, 17.2, 13.5, 120);   -- Blu-ray


-- ------------------------------------------------------------
--  GENRES
-- ------------------------------------------------------------
INSERT INTO genre_document (id, nom) VALUES
    (1,  'Roman'),
    (2,  'Science-fiction'),
    (3,  'Thriller'),
    (4,  'Fantasy'),
    (5,  'Documentaire'),
    (6,  'Biographie'),
    (7,  'Musique classique'),
    (8,  'Rock / Pop'),
    (9,  'Bande originale'),
    (10, 'Drame'),
    (11, 'Action / Aventure'),
    (12, 'Horreur');


-- ------------------------------------------------------------
--  ÉDITEURS / LABELS / STUDIOS
-- ------------------------------------------------------------
INSERT INTO editeur (id, nom, adresse, lien_site_web, lien_wikipedia) VALUES
    (1, 'Gallimard',         '5 rue Gaston-Gallimard, 75007 Paris', 'https://www.gallimard.fr',       'https://fr.wikipedia.org/wiki/Gallimard'),
    (2, 'Le Seuil',          '25 bd Romain-Rolland, 75014 Paris',   'https://www.seuil.com',           'https://fr.wikipedia.org/wiki/Éditions_du_Seuil'),
    (3, 'J''ai lu',          '87 quai Panhard, 75013 Paris',        'https://www.jailu.com',           'https://fr.wikipedia.org/wiki/J%27ai_lu'),
    (4, 'Universal Music',   '20 rue des Fossés-Saint-Jacques, 75005 Paris', 'https://www.universalmusic.fr', 'https://fr.wikipedia.org/wiki/Universal_Music_Group'),
    (5, 'Warner Bros.',      '4000 Warner Blvd, Burbank CA',        'https://www.warnerbros.com',      'https://fr.wikipedia.org/wiki/Warner_Bros.'),
    (6, 'Sony Pictures',     '10202 W Washington Blvd, Culver City', 'https://www.sonypictures.com',   'https://fr.wikipedia.org/wiki/Sony_Pictures'),
    (7, 'Actes Sud',         'Le Méjan, Place Nina-Berberova, 13200 Arles', 'https://www.actes-sud.fr', 'https://fr.wikipedia.org/wiki/Actes_Sud');


-- ------------------------------------------------------------
--  AUTEURS
-- ------------------------------------------------------------
INSERT INTO auteur (id, nom, prenom, nationalite, date_naissance, date_deces, ville_naissance, lien_wikipedia) VALUES
    (1,  'Orwell',       'George',       'Britannique',  '1903-06-25', '1950-01-21', 'Motihari',        'https://fr.wikipedia.org/wiki/George_Orwell'),
    (2,  'Camus',        'Albert',       'Française',    '1913-11-07', '1960-01-04', 'Dréan',           'https://fr.wikipedia.org/wiki/Albert_Camus'),
    (3,  'Le Guin',      'Ursula K.',    'Américaine',   '1929-10-21', '2018-01-22', 'Berkeley',        'https://fr.wikipedia.org/wiki/Ursula_K._Le_Guin'),
    (4,  'King',         'Stephen',      'Américaine',   '1947-09-21', NULL,         'Portland',        'https://fr.wikipedia.org/wiki/Stephen_King'),
    (5,  'Nolan',        'Christopher', 'Britannique',  '1970-07-30', NULL,         'Londres',         'https://fr.wikipedia.org/wiki/Christopher_Nolan'),
    (6,  'Spielberg',    'Steven',       'Américaine',   '1946-12-18', NULL,         'Cincinnati',      'https://fr.wikipedia.org/wiki/Steven_Spielberg'),
    (7,  'Zimmer',       'Hans',         'Allemande',    '1957-09-12', NULL,         'Francfort',       'https://fr.wikipedia.org/wiki/Hans_Zimmer'),
    (8,  'Cohen',        'Leonard',      'Canadienne',   '1934-09-21', '2016-11-07', 'Montréal',        'https://fr.wikipedia.org/wiki/Leonard_Cohen'),
    (9,  'Miyazaki',     'Hayao',        'Japonaise',    '1941-01-05', NULL,         'Tokyo',           'https://fr.wikipedia.org/wiki/Hayao_Miyazaki'),
    (10, 'Duras',        'Marguerite',   'Française',    '1914-04-04', '1996-03-03', 'Gia Dinh',        'https://fr.wikipedia.org/wiki/Marguerite_Duras'),
    (11, 'Tolkien',      'J.R.R.',       'Britannique',  '1892-01-03', '1973-09-02', 'Bloemfontein',    'https://fr.wikipedia.org/wiki/J._R._R._Tolkien'),
    (12, 'Villeneuve',   'Denis',        'Canadienne',   '1967-10-03', NULL,         'Gentilly',        'https://fr.wikipedia.org/wiki/Denis_Villeneuve');


-- ------------------------------------------------------------
--  ASSOCIATIONS AUTEUR <-> TYPE_AUTEUR
-- ------------------------------------------------------------
INSERT INTO auteur_type_auteur (auteur_id, type_auteur_id) VALUES
    (1,  1),  -- Orwell        → Écrivain
    (2,  1),  -- Camus         → Écrivain
    (3,  1),  -- Le Guin       → Écrivain
    (4,  1),  -- King          → Écrivain
    (4,  4),  -- King          → Scénariste
    (5,  2),  -- Nolan         → Réalisateur
    (5,  4),  -- Nolan         → Scénariste
    (6,  2),  -- Spielberg     → Réalisateur
    (7,  3),  -- Zimmer        → Compositeur
    (8,  3),  -- Cohen         → Compositeur
    (8,  1),  -- Cohen         → Écrivain (poète)
    (9,  2),  -- Miyazaki      → Réalisateur
    (9,  4),  -- Miyazaki      → Scénariste
    (10, 1),  -- Duras         → Écrivain
    (11, 1),  -- Tolkien       → Écrivain
    (11, 5),  -- Tolkien       → Illustrateur
    (12, 2),  -- Villeneuve    → Réalisateur
    (12, 4);  -- Villeneuve    → Scénariste


-- ------------------------------------------------------------
--  DOCUMENTS
--  specificite : 'livre', 'cd', 'dvd', 'blu-ray'
-- ------------------------------------------------------------
INSERT INTO document (id, titre, description, nb_pages, code_emplacement, code_isbn, code_emprunt, specificite, date_acquisition, date_publication, created_at, updated_at, auteur_id, format_id, editeur_id) VALUES

-- === LIVRES ===
(1,  '1984',
     'Roman dystopique dans lequel Big Brother surveille tout.',
     328, 'A1-001', '9782070368228', 'EMP-0001', 'livre', '2018-03-10', '1949-06-08', '2018-03-10 09:00:00', '2024-01-15 10:00:00', 1, 1, 3),

(2,  'La Ferme des animaux',
     'Fable politique allégorique sur le totalitarisme.',
     144, 'A1-002', '9782070360024', 'EMP-0002', 'livre', '2018-03-10', '1945-08-17', '2018-03-10 09:05:00', '2024-01-15 10:00:00', 1, 1, 3),

(3,  'L''Étranger',
     'Meursault, un homme indifférent au monde, commet un meurtre absurde.',
     186, 'A1-003', '9782070360024', 'EMP-0003', 'livre', '2019-05-20', '1942-01-01', '2019-05-20 14:00:00', '2024-02-01 08:00:00', 2, 1, 1),

(4,  'La Peste',
     'Une épidémie de peste ravage la ville d''Oran.',
     348, 'A1-004', '9782070360499', 'EMP-0004', 'livre', '2019-05-20', '1947-01-01', '2019-05-20 14:10:00', '2024-02-01 08:00:00', 2, 2, 1),

(5,  'La Main gauche de la nuit',
     'Envoyé diplomatique sur une planète où le genre n''existe pas.',
     352, 'A1-005', '9782072761164', 'EMP-0005', 'livre', '2020-09-01', '1969-01-01', '2020-09-01 10:00:00', '2024-03-10 09:00:00', 3, 1, 1),

(6,  'Ça',
     'Sept enfants affrontent une entité maléfique dans les égouts de Derry.',
     1376, 'A1-006', '9782253151968', 'EMP-0006', 'livre', '2021-11-05', '1986-09-15', '2021-11-05 11:00:00', '2024-03-10 09:00:00', 4, 2, 3),

(7,  'Le Seigneur des Anneaux : La Communauté de l''Anneau',
     'Frodon Sacquet hérite d''un anneau au pouvoir redoutable.',
     528, 'A1-007', '9782267011258', 'EMP-0007', 'livre', '2017-01-15', '1954-07-29', '2017-01-15 09:00:00', '2024-04-01 08:00:00', 11, 2, 2),

(8,  'L''Amant',
     'Relation amoureuse et interdite dans le Vietnam colonial.',
     142, 'A1-008', '9782707301000', 'EMP-0008', 'livre', '2022-06-10', '1984-01-01', '2022-06-10 10:00:00', '2024-04-01 08:00:00', 10, 1, 2),

-- === CD ===
(9,  'Songs of Leonard Cohen',
     'Premier album studio de Leonard Cohen, folk introspectif.',
     NULL, 'B2-001', NULL, 'EMP-0009', 'cd', '2019-02-14', '1967-12-27', '2019-02-14 15:00:00', '2024-01-20 10:00:00', 8, 3, 4),

(10, 'I''m Your Man',
     'Album synthpop et cabaret, synthèse de l''œuvre de Cohen.',
     NULL, 'B2-002', NULL, 'EMP-0010', 'cd', '2019-02-14', '1988-02-01', '2019-02-14 15:10:00', '2024-01-20 10:00:00', 8, 3, 4),

(11, 'Interstellar — Bande originale',
     'Composition orchestrale et orgue pour le film de Christopher Nolan.',
     NULL, 'B2-003', NULL, 'EMP-0011', 'cd', '2021-03-20', '2014-11-17', '2021-03-20 09:30:00', '2024-02-15 08:00:00', 7, 3, 4),

(12, 'Dune — Bande originale (2021)',
     'Bande originale de Denis Villeneuve, composition de Hans Zimmer.',
     NULL, 'B2-004', NULL, 'EMP-0012', 'cd', '2022-01-10', '2021-09-03', '2022-01-10 10:00:00', '2024-02-15 08:00:00', 7, 3, 4),

-- === DVD ===
(13, 'Inception',
     'Un voleur s''infiltre dans les rêves pour y implanter une idée.',
     NULL, 'C3-001', NULL, 'EMP-0013', 'dvd', '2020-11-20', '2010-07-16', '2020-11-20 14:00:00', '2024-03-01 08:00:00', 5, 4, 5),

(14, 'E.T. l''extra-terrestre',
     'Un enfant se lie d''amitié avec un extraterrestre perdu sur Terre.',
     NULL, 'C3-002', NULL, 'EMP-0014', 'dvd', '2018-08-01', '1982-06-11', '2018-08-01 09:00:00', '2024-03-01 08:00:00', 6, 4, 6),

(15, 'Le Voyage de Chihiro',
     'Une fillette doit travailler dans des thermes pour esprits pour sauver ses parents.',
     NULL, 'C3-003', NULL, 'EMP-0015', 'dvd', '2021-04-05', '2001-07-20', '2021-04-05 10:00:00', '2024-03-20 09:00:00', 9, 4, 6),

(16, 'Dune (2021)',
     'Adaptation du roman de Frank Herbert par Denis Villeneuve.',
     NULL, 'C3-004', NULL, 'EMP-0016', 'dvd', '2022-01-15', '2021-09-15', '2022-01-15 10:00:00', '2024-03-20 09:00:00', 12, 4, 5),

-- === BLU-RAY ===
(17, 'Interstellar — Blu-ray',
     'Édition Blu-ray collector du film de Christopher Nolan.',
     NULL, 'D4-001', NULL, 'EMP-0017', 'blu-ray', '2022-05-10', '2014-11-07', '2022-05-10 11:00:00', '2024-04-10 09:00:00', 5, 5, 5),

(18, 'Le Château Ambulant — Blu-ray',
     'Adaptation du roman de Diana Wynne Jones par Miyazaki.',
     NULL, 'D4-002', NULL, 'EMP-0018', 'blu-ray', '2023-02-20', '2004-11-20', '2023-02-20 10:00:00', '2024-04-10 09:00:00', 9, 5, 6),

(19, 'Shining — Blu-ray',
     'Adaptation du roman de Stephen King par Stanley Kubrick.',
     NULL, 'D4-003', NULL, 'EMP-0019', 'blu-ray', '2020-10-31', '1980-05-23', '2020-10-31 13:00:00', '2024-04-10 09:00:00', 4, 5, 5);


-- ------------------------------------------------------------
--  ASSOCIATIONS DOCUMENT <-> GENRE
-- ------------------------------------------------------------
INSERT INTO document_genre_document (document_id, genre_document_id) VALUES
    (1,  1),   -- 1984              → Roman
    (1,  2),   -- 1984              → Science-fiction
    (2,  1),   -- Ferme des animaux → Roman
    (3,  1),   -- L'Étranger        → Roman
    (4,  1),   -- La Peste          → Roman
    (5,  1),   -- Main gauche       → Roman
    (5,  2),   -- Main gauche       → Science-fiction
    (6,  1),   -- Ça                → Roman
    (6,  12),  -- Ça                → Horreur
    (7,  1),   -- SDA               → Roman
    (7,  4),   -- SDA               → Fantasy
    (8,  1),   -- L'Amant           → Roman
    (9,  8),   -- Songs of Cohen    → Rock/Pop
    (10, 8),   -- I'm Your Man      → Rock/Pop
    (11, 9),   -- BO Interstellar   → Bande originale
    (12, 9),   -- BO Dune           → Bande originale
    (13, 10),  -- Inception         → Drame
    (13, 2),   -- Inception         → Science-fiction
    (14, 11),  -- E.T.              → Action/Aventure
    (15, 10),  -- Chihiro           → Drame
    (15, 4),   -- Chihiro           → Fantasy
    (16, 2),   -- Dune DVD          → Science-fiction
    (16, 10),  -- Dune DVD          → Drame
    (17, 2),   -- Interstellar BR   → Science-fiction
    (17, 10),  -- Interstellar BR   → Drame
    (18, 4),   -- Château ambulant  → Fantasy
    (19, 12);  -- Shining BR        → Horreur


-- ------------------------------------------------------------
--  UTILISATEURS
--  Mots de passe : hash fictif (bcrypt placeholder)
-- ------------------------------------------------------------
INSERT INTO utilisateur (id, login, mot_de_passe, prenom, nom, adresse, email, date_naissance, date_fin_abonnement, numero_carte, role_utilisateur_id) VALUES

-- Bibliothécaires
(1, 'mdurand',  '$2b$12$aBiblio1HashFictif001', 'Marie',    'Durand',    '3 rue des Lilas, 74000 Annecy',       'marie.durand@biblio-annecy.fr',   '1985-04-12', NULL,         NULL, 1),
(2, 'jpires',   '$2b$12$aBiblio2HashFictif002', 'João',     'Pires',     '12 avenue de Genève, 74000 Annecy',   'joao.pires@biblio-annecy.fr',     '1978-11-30', NULL,         NULL, 1),

-- Emprunteurs
(3, 'amoreau',  '$2b$12$aUser001HashFictif003', 'Alice',    'Moreau',    '7 chemin du Lac, 74000 Annecy',       'alice.moreau@email.com',          '1995-07-22', '2025-12-31', 'EMP0000001', 2),
(4, 'lblanc',   '$2b$12$aUser002HashFictif004', 'Lucas',    'Blanc',     '45 bd du Fier, 74000 Annecy',         'lucas.blanc@email.com',           '2001-02-14', '2025-06-30', 'EMP0000002', 2),
(5, 'skowalski','$2b$12$aUser003HashFictif005', 'Sofia',    'Kowalski',  '2 impasse des Alpes, 74600 Seynod',   'sofia.kowalski@email.com',        '1990-09-03', '2026-03-31', 'EMP0000003', 2),
(6, 'tnguyenvan','$2b$12$aUser004HashFictif006','Théo',     'Nguyen Van','88 rue Royale, 74000 Annecy',         'theo.nguyen@email.com',           '1988-05-17', '2025-09-15', 'EMP0000004', 2),
(7, 'cmartinez', '$2b$12$aUser005HashFictif007','Camille',  'Martinez',  '15 rue du Pâquier, 74000 Annecy',     'camille.martinez@email.com',      '1999-12-01', '2026-01-15', 'EMP0000005', 2),
(8, 'rtremblay', '$2b$12$aUser006HashFictif008','Romain',   'Tremblay',  '6 allée des Sapins, 74370 Metz-Tessy','romain.tremblay@email.com',       '1975-08-25', '2025-11-30', 'EMP0000006', 2);


-- ------------------------------------------------------------
--  EMPRUNTS
--  La PK est (utilisateur_id, document_id) → un utilisateur
--  ne peut pas avoir deux emprunts actifs sur le même doc.
--  Quelques emprunts terminés, quelques en cours, un prolongé.
-- ------------------------------------------------------------
INSERT INTO emprunt (utilisateur_id, document_id, debut_emprunt, fin_emprunt, prolongation, fin_prolongation) VALUES

-- Alice : emprunts terminés
(3, 1,  '2024-01-10', '2024-01-24', FALSE, NULL),           -- 1984 (rendu)
(3, 9,  '2024-02-01', '2024-02-15', FALSE, NULL),           -- Songs of Cohen (rendu)

-- Alice : emprunt en cours
(3, 13, '2024-04-20', '2024-05-04', FALSE, NULL),           -- Inception DVD (en cours)

-- Lucas : emprunts terminés + prolongé
(4, 3,  '2024-02-10', '2024-02-24', FALSE, NULL),           -- L'Étranger (rendu)
(4, 7,  '2024-03-01', '2024-03-29', TRUE,  '2024-04-12'),   -- SDA prolongé (rendu)

-- Lucas : emprunt en cours
(4, 17, '2024-04-25', '2024-05-09', FALSE, NULL),           -- Interstellar Blu-ray (en cours)

-- Sofia : emprunts variés
(5, 4,  '2024-01-20', '2024-02-03', FALSE, NULL),           -- La Peste (rendu)
(5, 15, '2024-03-15', '2024-03-29', FALSE, NULL),           -- Chihiro DVD (rendu)
(5, 11, '2024-04-28', '2024-05-12', FALSE, NULL),           -- BO Interstellar CD (en cours)

-- Théo
(6, 6,  '2024-02-20', '2024-03-06', FALSE, NULL),           -- Ça (rendu)
(6, 14, '2024-04-01', '2024-04-15', FALSE, NULL),           -- E.T. DVD (rendu)
(6, 18, '2024-04-30', '2024-05-14', FALSE, NULL),           -- Château ambulant BR (en cours)

-- Camille
(7, 5,  '2024-03-10', '2024-03-24', FALSE, NULL),           -- Main gauche nuit (rendu)
(7, 12, '2024-03-25', '2024-04-08', FALSE, NULL),           -- BO Dune CD (rendu)
(7, 16, '2024-05-01', '2024-05-15', FALSE, NULL),           -- Dune DVD (en cours)

-- Romain
(8, 8,  '2024-02-05', '2024-02-19', FALSE, NULL),           -- L'Amant (rendu)
(8, 10, '2024-03-01', '2024-03-22', TRUE,  '2024-04-05'),   -- I'm Your Man prolongé (rendu)
(8, 19, '2024-04-15', '2024-04-29', FALSE, NULL);           -- Shining BR (rendu)

-- ============================================================
--  MISE À JOUR DES SÉQUENCES
--  Synchronisation de l'auto-incrémentation après les INSERTS
-- ============================================================

SELECT setval('role_utilisateur_id_seq', max(id)) FROM role_utilisateur;
SELECT setval('type_auteur_id_seq', max(id)) FROM type_auteur;
SELECT setval('format_id_seq', max(id)) FROM format;
SELECT setval('genre_document_id_seq', max(id)) FROM genre_document;
SELECT setval('editeur_id_seq', max(id)) FROM editeur;
SELECT setval('auteur_id_seq', max(id)) FROM auteur;
SELECT setval('document_id_seq', max(id)) FROM document;
SELECT setval('utilisateur_id_seq', max(id)) FROM utilisateur;