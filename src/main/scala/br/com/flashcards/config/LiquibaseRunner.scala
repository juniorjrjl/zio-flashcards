package br.com.flashcards.config

import br.com.flashcards.config.ConfigurationError.LiquibaseError
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import zio.{ZIO, ZLayer}

import java.nio.file.Paths
import java.sql.DriverManager

case class LiquibaseRunner(private val config: LiquibaseConfig):

  def runMigrations: ZIO[Any, LiquibaseError, Unit] = ZIO.attempt {
    val liquibase = Liquibase(
      config.changeLogFile,
      DirectoryResourceAccessor(Paths.get("src/main/resources")),
      JdbcConnection(
        DriverManager.getConnection(
          config.url,
          config.user,
          config.password
        )
      )
    )
    liquibase.update()
  }.mapError {
    ex =>
      val callstack = ex.getStackTrace.map(_.toString).toList
      LiquibaseError(ex.getClass.getName, ex.getMessage, callstack)
  }

object LiquibaseRunner:
  val layer: ZLayer[AppConfig, Nothing, LiquibaseRunner] =
    ZLayer.fromZIO {
      for {
        appConfig <- ZIO.service[AppConfig]
      } yield LiquibaseRunner(appConfig.liquibaseConfig)
    }
