package com.seedcompany.cordtables.components.tables.common.coalition_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.CommonCoalitionMembershipsReadRequest
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.CommonCoalitionMembershipsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.coalitionMembership
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.coalitionMembershipInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionMembershipsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonCoalitionMembershipsUpdateReadResponse(
    val error: ErrorType,
    val coalitionMembership: coalitionMembership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionMembershipsUpdateRead")
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
    @PostMapping("common/coalition-memberships/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonCoalitionMembershipsUpdateReadRequest): CommonCoalitionMembershipsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonCoalitionMembershipsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonCoalitionMembershipsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonCoalitionMembershipsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonCoalitionMembershipsUpdateReadResponse(error = readResponse.error, readResponse.coalitionMembership)
    }
}
