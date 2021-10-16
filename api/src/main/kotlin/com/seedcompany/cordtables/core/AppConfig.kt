package com.seedcompany.cordtables.core

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

public enum class ConfigEnv {
    local,
    test,
    prod,
}

@Component
@EnableConfigurationProperties
class AppConfig(

//    @Value("\${server.port}")
//    val serverPort: String,
//
//    @Value("\${awsAccessKeyId}")
//    val awsAccessKeyId: String,
//
//    @Value("\${awsSecretAccessKey}")
//    val awsSecretAccessKey: String,
//
//    @Value("\${email.server}")
//    val emailServer: String,
//
//    @Value("\${env}")
//    val env: ConfigEnv,

    @Value ("\${cord.admin.password}")
    val cordAdminPassword: String,

    @Value("\${spring.datasource.password}")
    val dbPassword: String,

    @Value("\${cord-api.url}")
    val cordApiGraphqlUrl: String,

    @Value("\${cord-api.email}")
    val cordApiEmail: String,

    @Value("\${cord-api.password}")
    val cordApiPassword: String,

) {
    @get:Bean
    val appConfig: AppConfig
        get() = this


    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}

