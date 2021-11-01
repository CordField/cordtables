package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.organizations.*
import com.seedcompany.cordtables.components.tables.common.organizations.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrganizationsCreateReadRequest(
        val token: String? = null,
        val organization: CommonOrganizationsInput,
)

data class CommonOrganizationsCreateReadResponse(
        val error: ErrorType,
        val organization: CommonOrganization? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonOrganizationsCreateRead")
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
    @PostMapping("common-organizations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonOrganizationsCreateReadRequest): CommonOrganizationsCreateReadResponse {

        val createResponse = create.createHandler(
                CommonOrganizationsCreateRequest(
                        token = req.token,
                        organization = req.organization
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonOrganizationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonOrganizationsReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonOrganizationsCreateReadResponse(error = readResponse.error, organization = readResponse.organization)
    }
}