package com.example

import com.example.styling.MyTheme
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations) {
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        static("static") {
            resources("images")
//            files("images")
        }

        get("/demo") {
            call.respondHtml {
                head {
                    title { +"Ktor Tech Demo" }
                    link {
                        rel = "stylesheet"
                        href = "/styles.css"
                    }
                }

                body {
                    header {
                        div("topleft") {
                            +"Demo site"
                            small {
                                +"- Its fun to code"
                            }
                        }
                        nav {
                            ul("navlinks") {
                                li {
                                    a {
                                        href = "#"
                                        +"Machine Learning"
                                    }
                                }
                                li {
                                    a {
                                        href = "#"
                                        +"Scrum process"
                                    }
                                }
                                li {
                                    a {
                                        href = "#"
                                        +"Kotlin"
                                    }
                                }
                            }
                        }
                        div("topright") {
                            +"Innovation is fun"
                        }
                    }


                }

            }
        }
        get("/styles.css") {
            call.respondCss {
                header {
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                    alignItems = Align.center
                    padding = "30px 10%"
                    backgroundColor = MyTheme.navbarbackground
                    border = "2px solid #000"
                    borderRadius = 0.5.em
                    borderColor = Color("#ffaff0")
                    color = MyTheme.textcolor


                }
                html {
                    margin = "0"
                    padding = "0"
                    background = "url('static/art.jpg') no-repeat center center fixed"
                    backgroundSize = "cover"
                    height = LinearDimension("100%")
                    overflow = Overflow.hidden
                    color = MyTheme.textcolor
                }

                body {
                    //                    color = Color.white
                }

                p {
                    fontSize = 2.em
                }

                a {
                    textDecoration = TextDecoration.none
                }

                rule(".topleft") {
                    color = MyTheme.righttop
                    fontSize = 2.em
                    alignContent = Align.flexStart
                }

                rule(".topright") {
                    color = MyTheme.righttop
                    fontSize = 2.em
                    alignContent = Align.center
                }
                rule(".navlinks") {
                    display = Display.inlineBlock
                    listStyleType = ListStyleType.none
                    padding = "0px, 20px"
                    backgroundColor = MyTheme.navItembackground
                }
            }
        }

        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}")
data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

