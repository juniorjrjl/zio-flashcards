package br.com.flashcards.config

import zio.*
import zio.config.magnolia.*
import zio.config.typesafe.*

final case class LiquibaseConfig(
    changeLogFile: String,
    url: String,
    user: String,
    password: String
)

final case class ServerConfig(port: Int)

final case class AppConfig(
    liquibaseConfig: LiquibaseConfig,
    serverConfig: ServerConfig
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
