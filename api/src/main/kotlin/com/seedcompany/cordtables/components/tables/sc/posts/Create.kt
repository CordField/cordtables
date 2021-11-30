package com.seedcompany.cordtables.components.tables.sc.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.posts.postInput
import com.seedcompany.cordtables.components.tables.sc.posts.Read
import com.seedcompany.cordtables.components.tables.sc.posts.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPostsCreateRequest(
    val token: String? = null,
    val post: postInput,
)

data class ScPostsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)


@Controller("ScPostsCreate")
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

    @PostMapping("sc-posts/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPostsCreateRequest): ScPostsCreateResponse {

        // if (req.post.name == null) return ScPostsCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.posts(directory, type, shareability, body, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::sc.post_type,
                    ?::sc.post_shareability,
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
            req.post.directory,
            req.post.type,
            req.post.shareability,
            req.post.body,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScPostsCreateResponse(error = ErrorType.NoError, id = id)
    }

}