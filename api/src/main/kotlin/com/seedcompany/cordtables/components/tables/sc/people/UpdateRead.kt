package com.seedcompany.cordtables.components.tables.sc.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.people.ScPeopleReadRequest
import com.seedcompany.cordtables.components.tables.sc.people.ScPeopleUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.people.people
import com.seedcompany.cordtables.components.tables.sc.people.peopleInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeopleUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPeopleUpdateReadResponse(
    val error: ErrorType,
    val people: people? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeopleUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc/people/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScPeopleUpdateReadRequest): ScPeopleUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScPeopleUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScPeopleUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScPeopleReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScPeopleUpdateReadResponse(error = readResponse.error, readResponse.people)
    }
}
