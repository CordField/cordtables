package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleToOrgRelationshipsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPeopleToOrgRelationshipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleToOrgRelationshipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/people-to-org-relationships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonPeopleToOrgRelationshipsUpdateRequest): CommonPeopleToOrgRelationshipsUpdateResponse {

        if (req.token == null) return CommonPeopleToOrgRelationshipsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonPeopleToOrgRelationshipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonPeopleToOrgRelationshipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "org" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "org",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "relationship_type" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "relationship_type",
                    id = req.id,
                    value = req.value,
                    cast = "::common.people_to_org_relationship_type"
                )
            }
            "begin_at" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "begin_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "end_at" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "end_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.people_to_org_relationships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonPeopleToOrgRelationshipsUpdateResponse(ErrorType.NoError)
    }

}
