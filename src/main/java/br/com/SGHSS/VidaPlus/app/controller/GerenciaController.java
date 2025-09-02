package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.service.GerenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/gerencia")
public class GerenciaController {

    @Autowired private GerenciaService gerenciaService;

    @GetMapping("/consultas")
    public ResponseEntity<List<Consulta>> listarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(gerenciaService.listarConsultasPorData(data));
    }

    @GetMapping("/relatorio-financeiro")
    public ResponseEntity<BigDecimal> relatorioFinanceiro(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(gerenciaService.gerarRelatorioFinanceiro(inicio, fim));
    }
}