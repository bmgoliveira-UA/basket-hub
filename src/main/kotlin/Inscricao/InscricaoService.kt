package com.est.Inscricao

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.r2dbc.*
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import com.est.Equipa.EquipaService
import com.est.Evento.EventoService
import org.jetbrains.exposed.v1.core.ReferenceOption
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.DateTimeFormatterBuilder


@Serializable
data class Inscricao(
    val id: Int? = null,
    val equipaId: Int,
    val eventoId: Int,
    val dataInscricao: String
) {
    init {
        // Criamos um formatador flexível que aceita tanto hifens quanto barras
        val formatoFlexivel = DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[yyyy/MM/dd][yyyy-MM-dd]"))
            .toFormatter()

        require(isValidDate(dataInscricao, formatoFlexivel)) {
            "A dataInscricao deve estar no formato 'yyyy/MM/dd' ou 'yyyy-MM-dd'. Valor recebido: $dataInscricao"
        }
    }
}

private fun isValidDate(dateStr: String, formatter: DateTimeFormatter): Boolean {
    return try {
        formatter.parse(dateStr)
        true
    } catch (e: DateTimeParseException) {
        false
    }
}

class InscricaoService(val database: R2dbcDatabase) {

    object Inscricoes : Table("inscricoes") {
        val id = integer("id").autoIncrement()

        // Foreign Keys obrigando a existir a Equipa e o Evento
        val equipaId = reference("equipa_id", EquipaService.Equipas.id, onDelete = ReferenceOption.CASCADE)
        val eventoId = reference("evento_id", EventoService.Eventos.id, onDelete = ReferenceOption.CASCADE)
        val dataInscricao = varchar("data_inscricao", 20)

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(Inscricoes)
        }
    }

    suspend fun criarInscricao(dados: Inscricao) {
        suspendTransaction(database) {
            Inscricoes.insert {
                it[equipaId] = dados.equipaId
                it[eventoId] = dados.eventoId
                it[dataInscricao] = dados.dataInscricao
            }
        }
    }

    suspend fun lerInscricoes(): List<Inscricao> {
        return suspendTransaction(database) {
            Inscricoes.selectAll().map {
                Inscricao(
                    id = it[Inscricoes.id],
                    equipaId = it[Inscricoes.equipaId],
                    eventoId = it[Inscricoes.eventoId],
                    dataInscricao = it[Inscricoes.dataInscricao]
                )
            }.toList()
        }
    }

    suspend fun lerInscricaoPorId(id: Int): Inscricao? {
        return suspendTransaction(database) {
            Inscricoes.selectAll()
                .where { Inscricoes.id eq id }
                .map {
                    Inscricao(
                        id = it[Inscricoes.id],
                        equipaId = it[Inscricoes.equipaId],
                        eventoId = it[Inscricoes.eventoId],
                        dataInscricao = it[Inscricoes.dataInscricao]
                    )
                }.singleOrNull()
        }
    }

    suspend fun atualizarInscricao(id: Int, dados: Inscricao) {
        suspendTransaction(database) {
            Inscricoes.update({ Inscricoes.id eq id }) {
                it[equipaId] = dados.equipaId
                it[eventoId] = dados.eventoId
                it[dataInscricao] = dados.dataInscricao
            }
        }
    }

    suspend fun removerInscricao(id: Int) {
        suspendTransaction(database) {
            Inscricoes.deleteWhere { Inscricoes.id eq id }
        }
    }
}