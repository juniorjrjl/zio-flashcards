package br.com.flashcards.core.exception

trait ZioFlashcardError:
  val originError: String
  val message: String
  val callstack: List[String]
