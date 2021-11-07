package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.TableNames
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_row_access.groupRowAccessInput
import com.seedcompany.cordtables.components.tables.admin.group_row_access.Read
import com.seedcompany.cordtables.components.tables.admin.group_row_access.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupRowAccessCreateRequest(
    val token: String? = null,
    val groupRowAccess: groupRowAccessInput,
)

data class AdminGroupRowAccessCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminGroupRowAccessCreate")
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

    @PostMapping("admin-group-row-access/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminGroupRowAccessCreateRequest): AdminGroupRowAccessCreateResponse {

        // if (req.groupRowAccess.name == null) return AdminGroupRowAccessCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.group_row_access(group_id, table_name, row,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
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
            req.groupRowAccess.group_id,
            req.groupRowAccess.table_name,
            req.groupRowAccess.row,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return AdminGroupRowAccessCreateResponse(error = ErrorType.NoError, id = id)
    }

}