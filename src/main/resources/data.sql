INSERT INTO mpa_ratings (mpa_id, name, description) VALUES
    (100, 'G', 'No age restrictions'),
    (200, 'PG', 'Parental guidance suggested'),
    (300, 'PG_13', 'Parents strongly cautioned'),
    (400, 'R', 'Restricted'),
    (500, 'NC_17', 'Adults only');

-- Данные для таблицы users
INSERT INTO users (user_id, email, login, name, birthday) VALUES
(100, 'john.doe@example.com', 'johndoe', 'John Doe', '1990-01-15'),
(200, 'jane.smith@example.com', 'janesmith', 'Jane Smith', '1985-05-25'),
(300, 'alex.brown@example.com', 'alexbrown', 'Alex Brown', '1992-07-10');

-- Данные для таблицы genres
INSERT INTO genres (genre_id, name) VALUES
(100, 'Action'),
(200, 'Comedy'),
(300, 'Drama'),
(400, 'Science Fiction'),
(500, 'Horror');

-- Данные для таблицы films
INSERT INTO films (film_id, name, description, release_date, durationInSeconds, mpa_rating) VALUES
(100, 'Inception', 'A mind-bending thriller by Christopher Nolan', '2010-07-16', 8880, 'PG-13'),
(200, 'The Matrix', 'A sci-fi classic', '1999-03-31', 8160, 'R'),
(300, 'Interstellar', 'Journey beyond the stars', '2014-11-07', 10140, 'PG-13');

-- Данные для таблицы friendship
INSERT INTO friendship (user_id, friend_id, status) VALUES
(100, 200, 'confirmed'),
(200, 100, 'confirmed'),
(100, 300, 'pending'),
(300, 100, 'pending');

-- Данные для таблицы likes
INSERT INTO likes (user_id, film_id) VALUES
(100, 100),
(100, 200),
(200, 200),
(300, 300),
(200, 100);

-- Данные для таблицы film_genres
INSERT INTO film_genres (film_id, genre_id) VALUES
(100, 100), -- Inception - Action
(100, 300), -- Inception - Drama
(200, 100), -- The Matrix - Action
(200, 400), -- The Matrix - Science Fiction
(300, 400), -- Interstellar - Science Fiction
(300, 300); -- Interstellar - Drama