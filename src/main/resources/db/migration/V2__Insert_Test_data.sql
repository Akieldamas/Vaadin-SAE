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
--  TYPES DOCUMENTS
-- ------------------------------------------------------------
INSERT INTO type_document (id, nom) VALUES
    ('1', 'Livre'),
    ('2', 'Film'),
    ('3', 'Magasine'),
    ('4', 'Journaux'),
    ('5', 'Album'),
    ('6', 'Bande dessinée'),
    ('7', 'Jeu vidéo'),
    ('8', 'Série TV'),
    ('9', 'Jeu de société');

-- ------------------------------------------------------------
--  BIBLIOTEQUES
-- ------------------------------------------------------------
INSERT INTO bibliotheque (id, nom, adresse, horaire_ouverture, horaire_fermeture) VALUES
    ('1', 'Médiathèque La Turbine', 'La Turbine Place Chorus  Cran-Gevrier', '8:00', '18:00'),
    ('2', 'Médiathèque Bonlieu', '1 Rue Jean Jaurès', '9:00', '19:00');

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
    (3,  'Le-Guin',      'Ursula-K.',    'Américaine',   '1929-10-21', '2018-01-22', 'Berkeley',        'https://fr.wikipedia.org/wiki/Ursula_K._Le_Guin'),
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
INSERT INTO document (
    id, titre, description, lien_gif, code_emplacement, code_isbn, 
    code_emprunt, specificite, date_acquisition, date_publication, 
    created_at, updated_at, auteur_id, format_id, editeur_id, type_id, bibliotheque_id) VALUES
-- === LIVRES (type_id = 1) ===
    (1, '1984', '328 pages. Roman dystopique dans lequel Big Brother surveille tout.', 'https://cdn.elidia.io/.uploads/.img/FIC171037.jpg', 'A1-001', '9782070368228', 'NON', 'MAUVAIS_ETAT', '2018-03-10', '1949-06-08', '2018-03-10 09:00:00', '2024-01-15 10:00:00', 1, 1, 3, 1, 1),
    (2, 'La Ferme des animaux', '144 pages. Fable politique allégorique sur le totalitarisme.', 'https://cdn.cultura.com/cdn-cgi/image/width=830/media/pim/TITELIVE/10_9782081521490_1_75.jpg', 'A1-002', '9782070360024', 'OUI', '', '2018-03-10', '1945-08-17', '2018-03-10 09:05:00', '2024-01-15 10:00:00', 1, 1, 3, 1, 1),
    (3, 'L''Étranger', '186 pages. Meursault, un homme indifférent au monde, commet un meurtre absurde.', 'https://static.fnac-static.com/multimedia/PE/Images/FR/NR/1c/f9/01/129308/1540-1/tsp20260316010829/L-Etranger.jpg', 'A1-003', '9782070360024', 'OUI', '', '2019-05-20', '1942-01-01', '2019-05-20 14:00:00', '2024-02-01 08:00:00', 2, 1, 1, 1, 1),
    (4, 'La Peste', '348 pages. Une épidémie de peste ravage la ville d''Oran.', 'https://static.fnac-static.com/multimedia/Images/FR/NR/2e/f9/01/129326/1540-0/tsp20191030071015/La-Peste.jpg', 'A1-004', '9782070360499', 'OUI', '', '2019-05-20', '1947-01-01', '2019-05-20 14:10:00', '2024-02-01 08:00:00', 2, 2, 1, 1, 1),
    (5, 'La Main gauche de la nuit', '352 pages. Envoyé diplomatique sur une planète où le genre n''existe pas.', 'https://m.media-amazon.com/images/I/81UykS68ujL.jpg', 'A1-005', '9782072761164', 'OUI', '', '2020-09-01', '1969-01-01', '2020-09-01 10:00:00', '2024-03-10 09:00:00', 3, 1, 1, 1, 2),
    (6, 'Ça', '1376 pages. Sept enfants affrontent une entité maléfique dans les égouts de Derry.', 'https://m.media-amazon.com/images/I/81bKNQ1ZZKL._AC_UF1000,1000_QL80_.jpg', 'A1-006', '9782253151968', 'OUI', '', '2021-11-05', '1986-09-15', '2021-11-05 11:00:00', '2024-03-10 09:00:00', 4, 2, 3, 1, 2),
    (7, 'Le Seigneur des Anneaux : La Communauté de l''Anneau', '528 pages. Frodon Sacquet hérite d''un anneau au pouvoir redoutable.', 'https://m.media-amazon.com/images/I/71p0c1Wgn7L._AC_UF1000,1000_QL80_.jpg', 'A1-007', '9782267011258', 'OUI', '', '2017-01-15', '1954-07-29', '2017-01-15 09:00:00', '2024-04-01 08:00:00', 11, 2, 2, 1, 2),
    (8, 'L''Amant', '142 pages. Relation amoureuse et interdite dans le Vietnam colonial.', 'https://m.media-amazon.com/images/I/715j8waVzVL._AC_UF1000,1000_QL80_.jpg', 'A1-008', '9782707301000', 'OUI', '', '2022-06-10', '1984-01-01', '2022-06-10 10:00:00', '2024-04-01 08:00:00', 10, 1, 2, 1, 2),

-- === FILMS / VIDÉOS (type_id = 2) ===
    (13, 'Inception', '148 min. Un voleur s''infiltre dans les rêves pour y implanter une idée.', 'https://m.media-amazon.com/images/M/MV5BZjhkNjM0ZTMtNGM5MC00ZTQ3LTk3YmYtZTkzYzdiNWE0ZTA2XkEyXkFqcGc@._V1_.jpg', 'C3-001', NULL, 'OUI', '', '2020-11-20', '2010-07-16', '2020-11-20 14:00:00', '2024-03-01 08:00:00', 5, 4, 5, 2, 1),
    (14, 'E.T. l''extra-terrestre', '115 min. Un enfant se lie d''amitié avec un extraterrestre perdu sur Terre.', 'https://cinema-tarasconsurariege.fr/wp-content/uploads/2025/08/extra-terrestre.jpg', 'C3-002', NULL, 'OUI', '', '2018-08-01', '1982-06-11', '2018-08-01 09:00:00', '2024-03-01 08:00:00', 6, 4, 6, 2, 1),
    (15, 'Le Voyage de Chihiro', '125 min. Une fillette doit travailler dans des thermes pour esprits pour sauver ses parents.', 'https://m.media-amazon.com/images/M/MV5BOWNhM2Y3NmUtN2Y4Ni00YjA1LThjNGItYTlkNTU2MWFkMzNkXkEyXkFqcGc@._V1_.jpg', 'C3-003', NULL, 'OUI', '', '2021-04-05', '2001-07-20', '2021-04-05 10:00:00', '2024-03-20 09:00:00', 9, 4, 6, 2, 1),
    (16, 'Dune (2021)', '155 min. Adaptation du roman de Frank Herbert par Denis Villeneuve.', 'https://www.allocine.fr/film/fichefilm_gen_cfilm=133392.html', 'C3-004', NULL, 'OUI', '', '2022-01-15', '2021-09-15', '2022-01-15 10:00:00', '2024-03-20 09:00:00', 12, 4, 5, 2, 1),
    (17, 'Interstellar — Blu-ray', '169 min. Édition Blu-ray collector du film de Christopher Nolan.', 'https://m.media-amazon.com/images/I/91vIHsL-zjL._AC_UF1000,1000_QL80_.jpg', 'D4-001', NULL, 'NON', 'VALEUR', '2022-05-10', '2014-11-07', '2022-05-10 11:00:00', '2024-04-10 09:00:00', 5, 5, 5, 2, 2),
    (18, 'Le Château Ambulant — Blu-ray', '119 min. Adaptation du roman de Diana Wynne Jones par Miyazaki.', 'https://images.justwatch.com/poster/185096351/s718/le-chateau-ambulant.jpg', 'D4-002', NULL, 'OUI', '', '2023-02-20', '2004-11-20', '2023-02-20 10:00:00', '2024-04-10 09:00:00', 9, 5, 6, 2, 2),
    (19, 'Shining — Blu-ray', '146 min. Adaptation du roman de Stephen King par Stanley Kubrick.', 'https://m.media-amazon.com/images/I/A1cWZr+V3cL._AC_UF894,1000_QL80_.jpg', 'D4-003', NULL, 'OUI', '', '2020-10-31', '1980-05-23', '2020-10-31 13:00:00', '2024-04-10 09:00:00', 4, 5, 5, 2, 2),
    (26, 'Tenet', '150 min. Un agent secret utilise l''inversion du temps pour empêcher la Troisième Guerre mondiale.', 'https://m.media-amazon.com/images/M/MV5BYjI0NDQzYmEtNzMwZC00ODA3LTgzZDYtZTk5ODZjY2Y2OTkzXkEyXkFqcGc@._V1_.jpg', 'C3-005', NULL, 'OUI', '', '2021-01-15', '2020-08-26', '2021-01-15 10:00:00', '2024-01-15 10:00:00', 5, 4, 5, 2, 2),
    (27, 'The Batman', '176 min. Le Chevalier Noir enquête sur une série de crimes commis par le Sphinx à Gotham City.', 'https://www.lescomics.fr/wp-content/uploads/2022/03/the-batman-2022-i120267.jpg', 'C3-006', NULL, 'OUI', '', '2022-07-20', '2022-03-02', '2022-07-20 11:00:00', '2024-01-15 10:00:00', 5, 4, 5, 2, 2),

-- === MAGASINES (type_id = 3) ===
    (20, 'National Geographic N°250', '144 pages. Magazine d''exploration, de science et de photographie historique.', 'https://lebouquinfrancais.fr/product_images/9999/51056026.webp', 'E5-001', NULL, 'OUI', '', '2024-01-10', '2023-12-01', '2024-01-10 08:00:00', '2024-01-10 08:00:00', 1, 1, 1, 3, 1),

-- === JOURNAUX (type_id = 4) ===
    (21, 'Le Monde - Édition Spéciale', '32 pages. Journal quotidien d''actualités nationales et internationales.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3O2viTlT1dd5fjr0f9O5eZEgyybsBCiMLIZTGuaZhRV33skXyQX459ZI', 'F6-001', NULL, 'NON', 'FRAGILITE', '2024-02-01', '2024-02-01', '2024-02-01 07:00:00', '2024-02-01 07:00:00', 2, 1, 2, 4, 2),

-- === ALBUMS / MUSIQUE (type_id = 5) ===
    (9, 'Songs of Leonard Cohen', '41 min (10 pistes). Premier album studio de Leonard Cohen, folk introspectif.', 'https://m.media-amazon.com/images/I/61vq7Y0LClL._UF894,1000_QL80_.jpg', 'B2-001', NULL, 'OUI', '', '2019-02-14', '1967-12-27', '2019-02-14 15:00:00', '2024-01-20 10:00:00', 8, 3, 4, 5, 1),
    (10, 'I''m Your Man', '40 min (8 pistes). Album synthpop et cabaret, synthèse de l''œuvre de Cohen.', 'https://m.media-amazon.com/images/I/611K+pBfrkL.jpg', 'B2-002', NULL, 'OUI', '', '2019-02-14', '1988-02-01', '2019-02-14 15:10:00', '2024-01-20 10:00:00', 8, 3, 4, 5, 1),
    (11, 'Interstellar — Bande originale', '71 min (16 pistes). Composition orchestrale et orgue pour le film de Christopher Nolan.', 'https://cdn-images.dzcdn.net/images/cover/4193cfd449576b01d33adeb54c2589ce/0x1900-000000-80-0-0.jpg', 'B2-003', NULL, 'OUI', '', '2021-03-20', '2014-11-17', '2021-03-20 09:30:00', '2024-02-15 08:00:00', 7, 3, 4, 5, 2),
    (12, 'Dune — Bande originale (2021)', '74 min (9 pistes). Bande originale de Denis Villeneuve, composition de Hans Zimmer.', 'https://m.media-amazon.com/images/I/81XLwfmntwL._UF894,1000_QL80_.jpg', 'B2-004', NULL, 'OUI', '', '2022-01-10', '2021-09-03', '2022-01-10 10:00:00', '2024-02-15 08:00:00', 7, 3, 4, 5, 2),

-- === BANDES DESSINÉES (type_id = 6) ===
    (22, 'Astérix le Gaulois', '48 pages. Première aventure mythique d''Astérix et Obélix contre les Romains.', 'https://cdn.cultura.com/cdn-cgi/image/width=830/media/pim/TITELIVE/17_9782012101333_1_75.jpg', 'G7-001', '9782012101333', 'OUI', '', '2022-03-15', '1961-10-01', '2022-03-15 10:00:00', '2024-01-15 10:00:00', 4, 2, 3, 6, 1),

-- === JEUX VIDÉO (type_id = 7) ===
    (23, 'The Legend of Zelda: Breath of the Wild', 'Jeu Console (Switch/WiiU). Jeu vidéo d''action-aventure en monde ouvert.', 'https://static.posters.cz/image/1300/toiles-the-legend-of-zelda-breath-of-the-wild-sunset-i111061.jpg', 'H8-001', NULL, 'OUI', '', '2021-06-20', '2017-03-03', '2021-06-20 14:00:00', '2024-02-01 09:00:00', 5, 3, 5, 7, 2),

-- === SÉRIES TV (type_id = 8) ===
    (24, 'Breaking Bad - Intégrale Saison 1', '340 min (7 épisodes). Série télévisée dramatique suivant l''évolution de Walter White.', 'https://static.fnac-static.com/multimedia/FR/images_produits/FR/Fnac.com/ZoomPE/6/3/7/3333297600736/tsp20130903125224/Coffret-integral-de-la-Saison-1.jpg', 'I9-001', NULL, 'OUI', '', '2020-05-12', '2008-01-20', '2020-05-12 11:00:00', '2024-03-01 10:00:00', 6, 4, 6, 8, 1),

-- === JEUX DE SOCIÉTÉ (type_id = 9) ===
    (25, 'Catan', 'Partie de 60-90 min (3-4 joueurs). Jeu de plateau, de stratégie et de négociation de ressources.', 'https://lagranderecre-lagranderecre-fr-storage.omn.proximis.com/Imagestorage/imagesSynchro/0/0/9d5db7bbfc6e425ec1ffbd294c22ea8fd60566c9_IMG-PRODUCT-206065-6.jpeg', 'J10-001', NULL, 'OUI', '', '2023-11-18', '1995-01-01', '2023-11-18 15:00:00', '2023-11-18 15:00:00', 11, 2, 2, 9, 2);

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
INSERT INTO utilisateur (
    id, login, mot_de_passe, prenom, nom, adresse, email, 
    date_naissance, date_fin_abonnement, numero_carte, 
    max_emprunts, duree_emprunt_semaines, role_utilisateur_id
) VALUES
-- === BIBLIOTHÉCAIRES (role_utilisateur_id = 1) ===
(1, 'mdurand',  '1985-04-12', 'Marie',    'Durand',    '3 rue des Lilas, 74000 Annecy',       'marie.durand@biblio-annecy.fr',   '1985-04-12', NULL,         NULL,         10, 5, 1),
(2, 'jpires',   '1978-11-30', 'João',     'Pires',     '12 avenue de Genève, 74000 Annecy',   'joao.pires@biblio-annecy.fr',     '1978-11-30', NULL,         NULL,         10, 5, 1),

-- === EMPRUNTEURS (role_utilisateur_id = 2) ===
(3, 'amoreau',  '1995-07-22', 'Alice',    'Moreau',    '7 chemin du Lac, 74000 Annecy',       'alice.moreau@email.com',          '1995-07-22', '2026-12-31', 'EMP0000001', 10, 5, 2),
(4, 'lblanc',   '2001-02-14', 'Lucas',    'Blanc',     '45 bd du Fier, 74000 Annecy',         'lucas.blanc@email.com',           '2001-02-14', '2026-06-30', 'EMP0000002', 10, 5, 2),
(5, 'skowalski','1990-09-03', 'Sofia',    'Kowalski',  '2 impasse des Alpes, 74600 Seynod',   'sofia.kowalski@email.com',        '1990-09-03', '2026-03-31', 'EMP0000003', 10, 5, 2),
(6, 'tnguyenvan','1988-05-17', 'Théo',     'Nguyen Van','88 rue Royale, 74000 Annecy',         'theo.nguyen@email.com',           '1988-05-17', '2026-01-15', 'EMP0000004', 10, 5, 2),
(7, 'cmartinez', '1999-12-01', 'Camille',  'Martinez',  '15 rue du Pâquier, 74000 Annecy',     'camille.martinez@email.com',      '1999-12-01', '2026-01-12', 'EMP0000005', 10, 5, 2),
(8, 'rtremblay', '1975-08-25', 'Romain',   'Tremblay',  '6 allée des Sapins, 74370 Metz-Tessy','romain.tremblay@email.com',       '1975-08-25', '2026-11-30', 'EMP0000006', 10, 5, 2),
(9, 'admin',   '$2a$10$Yr3lcHklyOVAFceTDGxM6udhMTRTI0QtDgMw.iXt4c97k3dwWLuw6', 'admin',     'admin',     '',   'admin@biblio-annecy.fr',     '1978-11-30', NULL,         NULL,         10, 5, 1),
(10, 'user',   '$2a$10$DiUsurU8Tf1/bWiogvXpGevXryAeJjLpa5i3/a66xsWz.kv8122wi', 'user',     'user',     '',   'user@biblio-annecy.fr',     '1978-11-30', NULL,         NULL,         10, 5, 1);

-- ------------------------------------------------------------
--  EMPRUNTS
--  La PK est (utilisateur_id, document_id) → un utilisateur
--  ne peut pas avoir deux emprunts actifs sur le même doc.
--  Quelques emprunts terminés, quelques en cours, un prolongé.
-- ------------------------------------------------------------
INSERT INTO emprunt (utilisateur_id, document_id, debut_emprunt, date_fin_prevue, date_rendu, prolongation) VALUES
    -- Alice : emprunts terminés (date_rendu présente, pas prolongation)
    (3, 1,  '2024-01-10', '2024-01-24', '2024-01-24', FALSE), -- 1984 (rendu)
    (3, 9,  '2024-02-01', '2024-02-15', '2024-02-14', FALSE), -- Songs of Cohen (rendu)

    -- Alice : emprunt en cours (date_rendu est NULL)
    (3, 13, '2024-04-20', '2024-05-04', NULL,         FALSE), -- Inception DVD (en cours)

    -- Lucas : emprunts terminés + un prolongé
    (4, 3,  '2024-02-10', '2024-02-24', '2024-02-24', FALSE), -- L'Étranger (rendu)
    (4, 7,  '2024-03-01', '2024-04-12', '2024-04-12', TRUE), -- SDA prolongé (rendu, la date finale inclut la prolongation)

    -- Lucas : emprunt en cours
    (4, 17, '2024-04-25', '2024-05-09', NULL,         FALSE), -- Interstellar Blu-ray (en cours)

    -- Sofia : emprunts variés
    (5, 4,  '2024-01-20', '2024-02-03', '2024-02-02', FALSE), -- La Peste (rendu)
    (5, 15, '2024-03-15', '2024-03-29', '2024-03-29', FALSE), -- Chihiro DVD (rendu)
    (5, 11, '2024-04-28', '2024-05-12', NULL,         FALSE), -- BO Interstellar CD (en cours)

    -- Théo
    (6, 6,  '2024-02-20', '2024-03-06', '2024-03-05', FALSE), -- Ça (rendu)
    (6, 14, '2024-04-01', '2024-04-15', '2024-04-15', FALSE), -- E.T. DVD (rendu)
    (6, 18, '2024-04-30', '2024-05-14', NULL,         FALSE), -- Château ambulant BR (en cours)

    -- Camille
    (7, 5,  '2024-03-10', '2024-03-24', '2024-03-24', FALSE), -- Main gauche nuit (rendu)
    (7, 12, '2024-03-25', '2024-04-08', '2024-04-07', FALSE), -- BO Dune CD (rendu)
    (7, 16, '2024-05-01', '2024-05-15', NULL,         FALSE), -- Dune DVD (en cours)

    -- Romain
    (8, 8,  '2024-02-05', '2024-02-19', '2024-02-19', FALSE), -- L'Amant (rendu)
    (8, 10, '2024-03-01', '2024-04-05', '2024-04-05', TRUE), -- I'm Your Man prolongé (rendu)
    (8, 19, '2024-04-15', '2024-04-29', '2024-04-28', FALSE); -- Shining BR (rendu)

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
SELECT setval('type_document_id_seq', max(id)) FROM type_document;
SELECT setval('bibliotheque_id_seq', max(id)) FROM bibliotheque;