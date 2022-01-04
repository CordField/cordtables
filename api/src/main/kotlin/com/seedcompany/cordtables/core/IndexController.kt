package com.seedcompany.cordtables.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import io.swagger.v3.oas.annotations.Hidden

@Hidden
@RestController
class IndexController(

    @Autowired
    var appConfig: AppConfig,
) {
    private var index: String? = null

    internal inner class MetaData {
        var title: String? = null
        var description: String? = null
    }

    init {
        try {

            val resource: Resource = ClassPathResource("static/index.html")
            val inputStream = resource.inputStream

            val s = Scanner(inputStream)
            s.useDelimiter("\\A")
            index = if (s.hasNext()) s.next() else ""
            s.close()
        } catch (ex: Exception) {
            println(ex.toString())
            ex.printStackTrace()
        }
    }

    @RequestMapping(
        value = [
            "/home",
            "/register",
            "/login",
            "/table/{schema}/{table}",
            "/page/{page}",
            "/profile",
            "/email/{token}",
        ]
    )
    @ResponseBody
    fun frontendRoute(request: HttpServletRequest, response: HttpServletResponse?): String? {

        try {
            // get path
            val path = request.servletPath
            val tokens = path.split("/").toTypedArray()
            val noun = tokens[1]

            // title
            val oldTitle = "<title>Cord Field</title>"
            val newTitle = "<title>Cord Field.org</title>"
            index = index?.replace(oldTitle.toRegex(), newTitle)

            // description
            val oldDescription = "Bible Translation Project Management"
            val newDescription = "Bible Translation Rapid Application Development Environment"
            index = index?.replace(oldDescription.toRegex(), newDescription)

            // url
//            val oldUrl = "content=\"https://dev.cordfield.org\""
//            val newUrl = "content=\"https://dev.cordfield.org/home\""
//            return index?.replace(oldUrl.toRegex(), newUrl)

        } catch (ex: Exception) {
            println(ex.toString())
            ex.printStackTrace()
        }
        return index
    }

}
