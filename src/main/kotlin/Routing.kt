package com.est

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.ThymeleafContent
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    routing {
        // Rota principal
        get("/") {
            call.respond(ThymeleafContent("index", emptyMap()))
        }

        // Rota dinâmica protegida
        get("/{template}") {
            val template = call.parameters["template"] ?: "index"
            val perfil = call.request.queryParameters["perfil"] ?: "PUBLICO"

            // Verifica se o ficheiro HTML existe na pasta de recursos
            val resourcePath = "templates/thymeleaf/$template.html"
            val resourceExists = this@configureRouting.javaClass.classLoader.getResource(resourcePath) != null

            if (resourceExists) {
                call.respond(ThymeleafContent(template, mapOf("perfil" to perfil)))
            } else {
                // Se o ficheiro não existir, responde com 404 de forma limpa
                call.respond(HttpStatusCode.NotFound, "A página '$template' não foi encontrada no servidor.")
            }
        }

        // Ficheiros estáticos (CSS, JS)
        staticResources("/static", "static")
    }
}