package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.common.MimeTypes
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


data class CommonFileVersionsListRequest(
    val token: String?
)

data class CommonFileVersionsListResponse(
    val error: ErrorType,
    val fileVersions: MutableList<CommonFileVersion>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonFileVersionsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/file-versions/list")
    @ResponseBody
    fun listHandler(@RequestBody req:CommonFileVersionsListRequest): CommonFileVersionsListResponse {
        var data: MutableList<CommonFileVersion> = mutableListOf()
        if (req.token == null) return CommonFileVersionsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.file_versions",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "category",
                    "mime_type",
                    "name",
                    "file",
                    "file_url",
                    "file_size" ,
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

                var category: String? = jdbcResult.getString("category")
                if (jdbcResult.wasNull()) category = null

                var mime_type: String? = jdbcResult.getString("mime_type")
                if (jdbcResult.wasNull()) mime_type = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var file: String? = jdbcResult.getString("file")
                if (jdbcResult.wasNull()) file = null

                var file_url: String? = jdbcResult.getString("file_url")
                if (jdbcResult.wasNull()) file_url = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var file_size: Int? = jdbcResult.getInt("file_size")
                if (jdbcResult.wasNull()) file_size = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    CommonFileVersion(
                        id = id,
                        category = category,
                        mime_type = if (mime_type == null) null else MimeTypes.valueOf(mime_type),
                        name = name,
                        file = file,
                        file_url = file_url,
                        file_size = file_size,
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
            return CommonFileVersionsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return CommonFileVersionsListResponse(ErrorType.NoError, data)
    }
}
