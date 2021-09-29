package com.seedcompany.cordspringstencil

import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CordSpringStencilApplicationTests(
    @LocalServerPort
    val port: Int
) {

    @Container
    private val container: BrowserWebDriverContainer<*> = BrowserWebDriverContainer<Nothing>()
        .withCapabilities(ChromeOptions().addArguments("no-sandbox").addArguments("headless"))

    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withUsername("postgres")
            withPassword("asdfasdf")
            withDatabaseName("cordfield")
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            System.setProperty("CORD_ADMIN_PASSWORD", "asdfasdf")
            System.setProperty("DB_DOMAIN", "host.docker.internal")
            System.setProperty("DB_DATABASE", "cordfield")
            System.setProperty("DB_USERNAME", "postgres")
            System.setProperty("DB_PASSWORD", "asdfasdf")

            registry.add("spring.datasource.jdbcUrl", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

//    @Test
//    fun `doesThisWork`() {
//
//        container.webDriver["http://host.docker.internal:$port/"]
//
//        val messageElement = container.webDriver.findElementByTagName("app-root")
//
//        println("app-root: ${messageElement.toString()}")
//
//        assert(true)
//    }

    @Test
    fun contextLoads() {
        assert(true)
    }

}
