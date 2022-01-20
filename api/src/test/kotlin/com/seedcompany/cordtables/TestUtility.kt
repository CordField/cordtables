package com.seedcompany.cordtables

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class TestUtility {

  val cordAdminEmail = "devops@tsco.org"
  val cordAdminPassword = "asdfasdf"

  val dbUser = "postgres"
  val dbPassword = "asdfasdf"
  val dbDatabase = "cordfield"
  val dbPort = 5432
  val dbDomain = "host.docker.internal"

  val serverUrl = "http://localhost:8080"
  val serverPort = 8080

  val awsAccessKey = "asdf"
  val awsSecret = "asdf"

  val emailServer = "http://localhost:8080"

}
