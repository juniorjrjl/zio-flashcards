package br.com.flashcards.adapter.outbound.persistence.entity

import java.time.OffsetDateTime

case class DeckEntity(
    id: Long,
    title: String,
    description: String,
    createdAt: OffsetDateTime,
    updatedAt: OffsetDateTime
)

case class CardEntity(
    id: Long,
    front: String,
    back: String,
    deckId: Long,
    createdAt: OffsetDateTime,
    updatedAt: OffsetDateTime
)
