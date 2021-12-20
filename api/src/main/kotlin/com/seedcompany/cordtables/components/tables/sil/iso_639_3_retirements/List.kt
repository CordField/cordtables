package com.seedcompany.cordtables.components.tables.sil.iso_639_3_retirements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
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
    val secureList: GetPaginatedResultSet,
) {

//    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-iso-639-3-retirements/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilIso6393RetirementsListRequest): SilIso6393RetirementsListResponse {
        var data: MutableList<iso6393Retirement> = mutableListOf()
        if (req.token == null) return SilIso6393RetirementsListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sil.iso_639_3_retirements",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
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
        )

        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {

                var id: Int? = resultSet!!.getInt("id")
                if (resultSet!!.wasNull()) id = null

                var _id: String? = resultSet!!.getString("_id")
                if (resultSet!!.wasNull()) _id = null

                var ref_name: String? = resultSet!!.getString("ref_name")
                if (resultSet!!.wasNull()) ref_name = null

                var ret_reason: String? = resultSet!!.getString("ret_reason")
                if (resultSet!!.wasNull()) ret_reason = null

                var change_to: String? = resultSet!!.getString("change_to")
                if (resultSet!!.wasNull()) change_to = null

                var ret_remedy: String? = resultSet!!.getString("ret_remedy")
                if (resultSet!!.wasNull()) ret_remedy = null

                var effective: String? = resultSet!!.getString("effective")
                if (resultSet!!.wasNull()) effective = null

                var created_by: Int? = resultSet!!.getInt("created_by")
                if (resultSet!!.wasNull()) created_by = null

                var created_at: String? = resultSet!!.getString("created_at")
                if (resultSet!!.wasNull()) created_at = null

                var modified_at: String? = resultSet!!.getString("modified_at")
                if (resultSet!!.wasNull()) modified_at = null

                var modified_by: Int? = resultSet!!.getInt("modified_by")
                if (resultSet!!.wasNull()) modified_by = null

                var owning_person: Int? = resultSet!!.getInt("owning_person")
                if (resultSet!!.wasNull()) owning_person = null

                var owning_group: Int? = resultSet!!.getInt("owning_group")
                if (resultSet!!.wasNull()) owning_group = null

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
        }
        else{
            return SilIso6393RetirementsListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }

        return SilIso6393RetirementsListResponse(ErrorType.NoError, size = size, data)
    }
}

