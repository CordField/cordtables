package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.stageRoleColumnGrantInput
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.Read
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageRoleColumnGrantsCreateRequest(
    val token: String? = null,
    val stageRoleColumnGrant: stageRoleColumnGrantInput,
)

data class CommonStageRoleColumnGrantsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageRoleColumnGrantsCreate")
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

    @PostMapping("common/stage-role-column-grants/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonStageRoleColumnGrantsCreateRequest): CommonStageRoleColumnGrantsCreateResponse {

        // if (req.stageRoleColumnGrant.name == null) return CommonStageRoleColumnGrantsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.stage_role_column_grants(stage, role, table_name, column_name, access_level, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?::admin.table_name,
                    ?,
                    ?::admin.access_level,
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
            req.stageRoleColumnGrant.stage,
            req.stageRoleColumnGrant.role,
            req.stageRoleColumnGrant.table_name,
            req.stageRoleColumnGrant.column_name,
            req.stageRoleColumnGrant.access_level,

            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return CommonStageRoleColumnGrantsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
