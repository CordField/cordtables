package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleGraphUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPeopleGraphUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleGraphUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/people-graph/update")
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
                    cast = "::uuid"
                )
            }
            "to_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "to_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
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
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_graph",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return CommonPeopleGraphUpdateResponse(ErrorType.NoError)
    }

}
