package com.seedcompany.cordtables.components.tables.sc.field_regions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_zones.fieldZoneInput
import com.seedcompany.cordtables.components.tables.sc.field_zones.Read
import com.seedcompany.cordtables.components.tables.sc.field_zones.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldRegionsCreateRequest(
    val token: String? = null,
    val fieldRegion: fieldRegionInput,
)

data class ScFieldRegionsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldRegionsCreate")
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

    @PostMapping("sc-field-regions/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScFieldRegionsCreateRequest): ScFieldRegionsCreateResponse {

        if (req.fieldRegion.name == null) return ScFieldRegionsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.field_regions(name, neo4j_id, director, created_by, modified_by, owning_person, owning_group)
                values(
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.fieldRegion.name,
            req.fieldRegion.neo4j_id,
            req.fieldRegion.director,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScFieldRegionsCreateResponse(error = ErrorType.NoError, id = id)
    }

}