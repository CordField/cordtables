package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
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
    val secureList: GetPaginatedResultSet,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("up/prayer-requests/list")
    @ResponseBody
    fun listHandler(@RequestBody req:UpPrayerRequestsListRequest): UpPrayerRequestsListResponse {
        var data: MutableList<prayerRequest> = mutableListOf()
        if (req.token == null) return UpPrayerRequestsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val jdbcResult = secureList.getPaginatedResultSetHandler(
          GetPaginatedResultSetRequest(
                tableName = "up.prayer_requests",
                token = req.token,
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
        )
        val resultSet = jdbcResult.result

        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet?.next() == true){
              
                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var request_language_id: String? = jdbcResult.getString("request_language_id")
                if (jdbcResult.wasNull()) request_language_id = null

                var target_language_id: String? = jdbcResult.getString("target_language_id")
                if (jdbcResult.wasNull()) target_language_id = null

                var sensitivity: String? = resultSet!!.getString("sensitivity")
                if (resultSet!!.wasNull()) sensitivity = null

                var organization_name: String? = resultSet!!.getString("organization_name")
                if (resultSet!!.wasNull()) organization_name = null

                var parent: String? = jdbcResult.getString("parent")
                if (jdbcResult.wasNull()) parent = null

                var translator: String? = jdbcResult.getString("translator")
                if (jdbcResult.wasNull()) translator = null

                var location: String? = resultSet!!.getString("location")
                if (resultSet!!.wasNull()) location = null

                var title: String? = resultSet!!.getString("title")
                if (resultSet!!.wasNull()) title = null

                var content: String? = resultSet!!.getString("content")
                if (resultSet!!.wasNull()) content = null

                var reviewed: Boolean? = resultSet!!.getBoolean("reviewed")
                if (resultSet!!.wasNull()) reviewed = null

                var  prayer_type: String? = resultSet!!.getString("prayer_type")
                if (resultSet!!.wasNull()) prayer_type = null

                var created_at: String? = resultSet!!.getString("created_at")
                if (resultSet!!.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = resultSet!!.getString("modified_at")
                if (resultSet!!.wasNull()) modified_at = null
                
                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
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
        }
        else{
            //println("error while listing ${e.message}")
            return UpPrayerRequestsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return UpPrayerRequestsListResponse(ErrorType.NoError, data)
    }
}

