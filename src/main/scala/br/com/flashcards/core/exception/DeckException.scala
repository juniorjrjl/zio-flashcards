package br.com.flashcards.core.exception

sealed trait DeckException:
  case object GenericError extends DeckException