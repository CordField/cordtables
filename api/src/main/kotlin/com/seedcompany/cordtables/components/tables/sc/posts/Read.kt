package com.seedcompany.cordtables.components.tables.sc.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScPostsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScPostsReadResponse(
    val error: ErrorType,
    val post: post? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPostsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/posts/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScPostsReadRequest): ScPostsReadResponse {

        if (req.token == null) return ScPostsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScPostsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.posts",
                getList = false,
                columns = arrayOf(
                    "id",
                    "directory",
                    "type",
                    "shareability",
                    "body",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var directory: String? = jdbcResult.getString("directory")
                if (jdbcResult.wasNull()) directory = null

                var type: String? = jdbcResult.getString("type")
                if (jdbcResult.wasNull()) type = null

                var shareability: String? = jdbcResult.getString("shareability")
                if (jdbcResult.wasNull()) shareability = null

                var body: String? = jdbcResult.getString("body")
                if (jdbcResult.wasNull()) body = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val post =
                    post(
                        id = id,
                        directory = directory,
                        type = type,
                        shareability = shareability,
                        body = body,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScPostsReadResponse(ErrorType.NoError, post = post)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScPostsReadResponse(ErrorType.SQLReadError)
        }

        return ScPostsReadResponse(error = ErrorType.UnknownError)
    }
}
