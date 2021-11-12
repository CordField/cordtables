package com.seedcompany.cordtables.common

import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

data class Session(
    val token: String? = null,
)

data class User(
    val id: String? = null,
)

data class Login(
    val user: User? = null,
)

data class Data(
    val session: Session? = null,
    val login: Login? = null,
)

data class Response(
    val data: Data? = null,
)

@Component
class CordApiRestUtils(
    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val rest: RestTemplate,
) {

    fun login(): String? {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val sessionRequest: HttpEntity<String> = HttpEntity<String>(
            """
            {"query":"query Query {session(browser: false) {token}}"}
        """.trimIndent(), headers
        )

        val sessionResponse = rest.postForObject(
            appConfig.cordApiGraphqlUrl, sessionRequest, Response::class.java
        )

        val token = sessionResponse?.data?.session?.token

        if (token != null) {

            headers.setBearerAuth(token)

            val loginRequest: HttpEntity<String> = HttpEntity<String>(
                """
            {"query":"mutation LoginMutation {login(input: { email: \"${appConfig.cordApiEmail}\", password: \"${appConfig.cordApiPassword}\" }) {user {id}}}"}
        """.trimIndent(), headers
            )

            val loginResponse = rest.postForObject(
                appConfig.cordApiGraphqlUrl, loginRequest, Response::class.java
            )

            val userId = loginResponse?.data?.login?.user?.id ?: return null

        }

        return token
    }

    final inline fun <reified T> post(token: String, query: String): T? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBearerAuth(token)

        val request: HttpEntity<String> = HttpEntity<String>(
            query, headers
        )

        val response = rest.postForObject(
            appConfig.cordApiGraphqlUrl,
            request,
            T::class.java
        )

        return response
    }
}