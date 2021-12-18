package com.seedcompany.cordtables.common

import com.seedcompany.cordtables.components.user.LoginRequest
import com.seedcompany.cordtables.components.user.LoginReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.stereotype.Component


@Component
class UtilityTest (
  @LocalServerPort
  val port: Int,

  @Autowired
  val rest: TestRestTemplate,
){
  val userPassword = "asdfasdf"
  val url = "http://localhost:$port"


  fun getToken(): String?{
    val user = userLogin("devops@tsco.org", "asdfasdf")
    return if (user.error == ErrorType.NoError){
      user.token
    }
    else {
      null
    }
  }

  fun userLogin(email: String, password: String): LoginReturn {
    val userLoginResponse = rest.postForEntity("$url/user/login", LoginRequest(email = email, password = password), LoginReturn::class.java)
    println(userLoginResponse)
    //assert(userLoginResponse !== null) { "response was null" }
    //assert(userLoginResponse.body !== null) { "response body was null" }
    //assert(!userLoginResponse.body!!.isAdmin) { "new user should not be admin" }
    //assert(userLoginResponse.body!!.token !== null) { "token should be present" }
    //assert(userLoginResponse.body!!.readableTables.size == 0) { "shouldn't be able to read any tables" }
    return  userLoginResponse.body!!
  }


}
