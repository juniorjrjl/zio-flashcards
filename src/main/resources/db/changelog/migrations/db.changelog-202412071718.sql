--liquibase formatted sql
--changeset junior:202412071715
--comment: cards table

CREATE TABLE CARDS(
    id BIGSERIAL not null primary key,
    front VARCHAR(30) not null,
    back VARCHAR(255) not null,
    deck_id BIGSERIAL not null,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_DECKS_CARDS FOREIGN KEY(deck_id) REFERENCES DECKS(id) ON DELETE CASCADE
);

--rollback DROP TABLE CARDS;
