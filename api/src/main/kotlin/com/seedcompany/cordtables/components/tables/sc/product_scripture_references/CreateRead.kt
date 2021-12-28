package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.*
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.ScProductScriptureReferencesCreateRequest
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProductScriptureReferencesCreateReadRequest(
    val token: String? = null,
    val productScriptureReference: productScriptureReferenceInput,
)

data class ScProductScriptureReferencesCreateReadResponse(
    val error: ErrorType,
    val productScriptureReference: productScriptureReference? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductScriptureReferencesCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc/product-scripture-references/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScProductScriptureReferencesCreateReadRequest): ScProductScriptureReferencesCreateReadResponse {

        val createResponse = create.createHandler(
            ScProductScriptureReferencesCreateRequest(
                token = req.token,
                productScriptureReference = req.productScriptureReference
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScProductScriptureReferencesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScProductScriptureReferencesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScProductScriptureReferencesCreateReadResponse(error = readResponse.error, productScriptureReference = readResponse.productScriptureReference)
    }
}
