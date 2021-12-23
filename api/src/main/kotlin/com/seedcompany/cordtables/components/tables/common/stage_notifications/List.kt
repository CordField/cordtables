package com.seedcompany.cordtables.components.tables.common.stage_notifications

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.stage_notifications.stageNotification
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


data class CommonStageNotificationsListRequest(
    val token: String?
)

data class CommonStageNotificationsListResponse(
    val error: ErrorType,
    val stageNotifications: MutableList<stageNotification>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageNotificationsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/stage-notifications/list")
    @ResponseBody
    fun listHandler(@RequestBody req:CommonStageNotificationsListRequest): CommonStageNotificationsListResponse {
        var data: MutableList<stageNotification> = mutableListOf()
        if (req.token == null) return CommonStageNotificationsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.stage_notifications",
                filter = "order by id",
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
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var stage: Int? = jdbcResult.getInt("stage")
                if (jdbcResult.wasNull()) stage = null

                var on_enter: Boolean? = jdbcResult.getBoolean("on_enter")
                if (jdbcResult.wasNull()) on_enter = null

                var on_exit: Boolean? = jdbcResult.getBoolean("on_exit")
                if (jdbcResult.wasNull()) on_exit = null

                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonStageNotificationsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return CommonStageNotificationsListResponse(ErrorType.NoError, data)
    }
}

