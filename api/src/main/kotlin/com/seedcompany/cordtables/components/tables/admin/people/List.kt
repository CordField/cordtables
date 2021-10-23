package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.CommonSensitivity
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
import kotlin.collections.List

data class People(
    val id: Int?,
    val phone: String?,
    val picture: String?,
    val privateFirstName: String?,
    val privateLastName: String?,
    val publicFirstName: String?,
    val publicLastName: String?,
    val primaryLocation: Int?,
    val privateFullName: String?,
    val publicFullName: String?,
    val sensitivityClearance: CommonSensitivity?,
    val timeZone: String?,
    val title: String?,
    val status: String?,
    val createdAt: String?,
    val createdBy: Int?,
    val modifiedAt: String?,
    val modifiedBy: Int?,
    val owningPerson: Int?,
    val owningGroup: Int?,

)

data class PeopleListRequest(
    val token: String? = null,
)

data class PeopleListResponse(
        val error: ErrorType,
        val people: List<People>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("PeopleList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("people/list")
    @ResponseBody
    fun listHandler(@RequestBody req: PeopleListRequest): PeopleListResponse {

        if (req.token == null) return PeopleListResponse(ErrorType.TokenNotFound, null)

        val items = mutableListOf<People>()

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.people",
                columns = arrayOf(
                    "id",
                    "phone",
                    "picture",
                    "privateFirstName",
                    "privateLastName",
                    "publicFirstName",
                    "publicLastName",
                    "primaryLocation",
                    "privateFullName",
                    "publicFullName",
                    "sensitivityClearance",
                    "timeZone",
                    "title",
                    "status",
                    "createdAt",
                    "createdBy",
                    "modifiedAt",
                    "modifiedBy",
                    "owningPerson",
                    "owningGroup"
                )
            )
        ).query

        try{

            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var phone: String? = jdbcResult.getString("phone")
                if (jdbcResult.wasNull()) phone = null

                var picture: String? = jdbcResult.getString("picture")
                if (jdbcResult.wasNull()) picture = null

                var privateFirstName: String? = jdbcResult.getString("private_first_name")
                if (jdbcResult.wasNull()) privateFirstName = null

                var privateLastName: String? = jdbcResult.getString("private_last_name")
                if (jdbcResult.wasNull()) privateLastName = null

                var publicFirstName: String? = jdbcResult.getString("public_first_name")
                if (jdbcResult.wasNull()) publicFirstName = null

                var publicLastName: String? = jdbcResult.getString("public_last_name")
                if (jdbcResult.wasNull()) publicLastName = null

                var primaryLocation: Int? = jdbcResult.getInt("primary_location")
                if (jdbcResult.wasNull()) primaryLocation = null

                var privateFullName: String? = jdbcResult.getString("private_full_name")
                if (jdbcResult.wasNull()) privateFullName = null

                var publicFullName: String? = jdbcResult.getString("public_full_name")
                if (jdbcResult.wasNull()) publicFullName = null

                var sensitivityClearance: String? = jdbcResult.getString("CommonSensitivity")
                if (jdbcResult.wasNull()) sensitivityClearance = null

                var timeZone: String? = jdbcResult.getString("time_zone")
                if (jdbcResult.wasNull()) timeZone = null

                var title: String? = jdbcResult.getString("title")
                if (jdbcResult.wasNull()) title = null

                var status: String? = jdbcResult.getString("status")
                if (jdbcResult.wasNull()) status = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                var owningPerson: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                items.add(
                    People(
                        id = id,
                        phone = phone,
                        picture = picture,
                        privateFirstName = privateFirstName,
                        privateLastName = privateLastName,
                        publicFirstName = publicFirstName,
                        publicLastName = publicLastName,
                        primaryLocation = primaryLocation,
                        privateFullName = privateFullName,
                        publicFullName = publicFullName,
                        sensitivityClearance = if(sensitivityClearance == null) null else CommonSensitivity.valueOf("sensitivityClearance"),
                        timeZone = timeZone,
                        title = title,
                        status = status,
                        createdAt = createdAt,
                        createdBy = createdBy,
                        modifiedAt = modifiedAt,
                        modifiedBy = modifiedBy,
                        owningPerson = owningPerson,
                        owningGroup = owningGroup,

                )
            )

        }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return PeopleListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return PeopleListResponse(ErrorType.NoError, items)
    }

}
