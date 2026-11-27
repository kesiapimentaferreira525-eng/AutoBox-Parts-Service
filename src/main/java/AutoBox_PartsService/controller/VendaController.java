package AutoBox_PartsService.controller;

import AutoBox_PartsService.application.service.VendaService;
import AutoBox_PartsService.dto.request.CriarVendaRequest;
import AutoBox_PartsService.dto.response.VendaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {
    private final VendaService service;

    @PostMapping
    public ResponseEntity<VendaResponse> criar(@Valid @RequestBody CriarVendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }
}