package AutoBox_PartsService.controller;

import AutoBox_PartsService.domain.entity.Veiculo;
import AutoBox_PartsService.domain.exception.RecursoNaoEncontradoException;
import AutoBox_PartsService.dto.response.VeiculoResponse;
import AutoBox_PartsService.infrastructure.repository.ClienteRepository;
import AutoBox_PartsService.infrastructure.repository.VeiculoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
public class VeiculoController {
    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;

    @GetMapping
    public List<VeiculoResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public VeiculoResponse buscar(@PathVariable Long id) {
        return toResponse(buscarEntidade(id));
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> criar(@Valid @RequestBody Veiculo veiculo) {
        veiculo.setCliente(clienteRepository.findById(veiculo.getCliente().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado")));
        Veiculo salvo = repository.save(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(buscarEntidade(salvo.getId())));
    }

    @PutMapping("/{id}")
    public VeiculoResponse atualizar(@PathVariable Long id, @Valid @RequestBody Veiculo dados) {
        Veiculo veiculo = buscarEntidade(id);
        veiculo.setPlaca(dados.getPlaca());
        veiculo.setMarca(dados.getMarca());
        veiculo.setModelo(dados.getModelo());
        veiculo.setAno(dados.getAno());
        veiculo.setQuilometragem(dados.getQuilometragem());
        veiculo.setCliente(clienteRepository.findById(dados.getCliente().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado")));
        repository.save(veiculo);
        return toResponse(buscarEntidade(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RecursoNaoEncontradoException("Veículo não encontrado");
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Veiculo buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
    }

    private VeiculoResponse toResponse(Veiculo veiculo) {
        var cliente = veiculo.getCliente();
        return new VeiculoResponse(veiculo.getId(), veiculo.getPlaca(), veiculo.getMarca(),
                veiculo.getModelo(), veiculo.getAno(), veiculo.getQuilometragem(),
                new VeiculoResponse.ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getTelefone()));
    }
}
