--liquibase formatted sql
--changeset junior:202412071715
--comment: decks table

CREATE TABLE DECKS(
    id BIGSERIAL not null primary key,
    title VARCHAR(30) not null,
    description VARCHAR(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

--rollback DROP TABLE DECKS;
