package com.seedcompany.cordspringstencil.components.tables.globalrolecolumngrants
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class LoginReturn(
        val id : Int? = null,
        val email: String? = null,
        val token: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org"])
@Controller()
class readAll(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
){

    val encoder = Argon2PasswordEncoder(16, 32, 1, 4096, 3)

    @PostMapping(path=["table/global-role-column-grants"], consumes = ["application/json"], produces = ["application/json"])

    @ResponseBody
    fun LoginHandler():LoginReturn{

        var response = LoginReturn()

        this.ds.connection.use { conn ->
            val pashStatement = conn.prepareCall("select id, email from public.users")
            //pashStatement.setString(1, req.email)

            val getPashResult = pashStatement.executeQuery()

            var pash: String? = null

            if (getPashResult.next()) {
                pash = getPashResult.getString("email");
                print(pash);

            } else {
                println("pash not found")
            }
        }

        return response
    }
}