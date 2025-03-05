package br.com.flashcards.core.exception

sealed trait DeckServiceError extends ZioFlashcardError

case class GenericError(
    originError: String,
    message: String,
    callstack: List[String]
) extends DeckServiceError
