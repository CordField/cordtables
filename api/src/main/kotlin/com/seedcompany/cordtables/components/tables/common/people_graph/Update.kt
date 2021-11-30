package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.components.tables.common.people_graph.CommonPeopleGraphUpdateRequest
import com.seedcompany.cordtables.components.tables.common.people_graph.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.people_graph.CommonPeopleGraphUpdateResponse
import com.seedcompany.cordtables.components.tables.common.people_graph.peopleGraphInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleGraphUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPeopleGraphUpdateResponse(
    val error: ErrorType,
)



@Controller("CommonPeopleGraphUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-people-graph/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonPeopleGraphUpdateRequest): CommonPeopleGraphUpdateResponse {

        if (req.token == null) return CommonPeopleGraphUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonPeopleGraphUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonPeopleGraphUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "from_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "from_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "to_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "to_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "rel_type" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "rel_type",
                    id = req.id,
                    value = req.value,
                    cast = "::common.people_to_people_relationship_types"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonPeopleGraphUpdateResponse(ErrorType.NoError)
    }

}