package com.seedcompany.cordtables.components.admin

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

data class GetIsCellEditableRequest(
    val token: String? = null,
    val table: String? = null,
    val column: String? = null,
    val row: Int? = null,
)

data class GetIsCellEditableResponse(
    val error: ErrorType,
    val isEditable: Boolean? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("GetIsCellEditable")
class GetIsCellEditable(
    @Autowired
    val util: Utility,
) {

    @PostMapping("admin/get-is-cell-editable")
    @ResponseBody
    fun getSecureListQueryHandler(@RequestBody req: GetIsCellEditableRequest): GetIsCellEditableResponse {

        if (req.token == null) return GetIsCellEditableResponse(ErrorType.InputMissingToken, false)
        if (req.table == null) return GetIsCellEditableResponse(ErrorType.InputMissingTable, false)
        if (req.column == null) return GetIsCellEditableResponse(ErrorType.InputMissingColumn, false)
        if (req.row == null) return GetIsCellEditableResponse(ErrorType.InputMissingRow, false)

        // todo - add row check to the util function
        val canEdit = util.userHasUpdatePermission(token = req.token, tableName = req.table, columnName = req.column, rowId = req.row)
        return GetIsCellEditableResponse( ErrorType.NoError, canEdit)
    }
}