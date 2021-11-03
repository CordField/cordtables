package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionInput
import com.seedcompany.cordtables.components.tables.common.file_versions.Read
import com.seedcompany.cordtables.components.tables.common.file_versions.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFileVersionsCreateRequest(
    val token: String? = null,
    val fileVersion: CommonFileVersionInput,
)

data class CommonFileVersionsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonFileVersionsCreate")
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

    @PostMapping("common-file-versions/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonFileVersionsCreateRequest): CommonFileVersionsCreateResponse {

        if (req.fileVersion.name == null) return CommonFileVersionsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.file_versions(name, created_by, modified_by, owning_person, owning_group)
                values(
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.fileVersion.name,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return CommonFileVersionsCreateResponse(error = ErrorType.NoError, id = id)
    }

}