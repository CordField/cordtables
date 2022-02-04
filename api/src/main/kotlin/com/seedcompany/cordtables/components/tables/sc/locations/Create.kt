package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.components.tables.common.languages.Create as CommonCreate
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

data class ScLocationsCreateRequest(
    val token: String? = null,
    val location: ScLocationInput,
)

data class ScLocationsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLocationsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val commonCreate: CommonCreate,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("sc/locations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScLocationsCreateRequest): ScLocationsCreateResponse {

        if (req.token == null) return ScLocationsCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.location.name == null) return ScLocationsCreateResponse(error = ErrorType.InputMissingName, null)
        if(req.location.type == null) return ScLocationsCreateResponse(error = ErrorType.InputMissingColumn, null)

        // check enums and error out if needed
        if (!enumContains<LocationType>(req.location.type)) {
            return ScLocationsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        // create row with required fields, use id to update cells afterwards one by one

//        var createResponse = commonCreate.createHandler(
//            CommonLocationsCreateRequest(
//                token = req.token,
//                location = locationInput(
//                    name = req.location.name,
//                    type = req.location.type,
//                    owning_person = req.location.owning_person,
//                    owning_group = req.location.owning_group,
//                ),
//            )
//        )
//
//        if (createResponse.error != ErrorType.NoError) {
//          return ScLocationsCreateResponse(createResponse.error)
//        }


        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.locations(
                id,
                default_region,
                funding_account,
                iso_alpha_3,
                name,
                type,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
                ?,
                ?,
                ?,
                ?,
                ?,
                ?::common.location_type,
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
            req.location.id,
            req.location.default_region,
            req.location.funding_account,
            req.location.iso_alpha_3,
            req.location.name,
            req.location.type,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return ScLocationsCreateResponse(error = ErrorType.NoError, id = id)
    }


}
