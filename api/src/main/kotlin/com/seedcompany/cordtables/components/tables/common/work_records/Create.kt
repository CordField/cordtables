package com.seedcompany.cordtables.components.tables.common.work_records

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
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
    val work_record: CommonWorkRecordInput
)

data class CommonWorkRecordCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkRecordsCreate")

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

    @PostMapping("common/work-records/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonWorkRecordCreateRequest): CommonWorkRecordCreateResponse {

        if (req.token == null) return CommonWorkRecordCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.work_records(person, ticket, hours, minutes, comment, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
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
            req.work_record.person,
            req.work_record.ticket,
            req.work_record.hours,
            req.work_record.minutes,
            req.work_record.comment,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

        return CommonWorkRecordCreateResponse(error = ErrorType.NoError, id = id)
    }


}

