package com.seedcompany.cordtables.components.tables.sil.iso_639_3_retirements

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.iso_639_3_retirements.iso6393Retirement
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


data class SilIso6393RetirementsListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilIso6393RetirementsListResponse(
    val error: ErrorType,
    val size: Int,
    val iso6393Retirements: MutableList<iso6393Retirement>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilIso6393RetirementsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-iso-639-3-retirements/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilIso6393RetirementsListRequest): SilIso6393RetirementsListResponse {
        var data: MutableList<iso6393Retirement> = mutableListOf()
        if (req.token == null) return SilIso6393RetirementsListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.iso_639_3_retirements",
                filter = "order by id",
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
                )
            )
        ).query


        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilIso6393RetirementsListResponse(ErrorType.SQLReadError, size=0, mutableListOf())
        }

        return SilIso6393RetirementsListResponse(ErrorType.NoError, size=0, data)
    }
}

