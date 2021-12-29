package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.ScProductScriptureReferencesReadRequest
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.ScProductScriptureReferencesUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.productScriptureReference
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.productScriptureReferenceInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProductScriptureReferencesUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProductScriptureReferencesUpdateReadResponse(
    val error: ErrorType,
    val productScriptureReference: productScriptureReference? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductScriptureReferencesUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc-product-scripture-references/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScProductScriptureReferencesUpdateReadRequest): ScProductScriptureReferencesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScProductScriptureReferencesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScProductScriptureReferencesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScProductScriptureReferencesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScProductScriptureReferencesUpdateReadResponse(error = readResponse.error, readResponse.productScriptureReference)
    }
}