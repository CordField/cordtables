package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.prayerRequest
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


data class UpPrayerRequestsListRequest(
    val token: String?
)

data class UpPrayerRequestsListResponse(
    val error: ErrorType,
    val prayerRequests: MutableList<prayerRequest>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerRequestsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("up-prayer-requests/list")
    @ResponseBody
    fun listHandler(@RequestBody req:UpPrayerRequestsListRequest): UpPrayerRequestsListResponse {
        var data: MutableList<prayerRequest> = mutableListOf()
        if (req.token == null) return UpPrayerRequestsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "up.prayer_requests",
                filter = "order by id",
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
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var request_language_id: Int? = jdbcResult.getInt("request_language_id")
                if (jdbcResult.wasNull()) request_language_id = null

                var target_language_id: Int? = jdbcResult.getInt("target_language_id")
                if (jdbcResult.wasNull()) target_language_id = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

                var organization_name: String? = jdbcResult.getString("organization_name")
                if (jdbcResult.wasNull()) organization_name = null

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

                var  prayer_type: String? = jdbcResult.getString("prayer_type")
                if (jdbcResult.wasNull()) prayer_type = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return UpPrayerRequestsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return UpPrayerRequestsListResponse(ErrorType.NoError, data)
    }
}

