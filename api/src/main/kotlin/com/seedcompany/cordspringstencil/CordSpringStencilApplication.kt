package com.seedcompany.cordspringstencil

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
//@ConfigurationPropertiesScan
//@EnableScheduling
class CordSpringStencilApplication

fun main(args: Array<String>) {
    runApplication<CordSpringStencilApplication>(*args)
}

//@Configuration
//@EnableWebMvc
//class WebConfig : WebMvcConfigurer {
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowedHeaders("*")
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
//    }
//}