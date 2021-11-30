package com.seedcompany.cordtables.components.tables.sc.products

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.products.*
import com.seedcompany.cordtables.components.tables.sc.products.ScProductsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.products.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProductsCreateReadRequest(
    val token: String? = null,
    val product: productInput,
)

data class ScProductsCreateReadResponse(
    val error: ErrorType,
    val product: product? = null,
)


@Controller("ScProductsCreateRead")
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
    @PostMapping("sc-products/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScProductsCreateReadRequest): ScProductsCreateReadResponse {

        val createResponse = create.createHandler(
            ScProductsCreateRequest(
                token = req.token,
                product = req.product
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScProductsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScProductsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScProductsCreateReadResponse(error = readResponse.error, product = readResponse.product)
    }
}