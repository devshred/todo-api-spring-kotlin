CREATE TABLE IF NOT EXISTS todo
(
    id   VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    text VARCHAR NOT NULL,
    done BOOLEAN NOT NULL
);
