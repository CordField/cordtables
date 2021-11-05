package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.common.MimeTypes
import com.seedcompany.cordtables.common.CommonSensitivity
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

data class CommonFileVersionsReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class CommonFileVersionsReadResponse(
    val error: ErrorType,
    val fileVersion: CommonFileVersion? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonFileVersionsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-file-versions/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonFileVersionsReadRequest): CommonFileVersionsReadResponse {

        if (req.token == null) return CommonFileVersionsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonFileVersionsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.file_versions",
                getList = false,
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
                    "peer"
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var category: String? = jdbcResult.getString("category")
                if (jdbcResult.wasNull()) category = null

                var mime_type: String? = jdbcResult.getString("mime_type")
                if (jdbcResult.wasNull()) mime_type = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var file: Int? = jdbcResult.getInt("file")
                if (jdbcResult.wasNull()) file = null

                var file_url: String? = jdbcResult.getString("file_url")
                if (jdbcResult.wasNull()) file_url = null

                var file_size: Int? = jdbcResult.getInt("file_size")
                if (jdbcResult.wasNull()) file_size = null

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

                var peer: Int? = jdbcResult.getInt("peer")
                if (jdbcResult.wasNull()) peer = null

                val fileVersion =
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
                        peer = peer
                    )



                return CommonFileVersionsReadResponse(ErrorType.NoError, fileVersion = fileVersion)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonFileVersionsReadResponse(ErrorType.SQLReadError)
        }

        return CommonFileVersionsReadResponse(error = ErrorType.UnknownError)
    }
}