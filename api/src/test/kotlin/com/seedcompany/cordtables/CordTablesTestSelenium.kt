package com.seedcompany.cordtables

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.RemoteWebElement
import java.util.concurrent.TimeUnit

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CordTablesTestSelenium {

  private var driver: WebDriver? = null
  private val url = "http://localhost:3333"

  private fun shutdown() {
    driver!!.quit()
  }

  private fun expandRootElement(shadowHost: WebElement?): WebElement {
    val returnObj: WebElement?
    when (val shadowRoot = (driver as JavascriptExecutor).executeScript("return arguments[0].shadowRoot", shadowHost)) {
      is WebElement -> {
        returnObj = shadowRoot
      }
      is Map<*, *> -> {
        val id = shadowRoot[shadowRoot.keys.toTypedArray()[0]] as String?
        val remoteWebElement = RemoteWebElement()
        remoteWebElement.setParent(driver as RemoteWebDriver?)
        remoteWebElement.id = id
        returnObj = remoteWebElement
      }
      else -> {
        throw Error("Unexpected return type for shadowRoot in expandRootElement()")
      }
    }
    return returnObj
  }

  private fun openMenuAndClickButton(driver: WebDriver, action: String): WebElement? {
    val appRoot = driver.findElement(By.tagName("app-root"))
    val shadowRoot = expandRootElement(appRoot)
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

    val cfHeader = shadowRoot.findElement(By.tagName("cf-header"))
    val shadowRoot2 = expandRootElement(cfHeader)

    //click menu
    shadowRoot2.findElement(By.id("header"))?.findElement(By.id("menu-button"))?.click()
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

    var cfHeaderMenu = shadowRoot2.findElement(By.tagName("cf-header-menu"))
    cfHeaderMenu = expandRootElement(cfHeaderMenu).findElement(By.id("header-menu"))

    //click button by action
    cfHeaderMenu?.findElement(By.xpath("button[@class='menu-item'][.='$action']"))?.click()
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

    return shadowRoot.findElement(By.id("root-wrap-outer"))?.findElement(By.id("root-wrap-inner"))
  }

  private fun fillFormAndSendValues(action: String, rootWrap: WebElement, email: String, password: String) {
    val cf = rootWrap.findElement(By.xpath("main/stencil-router/stencil-route-switch/stencil-route/cf-$action"))
    val form = expandRootElement(cf).findElement(By.tagName("form"))
    val emailField = form?.findElement(By.xpath("div[@id='email-holder']/input[@id='email']"))
    val passwordField = form?.findElement(By.xpath("div[@id='password-holder']/input[@id='password']"))

    emailField?.sendKeys(email)
    passwordField?.sendKeys(password, Keys.RETURN)
  }

  private fun generateRandomString(): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..10)
      .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
      .map(charPool::get)
      .joinToString("")
  }

  private fun init() {
    WebDriverManager.chromedriver().setup()
    driver = ChromeDriver()
    driver!!.get(url)
  }

  private fun login(email: String, password: String) {
    val rootWrap = openMenuAndClickButton(driver!!, "Login")
    fillFormAndSendValues("login", rootWrap!!, email, password)

    println("Logged In")
    Thread.sleep(2000)
  }

  private fun logout() {
    openMenuAndClickButton(driver!!, "Logout")
    println("Logged Out")
    Thread.sleep(2000)
  }

  private fun register(email: String, password: String) {
    val rootWrap = openMenuAndClickButton(driver!!, "Register")
    fillFormAndSendValues("register", rootWrap!!, email, password )
    println("Registered User")
    Thread.sleep(2000)
  }

//  @Test
//  fun testLogin() {
//    init()
//    login("devops@tsco.org", "asdfasdf")
//    shutdown()
//  }

//  @Test
//  fun testLogout() {
//    init()
//    logout()
//    shutdown()
//  }
//
//  @Test
//  fun testRegister() {
//    init()
//    register()
//    shutdown()
//  }

  @Test
  fun testAll() {
    init()
    val randomString = generateRandomString()
    register("$randomString@mail.com", randomString)
    logout()
    login("$randomString@mail.com", randomString)
    logout()
    shutdown()
  }
}





