package com.seedcompany.cordtables.components.tables.sc.funding_accounts

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

data class ScFundingAccountsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScFundingAccountsReadResponse(
    val error: ErrorType,
    val fundingAccount: fundingAccount? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFundingAccountsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/funding-accounts/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScFundingAccountsReadRequest): ScFundingAccountsReadResponse {

        if (req.token == null) return ScFundingAccountsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScFundingAccountsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.funding_accounts",
                getList = false,
                columns = arrayOf(
                    "id",
//                    "neo4j_id",
                    "account_number",
                    "name",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

//                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
//                if (jdbcResult.wasNull()) neo4j_id = null

                var account_number: Int? = jdbcResult.getInt("account_number")
                if (jdbcResult.wasNull()) account_number = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val fundingAccount =
                    fundingAccount(
                        id = id,
//                        neo4j_id = neo4j_id,
                        account_number = account_number,
                        name = name,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScFundingAccountsReadResponse(ErrorType.NoError, fundingAccount = fundingAccount)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScFundingAccountsReadResponse(ErrorType.SQLReadError)
        }

        return ScFundingAccountsReadResponse(error = ErrorType.UnknownError)
    }
}
