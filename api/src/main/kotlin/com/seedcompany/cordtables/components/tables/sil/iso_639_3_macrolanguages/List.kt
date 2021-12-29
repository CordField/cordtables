package com.seedcompany.cordtables.components.tables.sil.iso_639_3_macrolanguages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class SilIso6393MacrolanguagesListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilIso6393MacrolanguagesListResponse(
    val error: ErrorType,
    val size: Int,
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
    val secureList: GetPaginatedResultSet,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/iso-639-3-macrolanguages/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilIso6393MacrolanguagesListRequest): SilIso6393MacrolanguagesListResponse {
        var data: MutableList<iso6393Macrolanguage> = mutableListOf()
        if (req.token == null) return SilIso6393MacrolanguagesListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sil.iso_639_3_macrolanguages",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
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
        )
        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {

                var id: Int? = resultSet!!.getInt("id")
                if (resultSet!!.wasNull()) id = null

                var m_id: String? = resultSet!!.getString("m_id")
                if (resultSet!!.wasNull()) m_id = null

                var i_id: String? = resultSet!!.getString("i_id")
                if (resultSet!!.wasNull()) i_id = null

                var i_status: String? = resultSet!!.getString("i_status")
                if (resultSet!!.wasNull()) i_status = null

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
        }
        else{
            return SilIso6393MacrolanguagesListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }

        return SilIso6393MacrolanguagesListResponse(ErrorType.NoError, size = size, data)
    }
}

