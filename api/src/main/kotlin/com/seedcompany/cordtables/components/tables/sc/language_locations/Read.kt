package com.seedcompany.cordtables.components.tables.sc.language_locations

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

data class ScLanguageLocationsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScLanguageLocationsReadResponse(
    val error: ErrorType,
    val languageLocation: languageLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageLocationsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/language-locations/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScLanguageLocationsReadRequest): ScLanguageLocationsReadResponse {

      if (req.token == null) return ScLanguageLocationsReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScLanguageLocationsReadResponse(ErrorType.AdminOnly)

        if (req.id == null) return ScLanguageLocationsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.language_locations",
                getList = false,
                columns = arrayOf(
                    "id",
                    "language",
                    "location",
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

                var language: String? = jdbcResult.getString("language")
                if (jdbcResult.wasNull()) language = null

                var location: String? = jdbcResult.getString("location")
                if (jdbcResult.wasNull()) location = null

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

                val languageLocation =
                    languageLocation(
                        id = id,
                        language = language,
                        location = location,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScLanguageLocationsReadResponse(ErrorType.NoError, languageLocation = languageLocation)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScLanguageLocationsReadResponse(ErrorType.SQLReadError)
        }

        return ScLanguageLocationsReadResponse(error = ErrorType.UnknownError)
    }
}
