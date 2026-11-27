package AutoBox_PartsService.application.service;

import AutoBox_PartsService.domain.entity.Peca;
import AutoBox_PartsService.domain.exception.RecursoNaoEncontradoException;
import AutoBox_PartsService.dto.request.CriarPecaRequest;
import AutoBox_PartsService.dto.response.PecaResponse;
import AutoBox_PartsService.infrastructure.repository.PecaRepository;
import AutoBox_PartsService.mapper.PecaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PecaService {
    private final PecaRepository repository;
    private final PecaMapper mapper = new PecaMapper();

    public PecaService(PecaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PecaResponse criar(CriarPecaRequest request) {
        Peca peca = new Peca();
        peca.setNome(request.nome());
        peca.setDescricao(request.descricao());
        peca.setSku("SKU-" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        peca.setQuantidadeEstoque(request.quantidadeEstoque());
        peca.setPreco(request.preco());
        return mapper.toResponse(repository.save(peca));
    }

    public PecaResponse buscarPorId(Long id) {
        Peca peca = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
        return mapper.toResponse(peca);
    }

    public List<PecaResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Peça não encontrada");
        }
        repository.deleteById(id);
    }
}
