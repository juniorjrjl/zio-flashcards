package br.com.flashcards.adapter.endpoint

import br.com.flashcards.adapter.endpoint.response.error.HttpError

def mapErrorResponse(error: Any): HttpError =
  error match
    case _ =>
      HttpError.GenericError("Erro inesperado", "Aconteceu um erro inesperado")
