package com.seedcompany.cordtables.components.tables.common.notes

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.notes.noteInput
import com.seedcompany.cordtables.components.tables.common.notes.Read
import com.seedcompany.cordtables.components.tables.common.notes.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonNotesCreateRequest(
    val token: String? = null,
    val note: noteInput,
)

data class CommonNotesCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonNotesCreate")
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

    @PostMapping("common/notes/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonNotesCreateRequest): CommonNotesCreateResponse {

        // if (req.note.name == null) return CommonNotesCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.notes(table_name, column_name, row, content, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::admin.table_name,
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.note.table_name,
            req.note.column_name,
            req.note.row,
            req.note.content,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return CommonNotesCreateResponse(error = ErrorType.NoError, id = id)
    }

}
