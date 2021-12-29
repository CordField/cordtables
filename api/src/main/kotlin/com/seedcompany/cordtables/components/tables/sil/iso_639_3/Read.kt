package com.seedcompany.cordtables.components.tables.sil.iso_639_3

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.iso_639_3.iso6393
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

data class SilIso6393ReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class SilIso6393ReadResponse(
    val error: ErrorType,
    val iso6393: iso6393? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilIso6393Read")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/iso-639-3/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilIso6393ReadRequest): SilIso6393ReadResponse {

        if (req.token == null) return SilIso6393ReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilIso6393ReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.iso_639_3",
                getList = false,
                columns = arrayOf(
                    "id",
                    "_id",
                    "part_2b",
                    "part_2t",
                    "part_1",
                    "scope",
                    "type",
                    "ref_name",
                    "comment",
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

                var _id: String? = jdbcResult.getString("_id")
                if (jdbcResult.wasNull()) _id = null

                var part_2b: String? = jdbcResult.getString("part_2b")
                if (jdbcResult.wasNull()) part_2b = null

                var part_2t: String? = jdbcResult.getString("part_2t")
                if (jdbcResult.wasNull()) part_2t = null

                var part_1: String? = jdbcResult.getString("part_1")
                if (jdbcResult.wasNull()) part_1 = null

                var scope: String? = jdbcResult.getString("scope")
                if (jdbcResult.wasNull()) scope = null

                var type: String? = jdbcResult.getString("type")
                if (jdbcResult.wasNull()) type = null

                var ref_name: String? = jdbcResult.getString("ref_name")
                if (jdbcResult.wasNull()) ref_name = null

                var comment: String? = jdbcResult.getString("comment")
                if (jdbcResult.wasNull()) comment = null

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

                val iso6393 =
                    iso6393(
                        id = id,
                        _id = _id,
                        part_2b = part_2b,
                        part_2t = part_2t,
                        part_1 = part_1,
                        scope = scope,
                        type = type,
                        ref_name = ref_name,
                        comment = comment,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return SilIso6393ReadResponse(ErrorType.NoError, iso6393 = iso6393)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilIso6393ReadResponse(ErrorType.SQLReadError)
        }

        return SilIso6393ReadResponse(error = ErrorType.UnknownError)
    }
}
