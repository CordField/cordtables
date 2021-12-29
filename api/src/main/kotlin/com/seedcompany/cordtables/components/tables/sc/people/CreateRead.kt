package com.seedcompany.cordtables.components.tables.sc.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.people.*
import com.seedcompany.cordtables.components.tables.sc.people.ScPeopleCreateRequest
import com.seedcompany.cordtables.components.tables.sc.people.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeopleCreateReadRequest(
    val token: String? = null,
    val people: peopleInput,
)

data class ScPeopleCreateReadResponse(
    val error: ErrorType,
    val people: people? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeopleCreateRead")
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
    @PostMapping("sc/people/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScPeopleCreateReadRequest): ScPeopleCreateReadResponse {

        val createResponse = create.createHandler(
            ScPeopleCreateRequest(
                token = req.token,
                people = req.people
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScPeopleCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScPeopleReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScPeopleCreateReadResponse(error = readResponse.error, people = readResponse.people)
    }
}
