package AutoBox_PartsService.controller;

import AutoBox_PartsService.application.service.PecaService;
import AutoBox_PartsService.dto.request.CriarPecaRequest;
import AutoBox_PartsService.dto.response.PecaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
@RequiredArgsConstructor
public class PecaController {
    private final PecaService pecaService;

    @GetMapping
    public ResponseEntity<List<PecaResponse>> listar() {
        return ResponseEntity.ok(pecaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pecaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PecaResponse> criar(@Valid @RequestBody CriarPecaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pecaService.criar(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pecaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}