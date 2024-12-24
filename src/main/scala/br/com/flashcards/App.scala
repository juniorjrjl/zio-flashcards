package br.com.flashcards

import br.com.flashcards.adapter.endpoint.DeckEndpoint
import br.com.flashcards.config.{
  AppConfig,
  Configuration,
  EndpointConfig,
  LiquibaseRunner
}
import br.com.flashcards.core.service.impl.DeckService
import br.com.flashcards.core.service.query.impl.DeckQueryService
import sttp.tapir.server.interceptor.cors.CORSConfig.AllowedOrigin
import sttp.tapir.server.interceptor.cors.{CORSConfig, CORSInterceptor}
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import zio.*
import zio.http.*

object App extends ZIOAppDefault:

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    val options: ZioHttpServerOptions[Any] =
      ZioHttpServerOptions.customiseInterceptors
        .corsInterceptor(
          CORSInterceptor.customOrThrow(
            CORSConfig.default.copy(
              allowedOrigin = AllowedOrigin.All
            )
          )
        )
        .options
    val serverLayer: ZLayer[AppConfig, Nothing, Server.Config] =
      ZLayer.fromZIO {
        for {
          appConfig <- ZIO.service[AppConfig]
          port = appConfig.serverConfig.port
        } yield Server.Config.default.port(port)
      }

    (for {
      migrator <- ZIO.service[LiquibaseRunner]
      appConfig <- ZIO.service[AppConfig]
      port = appConfig.serverConfig.port
      _ <- migrator.runMigrations
      endpoints <- ZIO.service[EndpointConfig]
      httpApp = ZioHttpInterpreter(options).toHttp(endpoints.endpoints)
      actualPort <- Server.install(httpApp)
      _ <- ZIO.logInfo(s"Application zio-flashcards started")
      _ <- ZIO.logInfo(s"Go to http://localhost:$port/docs to open SwaggerUI")
      _ <- ZIO.never
    } yield ())
      .provide(
        Configuration.layer,
        EndpointConfig.layer,
        DeckEndpoint.layer,
        DeckService.layer,
        DeckQueryService.layer,
        LiquibaseRunner.layer,
        serverLayer,
        Server.live
      )
      .exitCode
