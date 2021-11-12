package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class IsAdminRequest(
    val token: String
)

data class IsAdminResponse(
    val error: ErrorType,
    val isAdmin: Boolean? = null,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller()
class IsAdmin(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("user/is_admin")
    @ResponseBody
    fun isAdminHandler(@RequestBody req: IsAdminRequest): IsAdminResponse {
        if (util.isAdmin(req.token)) return IsAdminResponse(ErrorType.NoError,true);
        else return IsAdminResponse(ErrorType.NoError, false);
    }
}


