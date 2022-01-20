package com.seedcompany.cordtables.components.tables.sc.periodic_reports

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.periodicReportInput
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.Read
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeriodicReportsCreateRequest(
    val token: String? = null,
    val periodicReport: periodicReportInput,
)

data class ScPeriodicReportsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeriodicReportsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("sc/periodic-reports/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPeriodicReportsCreateRequest): ScPeriodicReportsCreateResponse {

      if (req.token == null) return ScPeriodicReportsCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScPeriodicReportsCreateResponse(ErrorType.AdminOnly)
        // if (req.periodicReport.name == null) return ScPeriodicReportsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.periodic_reports(directory, end_at, report_file, start_at, type, skipped_reason, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::timestamp,
                    ?::uuid,
                    ?::timestamp,
                    ?::sc.periodic_report_type,
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.periodicReport.directory,
            req.periodicReport.end_at,
            req.periodicReport.report_file,
            req.periodicReport.start_at,
            req.periodicReport.type,
            req.periodicReport.skipped_reason,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScPeriodicReportsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
