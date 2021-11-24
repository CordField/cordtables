package com.seedcompany.cordtables.components.tables.sc.budget_records

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


data class ScBudgetRecordsListRequest(
    val token: String?
)

data class ScBudgetRecordsListResponse(
    val error: ErrorType,
    val budgetrecords: MutableList<BudgetRecord>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScBudgetRecordsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-budget-records/list")
    @ResponseBody
    fun listHandler(@RequestBody req: ScBudgetRecordsListRequest): ScBudgetRecordsListResponse {
        var data: MutableList<BudgetRecord> = mutableListOf()
        if (req.token == null) return ScBudgetRecordsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        
        //budgetrequest=sql
        val query =  """with row_level_access as
(
           select     row
           from       admin.group_row_access  as a
           inner join admin.group_memberships as b
           on         a.group_id = b.group_id
           inner join admin.tokens as c
           on         b.person = c.person
           where      a.table_name = 'sc.budgetrecords'
           and        c.token = :token ), public_row_level_access as
(
           select     row
           from       admin.group_row_access  as a
           inner join admin.group_memberships as b
           on         a.group_id = b.group_id
           inner join admin.tokens as c
           on         b.person = c.person
           where      a.table_name = 'sc.budgetrecords'
           and        c.token = 'public' ), column_level_access as
(
           select     column_name
           from       admin.role_column_grants a
           inner join admin.role_memberships b
           on         a.role = b.role
           inner join admin.tokens c
           on         b.person = c.person
           where      a.table_name = 'sc.budgetrecords'
           and        c.token = :token ), public_column_level_access as
(
           select     column_name
           from       admin.role_column_grants a
           inner join admin.role_memberships b
           on         a.role = b.role
           inner join admin.tokens c
           on         b.person = c.person
           where      a.table_name = 'sc.budgetrecords'
           and        c.token = 'public' )
select
         case
                  when 'id' in
                           (
                                  select column_name
                                  from   column_level_access) then id
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then id
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then id
                  when 'id' in
                           (
                                  select column_name
                                  from   public_column_level_access) then id
                  else null
         end as id ,
         case
                  when 'neo4j_id' in
                           (
                                  select column_name
                                  from   column_level_access) then neo4j_id
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then neo4j_id
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then neo4j_id
                  when 'neo4j_id' in
                           (
                                  select column_name
                                  from   public_column_level_access) then neo4j_id
                  else null
         end as neo4j_id ,
         case
                  when 'budget' in
                           (
                                  select column_name
                                  from   column_level_access) then budget
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then budget
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then budget
                  when 'budget' in
                           (
                                  select column_name
                                  from   public_column_level_access) then budget
                  else null
         end as budget , 
         case
                  when 'change_to_plan' in
                           (
                                  select column_name
                                  from   column_level_access) then change_to_plan
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then change_to_plan
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then change_to_plan
                  when 'change_to_plan' in
                           (
                                  select column_name
                                  from   public_column_level_access) then change_to_plan
                  else null
         end as change_to_plan , 
         case
                  when 'active' in
                           (
                                  select column_name
                                  from   column_level_access) then active
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then active
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then active
                  when 'active' in
                           (
                                  select column_name
                                  from   public_column_level_access) then active
                  else null
         end as active , 
         case
                  when 'amount' in
                           (
                                  select column_name
                                  from   column_level_access) then amount
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then amount
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then amount
                  when 'amount' in
                           (
                                  select column_name
                                  from   public_column_level_access) then amount
                  else null
         end as amount , 
         case
                  when 'fiscal_year' in
                           (
                                  select column_name
                                  from   column_level_access) then fiscal_year
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then fiscal_year
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then fiscal_year
                  when 'fiscal_year' in
                           (
                                  select column_name
                                  from   public_column_level_access) then fiscal_year
                  else null
         end as fiscal_year , 
         case
                  when 'partnership' in
                           (
                                  select column_name
                                  from   column_level_access) then partnership
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then partnership
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then partnership
                  when 'partnership' in
                           (
                                  select column_name
                                  from   public_column_level_access) then partnership
                  else null
         end as partnership , 
         case
                  when 'created_at' in
                           (
                                  select column_name
                                  from   column_level_access) then created_at
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then created_at
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then created_at
                  when 'created_at' in
                           (
                                  select column_name
                                  from   public_column_level_access) then created_at
                  else null
         end as created_at ,
         case
                  when 'created_by' in
                           (
                                  select column_name
                                  from   column_level_access) then created_by
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then created_by
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then created_by
                  when 'created_by' in
                           (
                                  select column_name
                                  from   public_column_level_access) then created_by
                  else null
         end as created_by ,
         case
                  when 'modified_at' in
                           (
                                  select column_name
                                  from   column_level_access) then modified_at
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then modified_at
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then modified_at
                  when 'modified_at' in
                           (
                                  select column_name
                                  from   public_column_level_access) then modified_at
                  else null
         end as modified_at ,
         case
                  when 'modified_by' in
                           (
                                  select column_name
                                  from   column_level_access) then modified_by
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then modified_by
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then modified_by
                  when 'modified_by' in
                           (
                                  select column_name
                                  from   public_column_level_access) then modified_by
                  else null
         end as modified_by ,
         case
                  when 'owning_person' in
                           (
                                  select column_name
                                  from   column_level_access) then owning_person
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then owning_person
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then owning_person
                  when 'owning_person' in
                           (
                                  select column_name
                                  from   public_column_level_access) then owning_person
                  else null
         end as owning_person ,
         case
                  when 'owning_group' in
                           (
                                  select column_name
                                  from   column_level_access) then owning_group
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then owning_group
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then owning_group
                  when 'owning_group' in
                           (
                                  select column_name
                                  from   public_column_level_access) then owning_group
                  else null
         end as owning_group,
         case
                  when 'coordinates' in
                           (
                                  select column_name
                                  from   column_level_access) then common.ST_AsLatLonText(coordinates::text)
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = 1)) then common.ST_AsLatLonText(coordinates::text)
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then common.ST_AsLatLonText(coordinates::text)
                  when 'coordinates' in
                           (
                                  select column_name
                                  from   public_column_level_access) then common.ST_AsLatLonText(coordinates::text)
                  else null
         end as coordinates
from     sc.budgetrecords
where    id in
         (
                select row
                from   row_level_access)
or
         (
                select exists
                       (
                              select id
                              from   admin.role_memberships
                              where  person =
                                     (
                                            select person
                                            from   admin.tokens
                                            where  token = :token)
                              and    role = 1))
or       owning_person =
         (
                select person
                from   admin.tokens
                where  token = :token)
or       id in
         (
                select row
                from   public_row_level_access)
order by id;""".trimIndent()


        try {
            println(query)
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
                if (jdbcResult.wasNull()) neo4j_id = null

                var budget: Int? = jdbcResult.getInt("budget")
                if (jdbcResult.wasNull()) budget = null

                var change_to_plan: Int? = jdbcResult.getInt("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getInt("active")
                if (jdbcResult.wasNull()) active = null

                var amount: Double? = jdbcResult.getInt("amount")
                if (jdbcResult.wasNull()) amount = null

                var fiscal_year: Int? = jdbcResult.getInt("fiscal_year")
                if (jdbcResult.wasNull()) fiscal_year = null

                var partnership: Int? = jdbcResult.getInt("partnership")
                if (jdbcResult.wasNull()) partnership = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    BudgetRecord(
                        id = id,
                        neo4j_id = neo4j_id,

                        budget = budget,
                        change_to_plan = change_to_plan,
                        active = active,
                        amount = amount,
                        fiscal_year = fiscal_year,
                        partnership = partnership,

                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScBudgetRecordsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScBudgetRecordsListResponse(ErrorType.NoError, data)
    }
}

