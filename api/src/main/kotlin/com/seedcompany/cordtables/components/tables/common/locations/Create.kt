package com.seedcompany.cordtables.components.tables.common.locations

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonLocationsCreateRequest(
    val token: String? = null,
    val location: locationInput,
)

data class CommonLocationsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonLocationsCreate")
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

    @PostMapping("common-locations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonLocationsCreateRequest): CommonLocationsCreateResponse {

        if (req.token == null) return CommonLocationsCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.location.name == null) return CommonLocationsCreateResponse(error = ErrorType.InputMissingName, null)
        if(req.location.type == null) return CommonLocationsCreateResponse(error = ErrorType.InputMissingColumn, null)

        // check enums and error out if needed
        if (!enumContains<LocationType>(req.location.type)) {
            return CommonLocationsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        // create row with required fields, use id to update cells afterwards one by one

        val id = jdbcTemplate.queryForObject(
            """
            insert into common.locations(
                name,
                sensitivity,
                type,
                iso_alpha3,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
                ?,
                ?::common.sensitivity,
                ?::common.location_type,
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
            req.location.name,
            req.location.sensitivity,
            req.location.type,
            req.location.iso_alpha3,
            req.token,
            req.token,
            req.token,
        )

        return CommonLocationsCreateResponse(error = ErrorType.NoError, id = id)
    }


}