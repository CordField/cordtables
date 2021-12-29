package com.seedcompany.cordtables.components.tables.common.posts

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


data class CommonPostsCreateRequest(
        val token: String? = null,
        val post: PostInput,
)

data class CommonPostsCreateResponse(
        val error: ErrorType,
        val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPostsCreate")
class Create(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common/posts/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonPostsCreateRequest): CommonPostsCreateResponse {
        if (req.token == null) return CommonPostsCreateResponse(error = com.seedcompany.cordtables.common.ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
                """
            insert into common.posts(content, thread, created_by, modified_by, owning_person, owning_group)
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
                req.post.content,
                req.post.thread,
                req.token,
                req.token,
                req.token,
        )


        return CommonPostsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
