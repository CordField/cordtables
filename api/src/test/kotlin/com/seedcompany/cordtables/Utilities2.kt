package com.seedcompany.cordtables

import com.seedcompany.cordtables.components.user.LoginRequest
import com.seedcompany.cordtables.components.user.LoginReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class Utilities2 (
  @Autowired
  val rest: TestRestTemplate,

  ){



  fun asdf(){
    println("hey")
  }

}
