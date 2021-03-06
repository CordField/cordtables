package com.seedcompany.cordtables.components.tables.common.directories


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonDirectoriesUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonDirectoriesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonDirectoriesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/directories/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonDirectoriesUpdateRequest): CommonDirectoriesUpdateResponse {

        if (req.token == null) return CommonDirectoriesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonDirectoriesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonDirectoriesUpdateResponse(ErrorType.MissingId)

//        if (req.directory.type != null && !enumContains<LocationType>(req.directory.type)) {
//            return CommonDirectoriesUpdateResponse(
//                error = ErrorType.ValueDoesNotMap
//            )
//        }

        when (req.column) {
            "parent" -> {
                util.updateField(
                    token = req.token,
                    table = "common.directories",
                    column = "parent",
                    id = req.id,
                    value = req.value
                )
            }

            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.directories",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.directories",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }

            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.directories",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }

        }

        return CommonDirectoriesUpdateResponse(ErrorType.NoError)
    }

}
