package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.file_versions.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFileVersionsUpdateRequest(
    val token: String?,
    val fileVersion: CommonFileVersionInput? = null,
)

data class CommonFileVersionsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonFileVersionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val commonUpdate: CommonUpdate,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-file-versions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonFileVersionsUpdateRequest): CommonFileVersionsUpdateResponse {

        if (req.token == null) return CommonFileVersionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.fileVersion == null) return CommonFileVersionsUpdateResponse(ErrorType.MissingId)
        if (req.fileVersion.id == null) return CommonFileVersionsUpdateResponse(ErrorType.MissingId)

//        if (req.file.type != null && !enumContains<LocationType>(req.file.type)) {
//            return CommonFilesUpdateResponse(
//                error = ErrorType.ValueDoesNotMap
//            )
//        }

        val updateResponse = commonUpdate.updateHandler(
            CommonFileVersionsUpdateRequest(
                token = req.token,
                fileVersion = CommonFileVersionInput(
                    id = req.fileVersion.id,
                    category = req.fileVersion.category,
                    mime_type = req.fileVersion.mime_type,
                    name = req.fileVersion.name,
                    file = req.fileVersion.file,
                    file_url = req.fileVersion.file_url,
                    file_size = req.fileVersion.file_size,
                    owning_person = req.fileVersion.owning_person,
                    owning_group = req.fileVersion.owning_group,
                    peer = req.fileVersion.peer,
                ),
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonFileVersionsUpdateResponse(updateResponse.error)
        }

        if (req.fileVersion.category != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "category",
            id = req.fileVersion.id!!,
            value = req.fileVersion.category,
        )

        if (req.fileVersion.mime_type != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "mime_type",
            id = req.fileVersion.id!!,
            value = req.fileVersion.mime_type,
        )

        if (req.fileVersion.name != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "name",
            id = req.fileVersion.id!!,
            value = req.fileVersion.name,
        )

        if (req.fileVersion.file != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "file",
            id = req.fileVersion.id!!,
            value = req.fileVersion.file,
        )

        if (req.fileVersion.file_url != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "file_url",
            id = req.fileVersion.id!!,
            value = req.fileVersion.file_url,
        )

        if (req.fileVersion.file_size != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "file_size",
            id = req.fileVersion.id!!,
            value = req.fileVersion.file_size,
        )

        if (req.fileVersion.owning_person != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "owning_person",
            id = req.fileVersion.id!!,
            value = req.fileVersion.owning_person,
        )

        if (req.fileVersion.owning_group != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "owning_group",
            id = req.fileVersion.id!!,
            value = req.fileVersion.owning_group,
        )

        if (req.fileVersion.peer != null) util.updateField(
            token = req.token,
            table = "common.file_versions",
            column = "peer",
            id = req.fileVersion.id!!,
            value = req.fileVersion.peer,
        )

        return CommonFileVersionsUpdateResponse(ErrorType.NoError)
    }

}