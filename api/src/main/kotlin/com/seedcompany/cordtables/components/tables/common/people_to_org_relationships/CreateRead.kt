package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.*
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.CommonPeopleToOrgRelationshipsCreateRequest
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleToOrgRelationshipsCreateReadRequest(
    val token: String? = null,
    val peopleToOrgRelationship: peopleToOrgRelationshipInput,
)

data class CommonPeopleToOrgRelationshipsCreateReadResponse(
    val error: ErrorType,
    val peopleToOrgRelationship: peopleToOrgRelationship? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonPeopleToOrgRelationshipsCreateRead")
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
    @PostMapping("common-people-to-org-relationships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonPeopleToOrgRelationshipsCreateReadRequest): CommonPeopleToOrgRelationshipsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonPeopleToOrgRelationshipsCreateRequest(
                token = req.token,
                peopleToOrgRelationship = req.peopleToOrgRelationship
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonPeopleToOrgRelationshipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPeopleToOrgRelationshipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonPeopleToOrgRelationshipsCreateReadResponse(error = readResponse.error, peopleToOrgRelationship = readResponse.peopleToOrgRelationship)
    }
}