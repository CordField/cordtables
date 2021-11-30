package com.seedcompany.cordtables.components.tables.sc.field_zones

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

data class ScFieldZonesCreateRequest(
    val token: String? = null,
    val fieldZone: fieldZoneInput,
)

data class ScFieldZonesCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)


@Controller("ScFieldZonesCreate")
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

    @PostMapping("sc-field-zones/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScFieldZonesCreateRequest): ScFieldZonesCreateResponse {

        if (req.fieldZone.name == null) return ScFieldZonesCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.field_zones(name, neo4j_id, director,  created_by, modified_by, owning_person, owning_group)
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
            req.fieldZone.name,
            req.fieldZone.neo4j_id,
            req.fieldZone.director,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScFieldZonesCreateResponse(error = ErrorType.NoError, id = id)
    }

}