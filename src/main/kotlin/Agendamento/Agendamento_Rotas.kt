package com.est.Agendamento

import com.est.DBUtils.DatabaseCreation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.Agendamento_Rotas() {
    val service = DatabaseCreation.AgendamentoService
    val jogoService = DatabaseCreation.JogoService

    routing {
        post("/agendamento/{id}") {
            try {
                val eventoId = call.parameters["id"]?.toIntOrNull()
                if (eventoId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de evento inválido")
                    return@post
                }
                val resultado = service.gerarOuAvancarChaveamento(eventoId)
                call.respond(HttpStatusCode.OK, resultado)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Erro ao gerar agendamento")
            }
        }

        get("/agendamento/{id}") {
            try {
                val eventoId = call.parameters["id"]?.toIntOrNull()
                if (eventoId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de evento inválido")
                    return@get
                }
                call.respond(HttpStatusCode.OK, jogoService.lerJogosPorEvento(eventoId))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Erro ao ler chaveamento")
            }
        }
    }
}