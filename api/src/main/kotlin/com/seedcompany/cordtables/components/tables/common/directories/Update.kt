package com.seedcompany.cordtables.components.tables.common.directories


import com.seedcompany.cordtables.components.tables.common.directories.CommonDirectoriesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.directories.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.directories.CommonDirectoryInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonDirectoriesUpdateRequest(
    val token: String?,
    val directory: CommonDirectoryInput? = null,
)

data class CommonDirectoriesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonDirectoriesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val commonUpdate: CommonUpdate,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-directories/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonDirectoriesUpdateRequest): CommonDirectoriesUpdateResponse {

        if (req.token == null) return CommonDirectoriesUpdateResponse(ErrorType.TokenNotFound)
        if (req.directory == null) return CommonDirectoriesUpdateResponse(ErrorType.MissingId)
        if (req.directory.id == null) return CommonDirectoriesUpdateResponse(ErrorType.MissingId)

//        if (req.directory.type != null && !enumContains<LocationType>(req.directory.type)) {
//            return CommonDirectoriesUpdateResponse(
//                error = ErrorType.ValueDoesNotMap
//            )
//        }

        val updateResponse = commonUpdate.updateHandler(
            CommonDirectoriesUpdateRequest(
                token = req.token,
                directory = CommonDirectoryInput(
                    id = req.directory.id,
                    parent = req.directory.parent,
                    name = req.directory.name,
                    owning_person = req.directory.owning_person,
                    owning_group = req.directory.owning_group,
                    peer = req.directory.peer,
                ),
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonDirectoriesUpdateResponse(updateResponse.error)
        }

        if (req.directory.parent != null) util.updateField(
            token = req.token,
            table = "common.directories",
            column = "parent",
            id = req.directory.id!!,
            value = req.directory.parent,
        )

        if (req.directory.name != null) util.updateField(
            token = req.token,
            table = "common.directories",
            column = "name",
            id = req.directory.id!!,
            value = req.directory.name,
        )

        if (req.directory.owning_person != null) util.updateField(
            token = req.token,
            table = "common.directories",
            column = "owning_person",
            id = req.directory.id!!,
            value = req.directory.owning_person,
        )

        if (req.directory.owning_group != null) util.updateField(
            token = req.token,
            table = "common.directories",
            column = "owning_group",
            id = req.directory.id!!,
            value = req.directory.owning_group,
        )

        if (req.directory.peer != null) util.updateField(
            token = req.token,
            table = "common.directories",
            column = "peer",
            id = req.directory.id!!,
            value = req.directory.peer,
        )

        return CommonDirectoriesUpdateResponse(ErrorType.NoError)
    }

}