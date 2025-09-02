package br.com.SGHSS.VidaPlus.app.controller;


import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import br.com.SGHSS.VidaPlus.app.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prontuarios")
public class ProntuarioController {

    @Autowired private ProntuarioService prontuarioService;

    @PostMapping
    public ResponseEntity<Prontuario> criar(@RequestBody Prontuario p) {
        return ResponseEntity.ok(prontuarioService.criar(p));
    }

    @PostMapping("/{id}/anotacoes")
    public ResponseEntity<Prontuario> adicionarAnotacao(@PathVariable Long id, @RequestParam String anotacao) {
        return ResponseEntity.ok(prontuarioService.adicionarAnotacao(id, anotacao));
    }

    @GetMapping("/{id}/visualizar")
    public ResponseEntity<String> visualizar(@PathVariable Long id) {
        return ResponseEntity.ok(prontuarioService.visualizar(id));
    }
}