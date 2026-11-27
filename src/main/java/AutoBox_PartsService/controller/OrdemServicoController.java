package AutoBox_PartsService.controller;

import AutoBox_PartsService.application.service.OrdemServicoService;
import AutoBox_PartsService.dto.request.AdicionarPecaOsRequest;
import AutoBox_PartsService.dto.request.AlterarStatusOsRequest;
import AutoBox_PartsService.dto.request.CriarOrdemServicoRequest;
import AutoBox_PartsService.dto.response.OrdemServicoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {
    private final OrdemServicoService service;

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody CriarOrdemServicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PostMapping("/{id}/pecas")
    public ResponseEntity<OrdemServicoResponse> adicionarPeca(@PathVariable Long id,
                                                               @Valid @RequestBody AdicionarPecaOsRequest request) {
        return ResponseEntity.ok(service.adicionarPeca(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrdemServicoResponse> alterarStatus(@PathVariable Long id,
                                                               @Valid @RequestBody AlterarStatusOsRequest request) {
        return ResponseEntity.ok(service.alterarStatus(id, request));
    }
}