package com.seedcompany.cordtables.components.tables.common.threads

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

data class CommonThreadsListRequest(
        val token: String?,
        val channelId: Integer?=null
)

data class CommonThreadsListResponse(
        val error: ErrorType,
        val threads: MutableList<Thread>
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonThreadsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
){
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/threads/list")
    @ResponseBody
    fun listHandler(@RequestBody req: CommonThreadsListRequest): CommonThreadsListResponse
    {
        var data: MutableList<Thread> = mutableListOf()
        var whereClause:String = ""
        if (req.token == null) return CommonThreadsListResponse(ErrorType.TokenNotFound, mutableListOf())
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        if(req.channelId!==null){
          whereClause = "channel = :channelId"
          paramSource.addValue("channelId", req.channelId)
        }

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.threads",
                        filter = "order by id",
                        columns = arrayOf(
                                "id",
                                "content",
                                "channel",
                                "created_at",
                                "created_by",
                                "modified_at",
                                "modified_by",
                                "owning_person",
                                "owning_group",
                        ),
                        whereClause = whereClause
                )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var content: String? = jdbcResult.getString("content")
                if (jdbcResult.wasNull()) content = null

                var channel: Int? = jdbcResult.getInt("channel")
                if (jdbcResult.wasNull()) channel = null

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

                data.add(
                        Thread(
                                id = id,
                                channel = channel,
                                content = content,
                                created_at = created_at,
                                created_by = created_by,
                                modified_at = modified_at,
                                modified_by = modified_by,
                                owning_person = owning_person,
                                owning_group = owning_group
                        )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonThreadsListResponse(ErrorType.SQLReadError, mutableListOf())
        }
        return CommonThreadsListResponse(ErrorType.NoError, data)
    }
}
