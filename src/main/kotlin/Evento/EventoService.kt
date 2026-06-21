package com.est.Evento

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.r2dbc.*
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import com.est.Pavilhao.PavilhaoService
import com.est.Organizador.OrganizadorService
import org.jetbrains.exposed.v1.core.ReferenceOption

@Serializable
sealed class Evento {
    abstract val id: Int?
    abstract val nome: String
    abstract val data: String
    abstract val pavilhaoId: Int
    abstract val organizadorId: Int

    abstract fun minimoJogadoresInscricao(): Int
    abstract fun permiteAgendamentoAutomatico(): Boolean
    abstract fun permiteClassificacao(): Boolean
}

@Serializable
@SerialName("TORNEIO")
data class Torneio(
    override val id: Int? = null,
    override val nome: String,
    override val data: String,
    override val pavilhaoId: Int,
    override val organizadorId: Int
) : Evento() {
    override fun minimoJogadoresInscricao() = 5
    override fun permiteAgendamentoAutomatico() = true
    override fun permiteClassificacao() = true
}

@Serializable
@SerialName("AMIGAVEL")
data class Amigavel(
    override val id: Int? = null,
    override val nome: String,
    override val data: String,
    override val pavilhaoId: Int,
    override val organizadorId: Int
) : Evento() {
    override fun minimoJogadoresInscricao() = 0
    override fun permiteAgendamentoAutomatico() = false
    override fun permiteClassificacao() = false
}

@Serializable
@SerialName("CLINICA_TREINO")
data class ClinicaDeTreino(
    override val id: Int? = null,
    override val nome: String,
    override val data: String,
    override val pavilhaoId: Int,
    override val organizadorId: Int,
    val tematica: String
) : Evento() {
    override fun minimoJogadoresInscricao() = 0
    override fun permiteAgendamentoAutomatico() = false
    override fun permiteClassificacao() = false
}

class EventoService(val database: R2dbcDatabase) {

    object Eventos : Table("eventos") {
        val id = integer("id").autoIncrement()
        val nome = varchar("nome", 100)
        val data = varchar("data", 20)
        val tipo = varchar("tipo", 20) // discriminador: TORNEIO, AMIGAVEL, CLINICA_TREINO
        val tematica = varchar("tematica", 100).nullable() // só usado pela ClinicaDeTreino

        // Foreign Keys reais para a base de dados
        val pavilhaoId = reference("pavilhao_id", PavilhaoService.Pavilhoes.id, onDelete = ReferenceOption.CASCADE)
        val organizadorId = reference("organizador_id", OrganizadorService.Organizadores.id, onDelete = ReferenceOption.CASCADE)

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(Eventos)
        }
    }

    // Reconstrói o subtipo correto a partir do discriminador guardado em BD
    private fun construirEvento(
        id: Int,
        nome: String,
        data: String,
        tipo: String,
        pavilhaoId: Int,
        organizadorId: Int,
        tematica: String?
    ): Evento = when (tipo) {
        "TORNEIO" -> Torneio(id, nome, data, pavilhaoId, organizadorId)
        "AMIGAVEL" -> Amigavel(id, nome, data, pavilhaoId, organizadorId)
        "CLINICA_TREINO" -> ClinicaDeTreino(id, nome, data, pavilhaoId, organizadorId, tematica ?: "")
        else -> throw IllegalStateException("Tipo de evento desconhecido na base de dados: $tipo")
    }

    private fun tipoDoEvento(evento: Evento): String = when (evento) {
        is Torneio -> "TORNEIO"
        is Amigavel -> "AMIGAVEL"
        is ClinicaDeTreino -> "CLINICA_TREINO"
    }

    suspend fun criarEvento(dados: Evento) {
        suspendTransaction(database) {
            Eventos.insert {
                it[nome] = dados.nome
                it[data] = dados.data
                it[tipo] = tipoDoEvento(dados)
                it[pavilhaoId] = dados.pavilhaoId
                it[organizadorId] = dados.organizadorId
                it[tematica] = (dados as? ClinicaDeTreino)?.tematica
            }
        }
    }

    suspend fun lerEventos(): List<Evento> {
        return suspendTransaction(database) {
            Eventos.selectAll().map {
                construirEvento(
                    id = it[Eventos.id],
                    nome = it[Eventos.nome],
                    data = it[Eventos.data],
                    tipo = it[Eventos.tipo],
                    pavilhaoId = it[Eventos.pavilhaoId],
                    organizadorId = it[Eventos.organizadorId],
                    tematica = it[Eventos.tematica]
                )
            }.toList()
        }
    }

    suspend fun lerEventoPorId(id: Int): Evento? {
        return suspendTransaction(database) {
            Eventos.selectAll()
                .where { Eventos.id eq id }
                .map {
                    construirEvento(
                        id = it[Eventos.id],
                        nome = it[Eventos.nome],
                        data = it[Eventos.data],
                        tipo = it[Eventos.tipo],
                        pavilhaoId = it[Eventos.pavilhaoId],
                        organizadorId = it[Eventos.organizadorId],
                        tematica = it[Eventos.tematica]
                    )
                }.singleOrNull()
        }
    }

    suspend fun atualizarEvento(id: Int, dados: Evento) {
        suspendTransaction(database) {
            Eventos.update({ Eventos.id eq id }) {
                it[nome] = dados.nome
                it[data] = dados.data
                it[tipo] = tipoDoEvento(dados)
                it[pavilhaoId] = dados.pavilhaoId
                it[organizadorId] = dados.organizadorId
                it[tematica] = (dados as? ClinicaDeTreino)?.tematica
            }
        }
    }

    suspend fun removerEvento(id: Int) {
        suspendTransaction(database) {
            Eventos.deleteWhere { Eventos.id eq id }
        }
    }
}