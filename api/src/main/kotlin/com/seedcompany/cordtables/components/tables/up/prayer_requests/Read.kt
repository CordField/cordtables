package com.seedcompany.cordtables.components.tables.up.prayer_requests

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

data class UpPrayerRequestsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class UpPrayerRequestsReadResponse(
    val error: ErrorType,
    val prayerRequest: prayerRequest? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerRequestsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("up/prayer-requests/read")
    @ResponseBody
    fun readHandler(@RequestBody req: UpPrayerRequestsReadRequest): UpPrayerRequestsReadResponse {

        if (req.token == null) return UpPrayerRequestsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return UpPrayerRequestsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "up.prayer_requests",
                getList = false,
                columns = arrayOf(
                    "id",
                    "request_language_id",
                    "target_language_id",
                    "sensitivity",
                    "organization_name",
                    "parent",
                    "translator",
                    "location",
                    "title",
                    "content",
                    "reviewed",
                    "prayer_type",
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

                var request_language_id: String? = jdbcResult.getString("request_language_id")
                if (jdbcResult.wasNull()) request_language_id = null

                var target_language_id: String? = jdbcResult.getString("target_language_id")
                if (jdbcResult.wasNull()) target_language_id = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

                var organization_name: String? = jdbcResult.getString("organization_name")
                if (jdbcResult.wasNull()) organization_name = null

                var parent: String? = jdbcResult.getString("parent")
                if (jdbcResult.wasNull()) parent = null

                var translator: String? = jdbcResult.getString("translator")
                if (jdbcResult.wasNull()) translator = null

                var location: String? = jdbcResult.getString("location")
                if (jdbcResult.wasNull()) location = null

                var title: String? = jdbcResult.getString("title")
                if (jdbcResult.wasNull()) title = null

                var content: String? = jdbcResult.getString("content")
                if (jdbcResult.wasNull()) content = null

                var reviewed: Boolean? = jdbcResult.getBoolean("reviewed")
                if (jdbcResult.wasNull()) reviewed = null

                var  prayer_type: String? = jdbcResult.getString("prayer_type")
                if (jdbcResult.wasNull()) prayer_type = null

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

                val prayerRequest =
                    prayerRequest(
                        id = id,
                        request_language_id = request_language_id,
                        target_language_id = target_language_id,
                        sensitivity = sensitivity,
                        organization_name = organization_name,
                        parent = parent,
                        translator = translator,
                        location = location,
                        title = title,
                        content = content,
                        reviewed = reviewed,
                        prayer_type = prayer_type,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return UpPrayerRequestsReadResponse(ErrorType.NoError, prayerRequest = prayerRequest)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return UpPrayerRequestsReadResponse(ErrorType.SQLReadError)
        }

        return UpPrayerRequestsReadResponse(error = ErrorType.UnknownError)
    }
}
