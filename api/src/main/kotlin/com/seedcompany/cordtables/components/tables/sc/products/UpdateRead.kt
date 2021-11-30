package com.seedcompany.cordtables.components.tables.sc.products

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.products.ScProductsReadRequest
import com.seedcompany.cordtables.components.tables.sc.products.ScProductsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.products.product
import com.seedcompany.cordtables.components.tables.sc.products.productInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProductsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProductsUpdateReadResponse(
    val error: ErrorType,
    val product: product? = null,
)


@Controller("ScProductsUpdateRead")
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
    @PostMapping("sc-products/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScProductsUpdateReadRequest): ScProductsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScProductsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScProductsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScProductsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScProductsUpdateReadResponse(error = readResponse.error, readResponse.product)
    }
}