package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.workflows.workflowInput
import com.seedcompany.cordtables.components.tables.common.workflows.Read
import com.seedcompany.cordtables.components.tables.common.workflows.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkflowsCreateRequest(
    val token: String? = null,
    val workflow: workflowInput,
)

data class CommonWorkflowsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)


@Controller("CommonWorkflowsCreate")
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

    @PostMapping("common-workflows/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonWorkflowsCreateRequest): CommonWorkflowsCreateResponse {

        // if (req.workflow.name == null) return CommonWorkflowsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.workflows(title, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.workflow.title,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return CommonWorkflowsCreateResponse(error = ErrorType.NoError, id = id)
    }

}