package com.seedcompany.cordtables.components.tables.common.threads

import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.ErrorType

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonThreadsCreateRequest(
        val token: String? = null,
        val thread: ThreadInput,
)

data class CommonThreadsCreateResponse(
        val error: ErrorType,
        val id: Int? = null,
)


@Controller("CommonThreadsCreate")
class Create(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common-threads/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonThreadsCreateRequest): CommonThreadsCreateResponse {
        if (req.token == null) return CommonThreadsCreateResponse(error = com.seedcompany.cordtables.common.ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
                """
            insert into common.threads(content, channel, created_by, modified_by, owning_person, owning_group)
                values(
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
                req.thread.content,
                req.thread.channel,
                req.token,
                req.token,
                req.token,
        )


        return CommonThreadsCreateResponse(error = ErrorType.NoError, id = id)
    }

}