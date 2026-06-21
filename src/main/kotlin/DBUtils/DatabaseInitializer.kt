package com.est.Database

import com.est.Equipa.Equipa
import com.est.Equipa.EquipaService
import com.est.Estatisticas.Estatisticas
import com.est.Estatisticas.EstatisticasService
import com.est.Evento.Amigavel
import com.est.Evento.ClinicaDeTreino
import com.est.Evento.EventoService
import com.est.Evento.Torneio
import com.est.Inscricao.Inscricao
import com.est.Inscricao.InscricaoService
import com.est.Jogador.Jogador
import com.est.Jogador.JogadorService
import com.est.Jogo.Jogo
import com.est.Jogo.JogoService
import com.est.Organizador.Organizador
import com.est.Organizador.OrganizadorService
import com.est.Patrocinador.Patrocinador
import com.est.Patrocinador.PatrocinadorService
import com.est.Pavilhao.Pavilhao
import com.est.Pavilhao.PavilhaoService

object DatabaseInitializer {

    suspend fun init(
        equipaService: EquipaService,
        jogadorService: JogadorService,
        organizadorService: OrganizadorService,
        pavilhaoService: PavilhaoService,
        eventoService: EventoService,
        inscricaoService: InscricaoService,
        jogoService: JogoService,
        estatisticasService: EstatisticasService,
        patrocinadorService: PatrocinadorService
    ) {
        // Usa a tabela de equipas como sentinela — se já tiver dados, não repopula
        if (equipaService.lerEquipas().isNotEmpty()) {
            println("Base de dados já populada. A saltar seed.")
            return
        }

        println("A popular base de dados...")

        // ----------------------------------------------------------------
        // 1. PAVILHÕES (sem dependências)
        // ----------------------------------------------------------------
        pavilhaoService.criarPavilhao(Pavilhao(nome = "Pavilhão Municipal de Aveiro",   cidade = "Aveiro",   localizacao = "Rua do Desporto, 1",      capacidade = 3000))
        pavilhaoService.criarPavilhao(Pavilhao(nome = "Pavilhão do Porto",              cidade = "Porto",    localizacao = "Avenida dos Aliados, 45", capacidade = 5000))
        pavilhaoService.criarPavilhao(Pavilhao(nome = "Pavilhão de Coimbra",            cidade = "Coimbra",  localizacao = "Praça da República, 10",  capacidade = 2500))
        // IDs esperados: 1, 2, 3

        // ----------------------------------------------------------------
        // 2. ORGANIZADORES (sem dependências)
        // ----------------------------------------------------------------
        organizadorService.criarOrganizador(Organizador(nome = "João Silva",    contacto = "912345678", email = "joao.silva@basket.pt"))
        organizadorService.criarOrganizador(Organizador(nome = "Maria Santos",  contacto = "923456789", email = "maria.santos@basket.pt"))
        organizadorService.criarOrganizador(Organizador(nome = "Carlos Ferreira", contacto = "934567890", email = "carlos.f@basket.pt"))
        // IDs esperados: 1, 2, 3

        // ----------------------------------------------------------------
        // 3. EQUIPAS (sem dependências)
        // ----------------------------------------------------------------
        equipaService.criarEquipa(Equipa(nome = "Wildcats de Aveiro",    sigla = "WCA", localidade = "Aveiro"))
        equipaService.criarEquipa(Equipa(nome = "Eagles do Porto",       sigla = "EDP", localidade = "Porto"))
        equipaService.criarEquipa(Equipa(nome = "Bulls de Coimbra",      sigla = "BDC", localidade = "Coimbra"))
        equipaService.criarEquipa(Equipa(nome = "Sharks de Braga",       sigla = "SDB", localidade = "Braga"))
        equipaService.criarEquipa(Equipa(nome = "Lions de Lisboa",       sigla = "LDL", localidade = "Lisboa"))
        equipaService.criarEquipa(Equipa(nome = "Tigers de Setúbal",     sigla = "TDS", localidade = "Setúbal"))
        // IDs esperados: 1..6

        // ----------------------------------------------------------------
        // 4. JOGADORES (sem dependências)
        // ----------------------------------------------------------------
        // Wildcats de Aveiro (equipa 1)
        jogadorService.criarJogador(Jogador(nome = "Rui Monteiro",     posicao = "Base",          n_camisola = 5))
        jogadorService.criarJogador(Jogador(nome = "André Costa",      posicao = "Extremo",       n_camisola = 7))
        jogadorService.criarJogador(Jogador(nome = "Pedro Neves",      posicao = "Poste",         n_camisola = 11))
        jogadorService.criarJogador(Jogador(nome = "Tiago Rocha",      posicao = "Extremo-Poste", n_camisola = 14))
        jogadorService.criarJogador(Jogador(nome = "Diogo Alves",      posicao = "Base",          n_camisola = 3))

        // Eagles do Porto (equipa 2)
        jogadorService.criarJogador(Jogador(nome = "Bruno Pinto",      posicao = "Base",          n_camisola = 1))
        jogadorService.criarJogador(Jogador(nome = "Nuno Cardoso",     posicao = "Extremo",       n_camisola = 9))
        jogadorService.criarJogador(Jogador(nome = "Fábio Lopes",      posicao = "Poste",         n_camisola = 13))
        jogadorService.criarJogador(Jogador(nome = "Ricardo Sousa",    posicao = "Extremo-Poste", n_camisola = 21))
        jogadorService.criarJogador(Jogador(nome = "Gonçalo Matos",    posicao = "Extremo",       n_camisola = 6))

        // Bulls de Coimbra (equipa 3)
        jogadorService.criarJogador(Jogador(nome = "Miguel Faria",     posicao = "Base",          n_camisola = 4))
        jogadorService.criarJogador(Jogador(nome = "Luís Tavares",     posicao = "Poste",         n_camisola = 15))
        jogadorService.criarJogador(Jogador(nome = "Sérgio Mendes",    posicao = "Extremo",       n_camisola = 8))
        jogadorService.criarJogador(Jogador(nome = "Hugo Ferreira",    posicao = "Extremo-Poste", n_camisola = 22))
        jogadorService.criarJogador(Jogador(nome = "Vasco Lima",       posicao = "Base",          n_camisola = 2))

        // Sharks de Braga (equipa 4)
        jogadorService.criarJogador(Jogador(nome = "Afonso Cruz",      posicao = "Poste",         n_camisola = 33))
        jogadorService.criarJogador(Jogador(nome = "Bernardo Reis",    posicao = "Base",          n_camisola = 10))
        jogadorService.criarJogador(Jogador(nome = "Cláudio Baptista", posicao = "Extremo",       n_camisola = 17))
        jogadorService.criarJogador(Jogador(nome = "Dário Moreira",    posicao = "Extremo-Poste", n_camisola = 20))
        jogadorService.criarJogador(Jogador(nome = "Eduardo Santos",   posicao = "Extremo",       n_camisola = 12))
        // IDs jogadores: 1..20

        // ----------------------------------------------------------------
        // 5. EVENTOS (dependem de pavilhão + organizador)
        // ----------------------------------------------------------------
        // Torneio principal — 4 equipas inscritas → chaveamento de 2 rondas
        eventoService.criarEvento(Torneio(
            nome = "Torneio Primavera 2026",
            data = "2026-04-15",
            pavilhaoId = 1,
            organizadorId = 1
        ))
        // Torneio secundário (sem jogos ainda, para testar agendamento)
        eventoService.criarEvento(Torneio(
            nome = "Torneio Verão 2026",
            data = "2026-07-20",
            pavilhaoId = 2,
            organizadorId = 2
        ))
        // Amigável
        eventoService.criarEvento(Amigavel(
            nome = "Amigável Porto vs Coimbra",
            data = "2026-05-10",
            pavilhaoId = 2,
            organizadorId = 2
        ))
        // Clínica de treino
        eventoService.criarEvento(ClinicaDeTreino(
            nome = "Clínica de Defesa em Zona",
            data = "2026-06-01",
            pavilhaoId = 3,
            organizadorId = 3,
            tematica = "Defesa em zona press"
        ))
        // IDs eventos: 1 (Torneio Primavera), 2 (Torneio Verão), 3 (Amigável), 4 (Clínica)

        // ----------------------------------------------------------------
        // 6. INSCRIÇÕES (dependem de equipa + evento)
        //    Torneio Primavera (eventoId=1): 4 equipas → 2 jogos na ronda 1
        //    Amigável (eventoId=3): Eagles vs Bulls
        // ----------------------------------------------------------------
        inscricaoService.criarInscricao(Inscricao(equipaId = 1, eventoId = 1, dataInscricao = "2026-03-01"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 2, eventoId = 1, dataInscricao = "2026-03-02"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 3, eventoId = 1, dataInscricao = "2026-03-03"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 4, eventoId = 1, dataInscricao = "2026-03-04"))

        // Torneio Verão (eventoId=2): 4 equipas inscritas mas sem jogos gerados ainda
        inscricaoService.criarInscricao(Inscricao(equipaId = 3, eventoId = 2, dataInscricao = "2026-05-01"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 4, eventoId = 2, dataInscricao = "2026-05-02"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 5, eventoId = 2, dataInscricao = "2026-05-03"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 6, eventoId = 2, dataInscricao = "2026-05-04"))

        // Amigável (eventoId=3)
        inscricaoService.criarInscricao(Inscricao(equipaId = 2, eventoId = 3, dataInscricao = "2026-04-20"))
        inscricaoService.criarInscricao(Inscricao(equipaId = 3, eventoId = 3, dataInscricao = "2026-04-20"))

        // ----------------------------------------------------------------
        // 7. JOGOS (dependem de evento + equipa + pavilhão)
        //    Torneio Primavera — Ronda 1 concluída com resultados
        //    para que o AgendamentoService possa avançar para a final
        // ----------------------------------------------------------------
        // Ronda 1, Jogo 1: Wildcats vs Eagles  → Wildcats vencem 85-72
        jogoService.criarJogo(Jogo(
            eventoId = 1, ronda = 1,
            equipaCasaId = 1, equipaForaId = 2,
            pavilhaoId = 1,
            pontosCasa = 85, pontosFora = 72,
            dataHora = "2026-04-15T10:00"
        ))
        // Ronda 1, Jogo 2: Bulls vs Sharks  → Bulls vencem 90-78
        jogoService.criarJogo(Jogo(
            eventoId = 1, ronda = 1,
            equipaCasaId = 3, equipaForaId = 4,
            pavilhaoId = 1,
            pontosCasa = 90, pontosFora = 78,
            dataHora = "2026-04-15T12:00"
        ))

        // Amigável (eventoId=3) — jogo único com resultado
        jogoService.criarJogo(Jogo(
            eventoId = 3, ronda = 1,
            equipaCasaId = 2, equipaForaId = 3,
            pavilhaoId = 2,
            pontosCasa = 68, pontosFora = 74,
            dataHora = "2026-05-10T18:00"
        ))

        // ----------------------------------------------------------------
        // 8. ESTATÍSTICAS (dependem de jogador + equipa)
        // ----------------------------------------------------------------
        // Alguns jogadores dos Wildcats (equipa 1)
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 1,  equipaId = 1, pontos = 22, assistencias = 8,  rebounds = 3,  estado = "Ativo"))
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 2,  equipaId = 1, pontos = 18, assistencias = 4,  rebounds = 6,  estado = "Ativo"))
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 3,  equipaId = 1, pontos = 15, assistencias = 2,  rebounds = 11, estado = "Ativo"))

        // Alguns jogadores dos Eagles (equipa 2)
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 6,  equipaId = 2, pontos = 25, assistencias = 7,  rebounds = 2,  estado = "Ativo"))
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 7,  equipaId = 2, pontos = 14, assistencias = 3,  rebounds = 5,  estado = "Ativo"))

        // Alguns jogadores dos Bulls (equipa 3)
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 11, equipaId = 3, pontos = 20, assistencias = 6,  rebounds = 4,  estado = "Ativo"))
        estatisticasService.registarMovimento(Estatisticas(jogadorId = 12, equipaId = 3, pontos = 12, assistencias = 1,  rebounds = 14, estado = "Ativo"))

        // ----------------------------------------------------------------
        // 9. PATROCINADORES (dependem opcionalmente de evento)
        // ----------------------------------------------------------------
        patrocinadorService.criarPatrocinador(Patrocinador(nome = "SportCash",   empresa = "SportCash Lda",    valorContrato = 5000.0,  eventoId = 1))
        patrocinadorService.criarPatrocinador(Patrocinador(nome = "BallBrands",  empresa = "BallBrands SA",    valorContrato = 3500.0,  eventoId = 1))
        patrocinadorService.criarPatrocinador(Patrocinador(nome = "HoopEnergy",  empresa = "HoopEnergy Corp",  valorContrato = 2000.0,  eventoId = 2))
        patrocinadorService.criarPatrocinador(Patrocinador(nome = "NetWear",     empresa = "NetWear Portugal", valorContrato = 10000.0, eventoId = null)) // patrocinador geral

        println("Base de dados populada com sucesso!")
    }
}