package com.seedcompany.cordtables.admin

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleCreateRequest
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleCreateResponse
import com.seedcompany.cordtables.components.tables.admin.people.peopleInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class AdminPeopleTestUtility (
  @Autowired
  val testUtil: TestUtility,

  @Autowired
  val rest: TestRestTemplate,

  @Autowired
  val usersUtil: AdminUsersTestUtility,
  ){


    fun `create admin people`(port: String): String {
        val adminPeople = rest.postForEntity(
            "http://localhost:$port/admin/people/create",
            AdminPeopleCreateRequest(
                token = usersUtil.getAdminToken(port = port),
                peopleInput(
                    about = "about user",
                    phone = "956324252",
                    private_first_name = "John",
                    private_last_name = "Doe",
                    public_first_name = "John",
                    public_last_name = "Doe"
                )
            ),
            AdminPeopleCreateResponse::class.java
        )
        return adminPeople.body!!.id!!
    }


}
