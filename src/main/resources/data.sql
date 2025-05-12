ALTER TABLE films ALTER COLUMN film_id RESTART WITH 4;

-- Данные для таблицы users
INSERT INTO users (user_id, email, login, name, birthday) VALUES
(1, 'john.doe@example.com', 'johndoe', 'John Doe', '1990-01-15'),
(2, 'jane.smith@example.com', 'janesmith', 'Jane Smith', '1985-05-25'),
(3, 'alex.brown@example.com', 'alexbrown', 'Alex Brown', '1992-07-10');

-- Данные для таблицы genres
INSERT INTO genres (genre_id, name) VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

INSERT INTO mpa_ratings (mpa_id, name, description) VALUES
    (1, 'G', 'No age restrictions'),
    (2, 'PG', 'Parental guidance suggested'),
    (3, 'PG-13', 'Parents strongly cautioned'),
    (4, 'R', 'Restricted'),
    (5, 'NC-17', 'Adults only');

-- Данные для таблицы films
INSERT INTO films (film_id, name, description, release_date, durationInSeconds, mpa_id) VALUES
(1, 'Inception', 'A mind-bending thriller by Christopher Nolan', '2010-07-16', 8880, 1),
(2, 'The Matrix', 'A computer hacker learns about the true nature of reality', '1999-03-31', 7860, 2),
(3, 'Interstellar', 'A team of explorers travel through a wormhole in space', '2014-11-07', 10140, 3);

-- Данные для таблицы friendship
INSERT INTO friendship (user_id, friend_id, status) VALUES
(1, 2, 'confirmed'),
(2, 1, 'confirmed'),
(1, 3, 'pending'),
(3, 1, 'pending');

-- Данные для таблицы likes
INSERT INTO likes (user_id, film_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3),
(2, 1);

-- Данные для таблицы film_genres
INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 1), -- Inception - Action
(1, 3), -- Inception - Drama
(2, 1), -- The Matrix - Action
(2, 4), -- The Matrix - Science Fiction
(3, 4), -- Interstellar - Science Fiction
(3, 3); -- Interstellar - Drama

INSERT INTO film_mpa_ratings (film_id, mpa_id) VALUES
(1, 1);