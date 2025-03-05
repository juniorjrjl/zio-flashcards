package br.com.flashcards.config

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.jdbczio.Quill
import io.getquill.{JdbcContextConfig, PostgresZioJdbcContext, SnakeCase}
import zio.{URLayer, ZIO, ZLayer}

import javax.sql.DataSource


object DBContext extends PostgresZioJdbcContext(SnakeCase)

object QuillConfig:

  val layer: ZLayer[DataSource, Nothing, Quill.Postgres[SnakeCase]] =
    Quill.Postgres.fromNamingStrategy(SnakeCase)

object DataSource:
  val layer: URLayer[AppConfig, DataSource] =
    ZLayer.fromZIO{
      for {
        appConfig <- ZIO.service[AppConfig]
        databaseConfig = appConfig.databaseConfig
        datasource <- ZIO.succeed{
          //JdbcContextConfig(LoadConfig()).configProperties
          val hikariConfig = HikariConfig()
          hikariConfig.setJdbcUrl(databaseConfig.url)
          hikariConfig.setDriverClassName(databaseConfig.dataSourceClassName)
          hikariConfig.setUsername(databaseConfig.user)
          hikariConfig.setPassword(databaseConfig.password)
          new HikariDataSource(hikariConfig)
        }
      } yield datasource
    }
