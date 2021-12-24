package com.seedcompany.cordtables.components.tables.sil.iso_639_3_macrolanguages

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.iso_639_3_macrolanguages.iso6393Macrolanguage
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


data class SilIso6393MacrolanguagesListRequest(
    val token: String?
)

data class SilIso6393MacrolanguagesListResponse(
    val error: ErrorType,
    val iso6393Macrolanguages: MutableList<iso6393Macrolanguage>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilIso6393MacrolanguagesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-iso-639-3-macrolanguages/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilIso6393MacrolanguagesListRequest): SilIso6393MacrolanguagesListResponse {
        var data: MutableList<iso6393Macrolanguage> = mutableListOf()
        if (req.token == null) return SilIso6393MacrolanguagesListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.iso_639_3_macrolanguages",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "m_id",
                    "i_id",
                    "i_status",
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

                var m_id: String? = jdbcResult.getString("m_id")
                if (jdbcResult.wasNull()) m_id = null

                var i_id: String? = jdbcResult.getString("i_id")
                if (jdbcResult.wasNull()) i_id = null

                var i_status: String? = jdbcResult.getString("i_status")
                if (jdbcResult.wasNull()) i_status = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    iso6393Macrolanguage(
                        id = id,
                        m_id = m_id,
                        i_id = i_id,
                        i_status = i_status,
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
            return SilIso6393MacrolanguagesListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return SilIso6393MacrolanguagesListResponse(ErrorType.NoError, data)
    }
}

