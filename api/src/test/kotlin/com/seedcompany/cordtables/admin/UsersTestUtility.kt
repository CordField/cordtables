package com.seedcompany.cordtables.admin

import com.seedcompany.cordtables.components.user.RegisterRequest
import com.seedcompany.cordtables.components.user.RegisterReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class UsersTestUtility(
    @Autowired
    val rest: TestRestTemplate,
) {

    fun register(port: String, email: String, password: String): RegisterReturn {
        val newUserResponse = rest.postForEntity("http://localhost:$port/user/register", RegisterRequest("asdf@asdf.asdf", password), RegisterReturn::class.java)

        assert(newUserResponse !== null) {"response was null"}
        assert(newUserResponse.body !== null) {"response body was null"}
        assert(!newUserResponse.body!!.isAdmin) {"new user should not be admin"}
        assert(newUserResponse.body!!.token !== null) {"token should be present"}

        return newUserResponse.body!!
    }

}
