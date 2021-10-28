package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.components.tables.common.locations.Create as CommonCreate
import com.seedcompany.cordtables.components.tables.common.locations.CommonLocationsCreateRequest
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.locations.CommonLocationInput
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
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
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

    @PostMapping("sc-locations/create")
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

        var createResponse = commonCreate.createHandler(
            CommonLocationsCreateRequest(
                token = req.token,
                location = CommonLocationInput(
                    name = req.location.name,
                    type = req.location.type,
                    owning_person = req.location.owning_person,
                    owning_group = req.location.owning_group,
                    peer = req.location.peer,
                ),
            )
        )

        if (createResponse.error != ErrorType.NoError) {
          return ScLocationsCreateResponse(createResponse.error)
        }


        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.locations(
                id,
                name,
                type,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
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
                1
            )
            returning id;
        """.trimIndent(),
            Int::class.java,
            createResponse.id,
            req.location.name,
            req.location.type,
            req.token,
            req.token,
            req.token,
        )

        req.location.id = id
        println("==========> 3 ==================>")

        val updateResponse = update.updateHandler(
            ScLocationsUpdateRequest(
                token = req.token,
                location = req.location,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScLocationsCreateResponse(updateResponse.error)
        }

        return ScLocationsCreateResponse(error = ErrorType.NoError, id = id)
    }


}