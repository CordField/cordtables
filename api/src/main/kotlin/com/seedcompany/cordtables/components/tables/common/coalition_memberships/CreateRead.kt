package com.seedcompany.cordtables.components.tables.common.coalition_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.*
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.CommonCoalitionMembershipsCreateRequest
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionMembershipsCreateReadRequest(
    val token: String? = null,
    val coalitionMembership: coalitionMembershipInput,
)

data class CommonCoalitionMembershipsCreateReadResponse(
    val error: ErrorType,
    val coalitionMembership: coalitionMembership? = null,
)


@Controller("CommonCoalitionMembershipsCreateRead")
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
    @PostMapping("common-coalition-memberships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonCoalitionMembershipsCreateReadRequest): CommonCoalitionMembershipsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonCoalitionMembershipsCreateRequest(
                token = req.token,
                coalitionMembership = req.coalitionMembership
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonCoalitionMembershipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonCoalitionMembershipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonCoalitionMembershipsCreateReadResponse(error = readResponse.error, coalitionMembership = readResponse.coalitionMembership)
    }
}