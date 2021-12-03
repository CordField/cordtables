package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.prayerRequest
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocation
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
    val id: Int? = null,
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

    @PostMapping("up-prayer-requests/read")
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
                    "language_id",
                    "sensitivity",
                    "parent",
                    "translator",
                    "location",
                    "title",
                    "content",
                    "reviewed",
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

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var language_id: Int? = jdbcResult.getInt("language_id")
                if (jdbcResult.wasNull()) language_id = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

                var parent: Int? = jdbcResult.getInt("parent")
                if (jdbcResult.wasNull()) parent = null

                var translator: Int? = jdbcResult.getInt("translator")
                if (jdbcResult.wasNull()) translator = null

                var location: String? = jdbcResult.getString("location")
                if (jdbcResult.wasNull()) location = null

                var title: String? = jdbcResult.getString("title")
                if (jdbcResult.wasNull()) title = null

                var content: String? = jdbcResult.getString("content")
                if (jdbcResult.wasNull()) content = null

                var reviewed: Boolean? = jdbcResult.getBoolean("reviewed")
                if (jdbcResult.wasNull()) reviewed = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val prayerRequest =
                    prayerRequest(
                        id = id,
                        language_id = language_id,
                        sensitivity = sensitivity,
                        parent = parent,
                        translator = translator,
                        location = location,
                        title = title,
                        content = content,
                        reviewed = reviewed,
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
