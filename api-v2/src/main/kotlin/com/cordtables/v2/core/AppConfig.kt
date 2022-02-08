package com.cordtables.v2.core

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties
class AppConfig (

    @Value ("\${cord.admin.password}")
    val cordAdminPassword: String,

    @Value("\${cord.init.load-db-version}")
    val loadDbVersion: Int = 1,
        ){
}