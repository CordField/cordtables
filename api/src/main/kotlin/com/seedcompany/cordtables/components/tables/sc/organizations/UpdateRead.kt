package com.seedcompany.cordtables.components.tables.sc.organizations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organizations.ScOrganizationsReadRequest
import com.seedcompany.cordtables.components.tables.sc.organizations.ScOrganizationsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.organizations.organization
import com.seedcompany.cordtables.components.tables.sc.organizations.organizationInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScOrganizationsUpdateReadResponse(
    val error: ErrorType,
    val organization: organization? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScOrganizationsUpdateRead")
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
    @PostMapping("sc-organizations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScOrganizationsUpdateReadRequest): ScOrganizationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScOrganizationsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScOrganizationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScOrganizationsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScOrganizationsUpdateReadResponse(error = readResponse.error, readResponse.organization)
    }
}