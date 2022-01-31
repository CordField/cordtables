package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.peopleToOrgRelationshipInput
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.Read
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleToOrgRelationshipsCreateRequest(
    val token: String? = null,
    val peopleToOrgRelationship: peopleToOrgRelationshipInput,
)

data class CommonPeopleToOrgRelationshipsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleToOrgRelationshipsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common/people-to-org-relationships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonPeopleToOrgRelationshipsCreateRequest): CommonPeopleToOrgRelationshipsCreateResponse {

        // if (req.peopleToOrgRelationship.name == null) return CommonPeopleToOrgRelationshipsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.people_to_org_relationships(org, person, relationship_type, begin_at, end_at, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?::common.people_to_org_relationship_type,
                    ?::timestamp,
                    ?::timestamp,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.peopleToOrgRelationship.org,
            req.peopleToOrgRelationship.person,
            req.peopleToOrgRelationship.relationship_type,
            req.peopleToOrgRelationship.begin_at,
            req.peopleToOrgRelationship.end_at,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return CommonPeopleToOrgRelationshipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
