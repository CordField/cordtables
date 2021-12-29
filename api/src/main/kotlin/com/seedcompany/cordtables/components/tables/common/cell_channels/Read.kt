package com.seedcompany.cordtables.components.tables.common.cell_channels


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


data class CommonCellChannelsReadRequest(
        val token: String?,
        val id: Int? = null,
)

data class CommonCellChannelsReadResponse(
        val error: ErrorType,
        val cell_channel: CellChannel? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCellChannelsRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-cell-channels/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonCellChannelsReadRequest): CommonCellChannelsReadResponse {
        if (req.token == null) return CommonCellChannelsReadResponse(ErrorType.TokenNotFound, null)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)


        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.cell_channels",
                        getList = false,
                        columns = arrayOf(
                                "id",
                                "table_name",
                                "column_name",
                                "row",
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

                var table_name: String? = jdbcResult.getString("table_name")
                if (jdbcResult.wasNull()) table_name = null

                var column_name: String? = jdbcResult.getString("column_name")
                if (jdbcResult.wasNull()) column_name = null

                var row: Int? = jdbcResult.getInt("row")
                if (jdbcResult.wasNull()) row = null

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

                return CommonCellChannelsReadResponse(ErrorType.NoError, CellChannel(
                        id = id,
                        table_name = table_name,
                        column_name = column_name,
                        row = row,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group
                ))


            }


        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonCellChannelsReadResponse(ErrorType.SQLReadError, null)
        }
        return CommonCellChannelsReadResponse(error = ErrorType.UnknownError)

    }
}