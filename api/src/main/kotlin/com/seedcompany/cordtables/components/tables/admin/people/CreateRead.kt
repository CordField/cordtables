package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.people.*
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleCreateRequest
import com.seedcompany.cordtables.components.tables.admin.people.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminPeopleCreateReadRequest(
    val token: String? = null,
    val people: peopleInput,
)

data class AdminPeopleCreateReadResponse(
    val error: ErrorType,
    val people: people? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminPeopleCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin-people/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminPeopleCreateReadRequest): AdminPeopleCreateReadResponse {

        val createResponse = create.createHandler(
            AdminPeopleCreateRequest(
                token = req.token,
                people = req.people
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminPeopleCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminPeopleReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminPeopleCreateReadResponse(error = readResponse.error, people = readResponse.people)
    }
}