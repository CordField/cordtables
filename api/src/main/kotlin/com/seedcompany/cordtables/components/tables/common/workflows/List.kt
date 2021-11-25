package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.common.CommonTicketStatus

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
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


data class CommonWorkflowsListRequest(
        val token: String?
)

data class CommonWorkflowsListResponse(
        val error: ErrorType,
        val workflow: MutableList<CommonWorkflowsRecords>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonWorkflowsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-workflows/list")
    @ResponseBody
    fun listHandler(@RequestBody req: CommonWorkflowsListRequest): CommonWorkflowsListResponse{
        var data: MutableList<CommonWorkflowsRecords> = mutableListOf()
        if (req.token == null) return CommonWorkflowsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)


        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement("""
                select
	                common.tickets.id,
                    common.tickets.ticket_status,
	                common.tickets.parent,
	                common.tickets.content,
	                common.work_estimates.total_time as estimated_total_time,
	                common.ticket_graph.from_ticket as blocked_by,
                    common.ticket_assignments.person as assigned_person,
	                common.work_records.total_time as total_time_worked,
                    common.work_records.comment,
                    common.ticket_feedback.feedback
                from
	                common.tickets
	            left join common.work_estimates on (common.work_estimates.id = common.tickets.id)
	            left join common.ticket_graph on (common.ticket_graph.to_ticket = common.tickets.id)
	            left join common.ticket_assignments on (common.ticket_assignments.ticket = tickets.id)
	            left join common.work_records on (common.work_records.ticket = common.tickets.id)
	            left join common.ticket_feedback on (common.ticket_feedback.ticket = common.tickets.id)
            """.trimIndent())

            try {

            val jdbcResult = statement.executeQuery()

            while (jdbcResult.next()){

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var ticket_status: String? = jdbcResult.getString("ticket_status")
                if (jdbcResult.wasNull()) ticket_status = null

                var parent: Int? = jdbcResult.getInt("parent")
                if (jdbcResult.wasNull()) parent = null

                var content: String? = jdbcResult.getString("content")
                if(jdbcResult.wasNull()) content = null

                var estimatedTotalTime: Number? = jdbcResult.getFloat("estimated_total_time")
                if (jdbcResult.wasNull()) estimatedTotalTime = null

                var blockedBy : Int? = jdbcResult.getInt("blocked_by")
                if (jdbcResult.wasNull()) blockedBy = null

                var assignedPerson: Int? = jdbcResult.getInt("assigned_person")
                if (jdbcResult.wasNull()) assignedPerson = null

                var totalTimeWorked : Number? = jdbcResult.getFloat("total_time_worked")
                if (jdbcResult.wasNull()) totalTimeWorked = null

                var comment : String? = jdbcResult.getString("comment")
                if (jdbcResult.wasNull()) comment = null

                var feedback : String? = jdbcResult.getString("feedback")
                if (jdbcResult.wasNull()) feedback = null


                data.add(
                        CommonWorkflowsRecords(
                                id = id,
                                ticket_status = if(ticket_status == null) null else CommonTicketStatus.valueOf(ticket_status),
                                parent = parent,
                                content = content,
                                estimated_total_time = estimatedTotalTime,
                                blocked_by = blockedBy,
                                assigned_person = assignedPerson,
                                total_time_worked = totalTimeWorked,
                                comment = comment,
                                feedback = feedback,
                        )
                )
            }
            } catch (e: SQLException) {
                println("error while listing ${e.message}")
                return CommonWorkflowsListResponse(ErrorType.SQLReadError, mutableListOf())
            }
        }

        return CommonWorkflowsListResponse(ErrorType.NoError, data)
    }
}
