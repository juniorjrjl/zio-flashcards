package br.com.flashcards.adapter.outbound.persistence.exception

import br.com.flashcards.core.exception.ZioFlashcardError

trait RepositoryError extends ZioFlashcardError

case class DeckRepositoryError(
    originError: String,
    message: String,
    callstack: List[String]
) extends RepositoryError
