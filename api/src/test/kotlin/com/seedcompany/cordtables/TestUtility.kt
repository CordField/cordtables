package com.seedcompany.cordtables

import org.springframework.stereotype.Component

@Component
class TestUtility {

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
