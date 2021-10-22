package com.seedcompany.cordtables.components.tables.common.chats

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CreateChatRequest(
    val token: String? = null,
    val table: String? = null,
    val row: Int? = null,
)

data class CreateChatResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonChatsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common-chats/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateChatRequest): CreateChatResponse {

        val id = jdbcTemplate.queryForObject(
            """
            insert into common.chats(table_name, row) values (?::admin.table_name, ?) returning id;
        """.trimIndent(),
            Int::class.java,
            req.table,
            req.row,
        )

        return CreateChatResponse(error = ErrorType.NoError, id = id)
    }
}