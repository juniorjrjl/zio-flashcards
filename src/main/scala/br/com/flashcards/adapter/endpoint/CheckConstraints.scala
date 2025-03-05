package br.com.flashcards.adapter.endpoint

import br.com.flashcards.adapter.endpoint.request.ValidationChecker
import br.com.flashcards.adapter.endpoint.response.error.HttpError
import br.com.flashcards.adapter.endpoint.response.error.HttpError.{
  BadRequestError,
  RequestError
}
import br.com.flashcards.core.exception.ZioFlashcardError
import zio.{IO, ZIO}

case class ConstraintViolationError(
    originError: String,
    message: String,
    callstack: List[String],
    errors: List[RequestError]
) extends ZioFlashcardError

def checkConstraints[R](
    request: R,
    checker: ValidationChecker[R]
): IO[HttpError, R] =
  ZIO.fromEither(checker.validate(request).toEither).mapError { error =>
    val errors = error.map(e => RequestError(e.field, e.message)).toList
    BadRequestError(
      "a requisição contém erros",
      "corriga os erros e tente novamente",
      errors
    )
  }
