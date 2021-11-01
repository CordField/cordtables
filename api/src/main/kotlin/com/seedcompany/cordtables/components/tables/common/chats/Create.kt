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

data class CommonChatsCreateRequest(
    val token: String? = null,
    val chat: ChatInput,
)

data class CommonChatsCreateResponse(
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

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common-chats/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonChatsCreateRequest): CommonChatsCreateResponse {

        if (req.token == null) return CommonChatsCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.chat == null) return CommonChatsCreateResponse(error = ErrorType.MissingId, null)
        if (req.chat.channel == null) return CommonChatsCreateResponse(error = ErrorType.InputMissingName, null)
        if (req.chat.content == null) return CommonChatsCreateResponse(error = ErrorType.InputMissingName, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.chats(channel, content, created_by, modified_by, owning_person, owning_group)
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
            req.chat.channel,
            req.chat.content,
            req.token,
            req.token,
            req.token,
        )

        req.chat.id = id

        return CommonChatsCreateResponse(error = ErrorType.NoError, id = id)
    }


}