package com.seedcompany.cordtables.components.tables.sil.iso_639_3_retirements

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.iso_639_3_retirements.iso6393Retirement
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

data class SilIso6393RetirementsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class SilIso6393RetirementsReadResponse(
    val error: ErrorType,
    val iso6393Retirement: iso6393Retirement? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilIso6393RetirementsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/iso-639-3-retirements/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilIso6393RetirementsReadRequest): SilIso6393RetirementsReadResponse {

        if (req.token == null) return SilIso6393RetirementsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilIso6393RetirementsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.iso_639_3_retirements",
                getList = false,
                columns = arrayOf(
                    "id",
                    "_id",
                    "ref_name",
                    "ret_reason",
                    "change_to",
                    "ret_remedy",
                    "effective",
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

                var _id: String? = jdbcResult.getString("_id")
                if (jdbcResult.wasNull()) _id = null

                var ref_name: String? = jdbcResult.getString("ref_name")
                if (jdbcResult.wasNull()) ref_name = null

                var ret_reason: String? = jdbcResult.getString("ret_reason")
                if (jdbcResult.wasNull()) ret_reason = null

                var change_to: String? = jdbcResult.getString("change_to")
                if (jdbcResult.wasNull()) change_to = null

                var ret_remedy: String? = jdbcResult.getString("ret_remedy")
                if (jdbcResult.wasNull()) ret_remedy = null

                var effective: String? = jdbcResult.getString("effective")
                if (jdbcResult.wasNull()) effective = null

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

                val iso6393Retirement =
                    iso6393Retirement(
                        id = id,
                        _id = _id,
                        ref_name = ref_name,
                        ret_reason = ret_reason,
                        change_to = change_to,
                        ret_remedy = ret_remedy,
                        effective = effective,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return SilIso6393RetirementsReadResponse(ErrorType.NoError, iso6393Retirement = iso6393Retirement)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilIso6393RetirementsReadResponse(ErrorType.SQLReadError)
        }

        return SilIso6393RetirementsReadResponse(error = ErrorType.UnknownError)
    }
}
