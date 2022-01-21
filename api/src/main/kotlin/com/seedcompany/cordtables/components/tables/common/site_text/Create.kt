package com.seedcompany.cordtables.components.tables.common.site_text

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonSiteTextCreateRequest(
        val token: String? = null,
        val site_text: CommonSiteTextInput,
)

data class CommonSiteTextCreateResponse(
        val error: ErrorType,
        val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextCreate")
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

    @PostMapping("common/site-texts/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonSiteTextCreateRequest): CommonSiteTextCreateResponse {

        if (req.token == null) return CommonSiteTextCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.site_text.ethnologue == null) return CommonSiteTextCreateResponse(error = ErrorType.InputMissingColumn, null)

        try {
        val id = jdbcTemplate.queryForObject("""
            insert into common.site_text(
                ethnologue,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
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
                ?
            )
            returning id;
            """.trimIndent(),
                String::class.java,
                req.site_text.ethnologue,
                req.token,
                req.token,
                req.token,
                util.adminGroupId()
            )

            return CommonSiteTextCreateResponse(error = ErrorType.NoError, id = id)

        } catch (e: SQLException) {
            println(e.message)
            return CommonSiteTextCreateResponse(ErrorType.SQLInsertError, null)
        }
    }
}
