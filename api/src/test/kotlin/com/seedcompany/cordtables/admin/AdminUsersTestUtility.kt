package com.seedcompany.cordtables.admin

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.components.user.LoginRequest
import com.seedcompany.cordtables.components.user.LoginReturn
import com.seedcompany.cordtables.components.user.RegisterRequest
import com.seedcompany.cordtables.components.user.RegisterReturn
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class AdminUsersTestUtility(

  @Autowired
  val testUtil: TestUtility,

  @Autowired
  val rest: TestRestTemplate,
) {

  fun register(port: String, email: String, password: String): RegisterReturn {
    val newUserResponse = rest.postForEntity(
      "http://localhost:$port/user/register",
      RegisterRequest("asdf@asdf.asdf", password),
      RegisterReturn::class.java
    )

    assert(newUserResponse !== null) { "response was null" }
    assert(newUserResponse.body !== null) { "response body was null" }
    assert(!newUserResponse.body!!.isAdmin) { "new user should not be admin" }
    assert(newUserResponse.body!!.token !== null) { "token was null" }

    return newUserResponse.body!!
  }

  fun login(port: String, email: String, password: String): LoginReturn {
    println("email: $email, password: $password")
    val loginResponse = rest.postForEntity(
      "http://localhost:$port/user/login",
      LoginRequest(email = email, password = password),
      LoginReturn::class.java,
    )

    assert(loginResponse !== null) { "response from login was null" }
    assert(loginResponse.body !== null) { "response body was null" }
    assert(loginResponse.body!!.userId !== null) { "user id was null" }
    assert(loginResponse.body!!.token !== null) { "token was null" }

    return loginResponse.body!!
  }

  fun getAdminToken(port: String): String {
    val adminLogin = login(port = port, email = testUtil.cordAdminEmail, password = testUtil.cordAdminPassword)
    return adminLogin.token!!
  }

}
