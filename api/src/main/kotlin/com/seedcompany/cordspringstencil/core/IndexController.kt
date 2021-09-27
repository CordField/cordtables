package com.seedcompany.cordspringstencil.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.InputStream
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class IndexController(

    @Autowired
    var appConfig: AppConfig,

//  @Autowired
//  var logger: Logger,

//  @Autowired
//  var serverId: Long,
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
            "/table/{table}",
            "/profile",
            "/email/{token}",
        ]
    )
    @ResponseBody
    fun frontendRoute(request: HttpServletRequest, response: HttpServletResponse?): String? {

        try {
//      // get path
//      val path = request.servletPath
//      val tokens = path.split("/").toTypedArray()
//      val noun = tokens[1]
//
//      // replace meta data
//      var oldText: String
//      var newText: String
//      val newIndex1 = index
//
//      // title
//      oldText = "<title>Crowd Altar</title>"
//      newText = "<title>Hi Mom</title>"
//      val newIndex2 = newIndex1!!.replace(oldText.toRegex(), newText)
//
//      // title
//      val oldText2 = "Conversation on Crowd Altar"
//      val newText2 = "Rewriter wuz here"
//      val newIndex3 = newIndex2.replace(oldText2.toRegex(), newText2)
//
//      // description
//      oldText = "Discussion Without Distraction"
//      newText = "DWD"
//      val newIndex4 = newIndex3.replace(oldText.toRegex(), newText)
//
//      // url
//      oldText = "content=\"https://crowdaltar.com\""
//      newText = "asdfasdfasdf"
//      return newIndex4.replace(oldText.toRegex(), newText)

        } catch (ex: Exception) {
            println(ex.toString())
            ex.printStackTrace()
        }
        return index
    }

}