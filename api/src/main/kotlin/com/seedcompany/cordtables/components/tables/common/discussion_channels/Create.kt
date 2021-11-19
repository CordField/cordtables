package com.seedcompany.cordtables.components.tables.common.discussion_channels

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

data class CommonDiscussionChannelsCreateRequest(
    val token: String? = null,
    val discussionchannel: DiscussionChannelInput,
)

data class CommonDiscussionChannelsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonDiscussionChannelsCreate")
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

    @PostMapping("common-discussion_channels/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonDiscussionChannelsCreateRequest): CommonDiscussionChannelsCreateResponse {

        if (req.token == null) return CommonDiscussionChannelsCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.discussionchannel == null) return CommonDiscussionChannelsCreateResponse(error = ErrorType.MissingId, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.discussion_channels(name, created_by, modified_by, owning_person, owning_group)
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
            req.discussionchannel.name,
            req.token,
            req.token,
            req.token,
        )

        req.discussionchannel.id = id

        return CommonDiscussionChannelsCreateResponse(error = ErrorType.NoError, id = id)
    }


}