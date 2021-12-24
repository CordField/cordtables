package com.seedcompany.cordtables.components.tables.sc.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeopleUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPeopleUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeopleUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/people/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPeopleUpdateRequest): ScPeopleUpdateResponse {

        if (req.token == null) return ScPeopleUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScPeopleUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPeopleUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.people",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value
                )
            }
            "skills" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.people",
                    column = "skills",
                    id = req.id,
                    value = req.value
                )
            }
            "status" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.people",
                    column = "status",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.people",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.people",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return ScPeopleUpdateResponse(ErrorType.NoError)
    }

}
