CREATE TABLE IF NOT EXISTS mpa_ratings (
    mpa_id BIGINT PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    description VARCHAR(100)
);

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL
);

-- Таблица фильмов
CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT AUTO_INCREMENT PRIMARY KEY
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE NOT NULL,
    durationInSeconds INTEGER NOT NULL,
    mpa_rating VARCHAR(10) NOT NULL
);

-- Таблица жанров
CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица дружбы
CREATE TABLE IF NOT EXISTS friendship (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

-- Таблица лайков
CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id)
);

-- Таблица жанров фильмов
CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);