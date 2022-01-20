package com.seedcompany.cordtables.components.tables.common.work_estimates

import com.seedcompany.cordtables.common.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class CommonWorkRecordCreateRequest(
    val token: String? = null,
    val work_estimate: CommonWorkEstimateInput
)

data class CommonWorkEstimateCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkEstimatesCreate")

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

    @PostMapping("common/work-estimates/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonWorkRecordCreateRequest): CommonWorkEstimateCreateResponse {

        if (req.token == null) return CommonWorkEstimateCreateResponse(error = ErrorType.InputMissingToken, null)
        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.work_estimates(person, ticket, hours, minutes, comment, created_by, modified_by, owning_person, owning_group)
                values(
                     (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?::uuid,
                    ?,
                    ?,
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
            req.token,
            req.work_estimate.ticket,
            req.work_estimate.hours,
            req.work_estimate.minutes,
            req.work_estimate.comment,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return CommonWorkEstimateCreateResponse(error = ErrorType.NoError, id = id)
    }


}

