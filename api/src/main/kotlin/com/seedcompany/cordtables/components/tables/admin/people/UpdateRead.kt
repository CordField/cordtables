package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleReadRequest
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.people.people
import com.seedcompany.cordtables.components.tables.admin.people.peopleInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminPeopleUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminPeopleUpdateReadResponse(
    val error: ErrorType,
    val people: people? = null,
)


@Controller("AdminPeopleUpdateRead")
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
    @PostMapping("admin-people/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminPeopleUpdateReadRequest): AdminPeopleUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminPeopleUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminPeopleUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminPeopleReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminPeopleUpdateReadResponse(error = readResponse.error, readResponse.people)
    }
}