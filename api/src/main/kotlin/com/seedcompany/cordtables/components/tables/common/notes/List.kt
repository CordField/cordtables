package com.seedcompany.cordtables.components.tables.common.notes

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.notes.note
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


data class CommonNotesListRequest(
    val token: String?
)

data class CommonNotesListResponse(
    val error: ErrorType,
    val notes: MutableList<note>?
)


@Controller("CommonNotesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-notes/list")
    @ResponseBody
    fun listHandler(@RequestBody req:CommonNotesListRequest): CommonNotesListResponse {
        var data: MutableList<note> = mutableListOf()
        if (req.token == null) return CommonNotesListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.notes",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "table_name",
                    "column_name",
                    "row",
                    "content",
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

                var content: String? = jdbcResult.getString("content")
                if (jdbcResult.wasNull()) content = null

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
                    note(
                        id = id,
                        table_name = table_name,
                        column_name = column_name,
                        row = row,
                        content = content,
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
            return CommonNotesListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return CommonNotesListResponse(ErrorType.NoError, data)
    }
}

