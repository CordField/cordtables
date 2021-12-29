package com.seedcompany.cordtables.components.tables.common.posts


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.threads.Thread
import com.seedcompany.cordtables.components.tables.sc.people.people
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

data class CommonPostsListRequest(
  val token: String?,
  val threadId: Integer? = null
)

data class CommonPostsListResponse(
  val error: ErrorType,
  val posts: MutableList<Post>,
  val peopleDetails: MutableList<Utility.PeopleDetails>
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPostsList")
class List(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common/posts/list")
  @ResponseBody
  fun listHandler(@RequestBody req: CommonPostsListRequest): CommonPostsListResponse {
    var data: MutableList<Post> = mutableListOf()
    var whereClause = ""
    var peopleIds: MutableList<Int> = mutableListOf()
    if (req.token == null) return CommonPostsListResponse(ErrorType.TokenNotFound, mutableListOf(), mutableListOf())
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    if(req.threadId!==null){
      whereClause = "thread = :threadId"
      paramSource.addValue("threadId", req.threadId)
    }

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "common.posts",
        filter = "order by id",
        columns = arrayOf(
          "id",
          "content",
          "thread",
          "created_at",
          "created_by",
          "modified_at",
          "modified_by",
          "owning_person",
          "owning_group",
        ),
        whereClause = whereClause
      )
    ).query

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: Int? = jdbcResult.getInt("id")
        if (jdbcResult.wasNull()) id = null

        var content: String? = jdbcResult.getString("content")
        if (jdbcResult.wasNull()) content = null

        var thread: Int? = jdbcResult.getInt("thread")
        if (jdbcResult.wasNull()) thread = null

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
        peopleIds.add(owning_person?:0)

        var owning_group: Int? = jdbcResult.getInt("owning_group")
        if (jdbcResult.wasNull()) owning_group = null

        data.add(
          Post(
            id = id,
            thread = thread,
            content = content,
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
      return CommonPostsListResponse(ErrorType.SQLReadError, mutableListOf(), mutableListOf())
    }
    val peopleDetails = util.getPeopleDetailsFromIds(peopleIds)

    return CommonPostsListResponse(ErrorType.NoError, data, peopleDetails)
  }
}
