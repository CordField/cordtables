package com.seedcompany.cordtables.components.tables.common.stage_notifications

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

data class CommonStageNotificationsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class CommonStageNotificationsReadResponse(
    val error: ErrorType,
    val stageNotification: stageNotification? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageNotificationsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/stage-notifications/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonStageNotificationsReadRequest): CommonStageNotificationsReadResponse {

        if (req.token == null) return CommonStageNotificationsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonStageNotificationsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.stage_notifications",
                getList = false,
                columns = arrayOf(
                    "id",
                    "stage",
                    "on_enter",
                    "on_exit",
                    "person",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var stage: String? = jdbcResult.getString("stage")
                if (jdbcResult.wasNull()) stage = null

                var on_enter: Boolean? = jdbcResult.getBoolean("on_enter")
                if (jdbcResult.wasNull()) on_enter = null

                var on_exit: Boolean? = jdbcResult.getBoolean("on_exit")
                if (jdbcResult.wasNull()) on_exit = null

                var person: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) person = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val stageNotification =
                    stageNotification(
                        id = id,
                        stage = stage,
                        on_enter = on_enter,
                        on_exit = on_exit,
                        person = person,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return CommonStageNotificationsReadResponse(ErrorType.NoError, stageNotification = stageNotification)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonStageNotificationsReadResponse(ErrorType.SQLReadError)
        }

        return CommonStageNotificationsReadResponse(error = ErrorType.UnknownError)
    }
}
