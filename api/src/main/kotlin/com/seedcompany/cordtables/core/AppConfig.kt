package com.seedcompany.cordtables.core

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@OpenAPIDefinition(
info = Info(
  title = "Cord Tables ",
  version = "1.0",
),
servers = [
    Server(
      description = "API URL",
      url = ("\${server.url}").toString(),
    )
  ]
)

@Component
@EnableConfigurationProperties
class AppConfig(

    @Value("\${cord.awsAccessKeyId}")
    val awsAccessKeyId: String,

    @Value("\${cord.awsSecretAccessKey}")
    val awsSecretAccessKey: String,

    @Value("\${cord.email.server}")
    val emailServer: String,

    @Value ("\${cord.admin.password}")
    val cordAdminPassword: String,

    @Value("\${server.url}")
    val thisServerUrl: String,

    @Value("\${cord.init.load-db-version}")
    val loadDbVersion: Int = 1,

) {
    @get:Bean
    val appConfig: AppConfig
        get() = this


    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}

