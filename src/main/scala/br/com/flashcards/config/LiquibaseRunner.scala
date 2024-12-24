package br.com.flashcards.config

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import zio.{ZIO, ZLayer}

import java.nio.file.Paths
import java.sql.DriverManager

case class LiquibaseRunner(private val config: LiquibaseConfig):

  def runMigrations: ZIO[Any, Throwable, Unit] = ZIO.attempt {
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
  }

object LiquibaseRunner:
  val layer: ZLayer[AppConfig, Nothing, LiquibaseRunner] =
    ZLayer.fromZIO {
      for {
        appConfig <- ZIO.service[AppConfig]
      } yield LiquibaseRunner(appConfig.liquibaseConfig)
    }
