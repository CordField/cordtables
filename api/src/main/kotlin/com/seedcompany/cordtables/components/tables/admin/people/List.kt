package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.TableNames
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.admin.people.people
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


data class AdminPeopleListRequest(
    val token: String?
)

data class AdminPeopleListResponse(
    val error: ErrorType,
    val peoples: MutableList<people>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeopleList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("admin-people/list")
    @ResponseBody
    fun listHandler(@RequestBody req:AdminPeopleListRequest): AdminPeopleListResponse {
        var data: MutableList<people> = mutableListOf()
        if (req.token == null) return AdminPeopleListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.people",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "about",
                    "phone",
                    "picture",
                    "private_first_name",
                    "private_last_name",
                    "public_first_name",
                    "public_last_name",
                    "primary_location",
                    "private_full_name",
                    "public_full_name",
                    "sensitivity_clearance",
                    "timezone",
                    "title",
                    "status",
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

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var about: String? = jdbcResult.getString("about")
                if (jdbcResult.wasNull()) about = null

                var phone: String? = jdbcResult.getString("phone")
                if (jdbcResult.wasNull()) phone = null

                var picture: String? = jdbcResult.getString("picture")
                if (jdbcResult.wasNull()) picture = null

                var private_first_name: String? = jdbcResult.getString("private_first_name")
                if (jdbcResult.wasNull()) private_first_name = null

                var private_last_name: String? = jdbcResult.getString("private_last_name")
                if (jdbcResult.wasNull()) private_last_name = null

                var public_first_name: String? = jdbcResult.getString("public_first_name")
                if (jdbcResult.wasNull()) public_first_name = null

                var public_last_name: String? = jdbcResult.getString("public_last_name")
                if (jdbcResult.wasNull()) public_last_name = null

                var primary_location: Int? = jdbcResult.getInt("primary_location")
                if (jdbcResult.wasNull()) primary_location = null

                var private_full_name: String? = jdbcResult.getString("private_full_name")
                if (jdbcResult.wasNull()) private_full_name = null

                var public_full_name: String? = jdbcResult.getString("public_full_name")
                if (jdbcResult.wasNull()) public_full_name = null

                var sensitivity_clearance: String? = jdbcResult.getString("sensitivity_clearance")
                if (jdbcResult.wasNull()) sensitivity_clearance = null

                var timezone: String? = jdbcResult.getString("timezone")
                if (jdbcResult.wasNull()) timezone = null

                var title: String? = jdbcResult.getString("title")
                if (jdbcResult.wasNull()) title = null

                var status: String? = jdbcResult.getString("status")
                if (jdbcResult.wasNull()) status = null

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
                    people(
                        id = id,
                        about = about,
                        phone = phone,
                        picture = picture,
                        private_first_name = private_first_name,
                        private_last_name = private_last_name,
                        public_first_name = public_first_name,
                        public_last_name = public_last_name,
                        primary_location = primary_location,
                        private_full_name = private_full_name,
                        public_full_name = public_full_name,
                        sensitivity_clearance = if (sensitivity_clearance == null) null else Sensitivities.valueOf(sensitivity_clearance),
                        timezone = timezone,
                        title = title,
                        status = status,
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
            return AdminPeopleListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return AdminPeopleListResponse(ErrorType.NoError, data)
    }
}
