package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.CommonPeopleToOrgRelationshipsReadRequest
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.CommonPeopleToOrgRelationshipsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.peopleToOrgRelationship
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.peopleToOrgRelationshipInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleToOrgRelationshipsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPeopleToOrgRelationshipsUpdateReadResponse(
    val error: ErrorType,
    val peopleToOrgRelationship: peopleToOrgRelationship? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleToOrgRelationshipsUpdateRead")
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
    @PostMapping("common/people-to-org-relationships/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonPeopleToOrgRelationshipsUpdateReadRequest): CommonPeopleToOrgRelationshipsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonPeopleToOrgRelationshipsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonPeopleToOrgRelationshipsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPeopleToOrgRelationshipsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonPeopleToOrgRelationshipsUpdateReadResponse(error = readResponse.error, readResponse.peopleToOrgRelationship)
    }
}
