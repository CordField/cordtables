package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.productScriptureReference
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class ScProductScriptureReferencesListRequest(
    val token: String?
)

data class ScProductScriptureReferencesListResponse(
    val error: ErrorType,
    val productScriptureReferences: MutableList<productScriptureReference>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScProductScriptureReferencesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-product-scripture-references/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScProductScriptureReferencesListRequest): ScProductScriptureReferencesListResponse {
        var data: MutableList<productScriptureReference> = mutableListOf()
        if (req.token == null) return ScProductScriptureReferencesListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.product_scripture_references",
                filter = "order by id",
                columns = arrayOf(
                    "id",

                    "product",
                    "scripture_reference",
                    "change_to_plan",
                    "active",

                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var product: Int? = jdbcResult.getInt("product")
                if (jdbcResult.wasNull()) product = null

                var change_to_plan: Int? = jdbcResult.getInt("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var scripture_reference: Int? = jdbcResult.getInt("scripture_reference")
                if (jdbcResult.wasNull()) scripture_reference = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    productScriptureReference(
                        id = id,
                        product = product,
                        scripture_reference = scripture_reference,
                        change_to_plan = change_to_plan,
                        active = active,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScProductScriptureReferencesListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScProductScriptureReferencesListResponse(ErrorType.NoError, data)
    }
}

