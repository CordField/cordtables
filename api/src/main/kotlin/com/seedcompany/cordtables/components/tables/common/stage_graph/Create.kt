package com.seedcompany.cordtables.components.tables.common.stage_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_graph.stageGraphInput
import com.seedcompany.cordtables.components.tables.common.stage_graph.Read
import com.seedcompany.cordtables.components.tables.common.stage_graph.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageGraphCreateRequest(
    val token: String? = null,
    val stageGraph: stageGraphInput,
)

data class CommonStageGraphCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageGraphCreate")
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

    @PostMapping("common/stage-graph/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonStageGraphCreateRequest): CommonStageGraphCreateResponse {

        // if (req.stageGraph.name == null) return CommonStageGraphCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.stage_graph(from_stage, to_stage,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
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
            req.stageGraph.from_stage,
            req.stageGraph.to_stage,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return CommonStageGraphCreateResponse(error = ErrorType.NoError, id = id)
    }

}
