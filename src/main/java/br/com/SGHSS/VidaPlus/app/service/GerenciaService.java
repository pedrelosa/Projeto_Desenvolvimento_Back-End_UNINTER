package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GerenciaService {

    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private AuditService auditService;

    public List<Consulta> listarConsultasPorData(LocalDate data) {
        List<Consulta> res = consultaRepository.findAll().stream()
                .filter(c -> c.getDataHora() != null && c.getDataHora().toLocalDate().equals(data))
                .toList();
        auditService.registrarAcao("LISTAR_CONSULTAS_DATA", "Data=" + data);
        return res;
    }

    // Stub de relatório financeiro
    public BigDecimal gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        // Em produção: somar valores por consulta, convênio, etc.
        // Aqui, devolve um valor fictício baseado em quantidade de consultas no período.
        long qtd = consultaRepository.findAll().stream()
                .filter(c -> c.getDataHora() != null &&
                        !c.getDataHora().toLocalDate().isBefore(inicio) &&
                        !c.getDataHora().toLocalDate().isAfter(fim))
                .count();

        BigDecimal total = BigDecimal.valueOf(qtd).multiply(BigDecimal.valueOf(150.00)); // R$150 por consulta (exemplo)
        auditService.registrarAcao("RELATORIO_FINANCEIRO", "Periodo=" + inicio + " a " + fim + " Total=" + total);
        return total;
    }
}