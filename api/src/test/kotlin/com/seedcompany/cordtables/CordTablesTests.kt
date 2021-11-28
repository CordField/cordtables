package com.seedcompany.cordtables

import com.seedcompany.cordtables.components.user.RegisterRequest
import com.seedcompany.cordtables.components.user.RegisterReturn
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.Testcontainers.exposeHostPorts
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.nio.file.Paths
import java.time.Duration
import java.util.concurrent.TimeUnit


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CordTablesTests(
    @LocalServerPort
    val port: Int,

    @Autowired
    val rest: TestRestTemplate,
) {
    val userPassword = "asdfasdf"
    val url = "http://localhost:$port"

    @Container
    private val container: BrowserWebDriverContainer<*> = BrowserWebDriverContainer<Nothing>()
        .withCapabilities(ChromeOptions().addArguments("no-sandbox").addArguments("headless"))
    companion object {
        val home = Paths.get("").toRealPath()
        @Container
        val postgreSQLContainer:GenericContainer<Nothing> = GenericContainer<Nothing>(ImageFromDockerfile().withDockerfile(home.resolve("src/Dockerfile").toAbsolutePath()))
            .apply {
                withEnv("POSTGRES_USER", "postgres")
                withEnv("POSTGRES_PASSWORD", "asdfasdf")
                withEnv("POSTGRES_DB", "cordfield")
                withEnv("POSTGRES_PORT", "5432")
            }.withExposedPorts(5432)




        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            println(home)
            System.setProperty("DB_DOMAIN", "host.docker.internal")
            System.setProperty("DB_DATABASE", "cordfield")
            System.setProperty("DB_PORT", "5432")
            System.setProperty("DB_USERNAME", "postgres")
            System.setProperty("DB_PASSWORD", "asdfasdf")

            System.setProperty("CORD_ADMIN_PASSWORD", "asdfasdf")
            System.setProperty("CORD_API_GRAPHQL_URL", "asdfasdf")
            System.setProperty("CORD_API_EMAIL", "asdfasdf")
            System.setProperty("CORD_API_PASSWORD", "asdfasdf")

            System.setProperty("NEO4J_URL", "asdfasdf")
            System.setProperty("NEO4J_USERNAME", "asdfasdf")
            System.setProperty("NEO4J_PASSWORD", "asdfasdf")

            System.setProperty("SERVER_URL", "http://localhost:8080")
            System.setProperty("SERVER_PORT", "8080")

//            registry.add("spring.datasource.jdbcUrl", postgreSQLContainer::getJdbcUrl)
//            do this manually

//            registry.add("spring.datasource.host", postgreSQLContainer::getContainerIpAddress);
//            registry.add("spring.datasource.port") { postgreSQLContainer::getMappedPort };
            registry.add("spring.datasource.username", {"postgres"})
            registry.add("spring.datasource.password", {"asdfasdf"})
            registry.add("spring.datasource.jdbcUrl", {"jdbc:postgresql://localhost:${postgreSQLContainer.getMappedPort(5432)}/postgres"})

//            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)

        }
    }

    init {
        exposeHostPorts(port);
    }

    @Test
    fun `doesThisWork`() {
        container.webDriver["http://host.testcontainers.internal:$port/login"]
//        container.webDriver.get("http://172.17.0.1:3333/")
        var test = container.webDriver.executeScript("return document.querySelector('%s')")

//        println(test)
//        container.waitingFor()
//        val firstResult = WebDriverWait(container.webDriver, 20)
//            .until(ExpectedConditions.elementToBeSelected(By.name("q")))

        // val wait: Wait<WebDriver> = WebDriverWait(container.webDriver, 10)

        val allElements = container.webDriver.findElementsByXPath(".//*")

        for(elm in allElements){
            println(elm.tagName)
            if (elm.tagName == "input"){
                println(elm.getAttribute("name"))
            }
        }

        //wait.until(checkAppRoot(container.webDriver))

        // println("Wat Start")
        // container.webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS)
        // container.webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)


//
//
//        println(firstResult)

        // val select = container.webDriver.findElementById("tables")
//        println(select.tagName.toString())

        //val className = messageElement.getAttribute("class")
       // println(className)
        //println("app-root: ${messageElement.toString()}")
        assert(true)
    }

<<<<<<< HEAD
=======
    // WIP, not ready:)
//    @Test
//    fun `doesThisWork2`() {
//        container.webDriver["http://host.testcontainers.internal:$port/login"]
////        container.webDriver.get("http://172.17.0.1:3333/")
//        var test = container.webDriver.executeScript("return document.querySelector('%s')")
//
////        println(test)
////        container.waitingFor()
////        val firstResult = WebDriverWait(container.webDriver, 20)
////            .until(ExpectedConditions.elementToBeSelected(By.name("q")))
//
//        // val wait: Wait<WebDriver> = WebDriverWait(container.webDriver, 10)
//
//        val allElements = container.webDriver.findElementsByXPath(".//*")
//
//        for(elm in allElements){
//            println(elm.tagName)
//            if (elm.tagName == "input"){
//                println(elm.getAttribute("name"))
//            }
//        }
//
//        //wait.until(checkAppRoot(container.webDriver))
//
//        // println("Wat Start")
//        // container.webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS)
//        // container.webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)
//
//
////
////
////        println(firstResult)
//
//        // val select = container.webDriver.findElementById("tables")
////        println(select.tagName.toString())
//
//        //val className = messageElement.getAttribute("class")
//       // println(className)
//        //println("app-root: ${messageElement.toString()}")
//        assert(true)
//    }
>>>>>>> 79828a99e7bedace33d8f397b04290550a51bab4

    fun checkAppRoot(webDriver: WebDriver):Boolean {
        val  rootElements = webDriver.findElement(By.tagName("app-root")).findElements(By.tagName("div"))
        return rootElements.size > 0
    }

    @Test
    fun user() {
        val user1 = register("user1@cordtables.com", userPassword)
        assert(true) // temp until we have some legit assert in this test case
    }

    fun register(email: String, password: String): RegisterReturn {
        val newUserResponse = rest.postForEntity("$url/user/register", RegisterRequest("asdf@asdf.asdf", userPassword), RegisterReturn::class.java)
        assert(newUserResponse !== null) { "response was null" }
        assert(newUserResponse.body !== null) { "response body was null" }
        assert(!newUserResponse.body!!.isAdmin) { "new user should not be admin" }
        assert(newUserResponse.body!!.token !== null) { "token should be present" }
        assert(newUserResponse.body!!.readableTables.size == 0) { "shouldn't be able to read any tables" }

        return newUserResponse.body!!
    }

}

