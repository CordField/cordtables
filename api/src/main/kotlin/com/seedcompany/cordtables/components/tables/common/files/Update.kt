package com.seedcompany.cordtables.components.tables.common.files

import com.seedcompany.cordtables.components.tables.common.files.CommonFilesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.files.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.files.CommonFileInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFilesUpdateRequest(
    val token: String?,
    val file: CommonFileInput? = null,
)

data class CommonFilesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonFilesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val commonUpdate: CommonUpdate,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-files/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonFilesUpdateRequest): CommonFilesUpdateResponse {

        if (req.token == null) return CommonFilesUpdateResponse(ErrorType.TokenNotFound)
        if (req.file == null) return CommonFilesUpdateResponse(ErrorType.MissingId)
        if (req.file.id == null) return CommonFilesUpdateResponse(ErrorType.MissingId)

//        if (req.file.type != null && !enumContains<LocationType>(req.file.type)) {
//            return CommonFilesUpdateResponse(
//                error = ErrorType.ValueDoesNotMap
//            )
//        }

        val updateResponse = commonUpdate.updateHandler(
            CommonFilesUpdateRequest(
                token = req.token,
                file = CommonFileInput(
                    id = req.file.id,
                    directory = req.file.directory,
                    name = req.file.name,
                    owning_person = req.file.owning_person,
                    owning_group = req.file.owning_group,
                    peer = req.file.peer,
                ),
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonFilesUpdateResponse(updateResponse.error)
        }

        if (req.file.directory != null) util.updateField(
            token = req.token,
            table = "common.files",
            column = "parent",
            id = req.file.id!!,
            value = req.file.directory,
        )

        if (req.file.name != null) util.updateField(
            token = req.token,
            table = "common.files",
            column = "name",
            id = req.file.id!!,
            value = req.file.name,
        )

        if (req.file.owning_person != null) util.updateField(
            token = req.token,
            table = "common.files",
            column = "owning_person",
            id = req.file.id!!,
            value = req.file.owning_person,
        )

        if (req.file.owning_group != null) util.updateField(
            token = req.token,
            table = "common.files",
            column = "owning_group",
            id = req.file.id!!,
            value = req.file.owning_group,
        )

        if (req.file.peer != null) util.updateField(
            token = req.token,
            table = "common.files",
            column = "peer",
            id = req.file.id!!,
            value = req.file.peer,
        )

        return CommonFilesUpdateResponse(ErrorType.NoError)
    }

}