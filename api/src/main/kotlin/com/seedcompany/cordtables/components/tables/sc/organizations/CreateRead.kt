package com.seedcompany.cordtables.components.tables.sc.organizations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organizations.*
import com.seedcompany.cordtables.components.tables.sc.organizations.ScOrganizationsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.organizations.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationsCreateReadRequest(
    val token: String? = null,
    val organization: organizationInput,
)

data class ScOrganizationsCreateReadResponse(
    val error: ErrorType,
    val organization: organization? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScOrganizationsCreateRead")
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
    @PostMapping("sc-organizations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScOrganizationsCreateReadRequest): ScOrganizationsCreateReadResponse {

        val createResponse = create.createHandler(
            ScOrganizationsCreateRequest(
                token = req.token,
                organization = req.organization
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScOrganizationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScOrganizationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScOrganizationsCreateReadResponse(error = readResponse.error, organization = readResponse.organization)
    }
}