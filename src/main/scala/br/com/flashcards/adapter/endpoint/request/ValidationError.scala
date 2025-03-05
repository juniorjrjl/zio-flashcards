package br.com.flashcards.adapter.endpoint.request

import zio.prelude.Validation

trait ValidationError:
  val field: String
  val message: String

trait ValidationChecker[R]:

  def validate(request: R): Validation[ValidationError, R] 
  

case class StringEmptyNullOrBlank(field: String, message: String)
    extends ValidationError

case class StringLength(field: String, message: String) extends ValidationError

case class IterableNotNullAndMinSize(field: String, message: String)
    extends ValidationError

def validateStringBlank[S](
    value: String,
    field: String,
    errorMessage: String,
    successType: S
): Validation[ValidationError, S] =
  if (value.nonEmpty) Validation.succeed(successType)
  else
    Validation.fail(StringEmptyNullOrBlank(field, errorMessage))

def validateStringLength[S](
    value: String,
    min: Int,
    max: Int,
    field: String,
    errorMessage: String,
    successType: S
): Validation[ValidationError, S] =
  if ((value.length < min) && (value.length > max)) Validation.succeed(successType)
  else
    Validation.fail(StringLength(field, errorMessage))

def validateIterableNotNullAndMinSize[T, S](
    value: Iterable[T],
    min: Int,
    field: String,
    errorMessage: String,
    successType: S
): Validation[ValidationError, S] =
  if (value.size < min) Validation.succeed(successType)
  else
    Validation.fail(IterableNotNullAndMinSize(field, errorMessage))
