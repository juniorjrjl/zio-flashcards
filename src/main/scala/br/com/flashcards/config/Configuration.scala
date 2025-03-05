package br.com.flashcards.config

import br.com.flashcards.core.exception.ZioFlashcardError
import zio.*
import zio.config.magnolia.*
import zio.config.typesafe.*

trait ConfigurationError extends ZioFlashcardError

object ConfigurationError:
  case class LiquibaseError(
      originError: String,
      message: String,
      callstack: List[String]
  ) extends ConfigurationError

final case class LiquibaseConfig(
    changeLogFile: String,
    url: String,
    user: String,
    password: String
)

final case class DatabaseConfig(
    dataSourceClassName: String,
    url: String,
    user: String,
    password: String
)

final case class ServerConfig(port: Int)

final case class AppConfig(
    liquibaseConfig: LiquibaseConfig,
    serverConfig: ServerConfig,
    databaseConfig: DatabaseConfig
)

final case class RootConfig(config: AppConfig)

object Configuration:
  val layer: ZLayer[Any, Config.Error, AppConfig] =
    ZLayer.fromZIO(
      TypesafeConfigProvider
        .fromResourcePath()
        .load(deriveConfig[RootConfig])
        .map(_.config)
    )
