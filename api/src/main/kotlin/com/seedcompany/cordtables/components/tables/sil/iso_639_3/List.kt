package com.seedcompany.cordtables.components.tables.sil.iso_639_3

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class SilIso6393ListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilIso6393ListResponse(
    val error: ErrorType,
    val size: Int,
    val iso6393s: MutableList<iso6393>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilIso6393List")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetPaginatedResultSet,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/iso-639-3/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilIso6393ListRequest): SilIso6393ListResponse {
        var data: MutableList<iso6393> = mutableListOf()
        if (req.token == null) return SilIso6393ListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

        // var offset = (req.page-1)* req.resultsPerPage!!
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        // paramSource.addValue("limit", req.resultsPerPage)
        // paramSource.addValue("offset", offset)

        val jdbcResult = secureList.getPaginatedResultSetHandler(
          GetPaginatedResultSetRequest(
                tableName = "sil.iso_639_3",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
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
                )
            )
        )

        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {
                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var _id: String? = resultSet!!.getString("_id")
                if (resultSet!!.wasNull()) _id = null

                var part_2b: String? = resultSet!!.getString("part_2b")
                if (resultSet!!.wasNull()) part_2b = null

                var part_2t: String? = resultSet!!.getString("part_2t")
                if (resultSet!!.wasNull()) part_2t = null

                var part_1: String? = resultSet!!.getString("part_1")
                if (resultSet!!.wasNull()) part_1 = null

                var scope: String? = resultSet!!.getString("scope")
                if (resultSet!!.wasNull()) scope = null

                var type: String? = resultSet!!.getString("type")
                if (resultSet!!.wasNull()) type = null

                var ref_name: String? = resultSet!!.getString("ref_name")
                if (resultSet!!.wasNull()) ref_name = null

                var comment: String? = resultSet!!.getString("comment")
                if (resultSet!!.wasNull()) comment = null

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
                )
            }
        }
        else{
            return SilIso6393ListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }

        return SilIso6393ListResponse(ErrorType.NoError, size = size, data)
    }
}

