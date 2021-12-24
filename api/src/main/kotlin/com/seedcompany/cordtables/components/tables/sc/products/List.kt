package com.seedcompany.cordtables.components.tables.sc.products

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
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
import  javax.sql.rowset.serial.SerialArray;

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet


data class ScProductsListRequest(
    val token: String?
)

data class ScProductsListResponse(
    val error: ErrorType,
    val products: MutableList<product>?
)

data class Beer(val mediums: String)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductsList")
class List<T>(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-products/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScProductsListRequest): ScProductsListResponse {
        var data: MutableList<product> = mutableListOf()
        if (req.token == null) return ScProductsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        var rowMapper: RowMapper<Beer> = RowMapper<Beer> { resultSet: ResultSet, rowIndex: Int ->
            Beer(resultSet.getString("mediums"))
        }

        var results = jdbcTemplate.query("SELECT mediums FROM sc.products", rowMapper)

        println("Original rows:")
        results.forEach { rec -> println(rec.mediums) }


        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.products",
                filter = "order by id",
                columns = arrayOf(
                    "id",

                    "neo4j_id",
                    "name",
                    "change_to_plan",
                    "active",
                    "mediums",
                    "methodologies",
                    "purposes",
                    "type",

                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                )
            )
        ).query


        println(query)
        val q2 = "select mediums from sc.products"

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)


            while (jdbcResult.next()) {

                 //.getString("mediums")


                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
                if (jdbcResult.wasNull()) neo4j_id = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var change_to_plan: String? = jdbcResult.getString("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null
//
                var mediums: String? =  jdbcResult.getString("mediums")
                if (jdbcResult.wasNull()) mediums = null

                println(mediums)
//                var mediums: SerialArray? = jdbcResult.getObject("mediums") as SerialArray
//                if (jdbcResult.wasNull()) mediums = null
                // var mediumsar:Byte = jdbcResult.getByte("mediums")
                //println(mediums.getArray())

                var methodologies: String? = jdbcResult.getString("methodologies")
                if (jdbcResult.wasNull()) methodologies = null

                var purposes: String? = jdbcResult.getString("purposes")
                if (jdbcResult.wasNull()) purposes = null

                var type: String? = jdbcResult.getString("type")
                if (jdbcResult.wasNull()) type = null



                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null


               // println(mediums)
                data.add(
                    product(
                        id = id,

                        neo4j_id = neo4j_id,
                        name = name,
                        change_to_plan = change_to_plan,
                        active = active,
                        mediums =   mediums , // (if (mediums == null) null else ProductMediums.split(",") ),
                        methodologies = methodologies,
                        purposes = purposes,
                        type = type,

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
            return ScProductsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScProductsListResponse(ErrorType.NoError, data)
    }
}

