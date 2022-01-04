package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_graph.peopleGraphInput
import com.seedcompany.cordtables.components.tables.common.people_graph.Read
import com.seedcompany.cordtables.components.tables.common.people_graph.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleGraphCreateRequest(
    val token: String? = null,
    val peopleGraph: peopleGraphInput,
)

data class CommonPeopleGraphCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleGraphCreate")
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

    @PostMapping("common/people-graph/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonPeopleGraphCreateRequest): CommonPeopleGraphCreateResponse {

        // if (req.peopleGraph.name == null) return CommonPeopleGraphCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.people_graph(from_person, to_person, rel_type, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    ?::common.people_to_people_relationship_types,
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.peopleGraph.from_person,
            req.peopleGraph.to_person,
            req.peopleGraph.rel_type,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return CommonPeopleGraphCreateResponse(error = ErrorType.NoError, id = id)
    }

}
