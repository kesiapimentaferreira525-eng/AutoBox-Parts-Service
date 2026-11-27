package AutoBox_PartsService.application.service;

import AutoBox_PartsService.domain.entity.ItemOrdemServico;
import AutoBox_PartsService.domain.entity.OrdemServico;
import AutoBox_PartsService.domain.exception.EstoqueInsuficienteException;
import AutoBox_PartsService.domain.exception.RecursoNaoEncontradoException;
import AutoBox_PartsService.domain.exception.RegraNegocioException;
import AutoBox_PartsService.dto.request.AdicionarPecaOsRequest;
import AutoBox_PartsService.dto.request.AlterarStatusOsRequest;
import AutoBox_PartsService.dto.request.CriarOrdemServicoRequest;
import AutoBox_PartsService.dto.response.OrdemServicoResponse;
import AutoBox_PartsService.infrastructure.repository.ClienteRepository;
import AutoBox_PartsService.infrastructure.repository.OrdemServicoRepository;
import AutoBox_PartsService.infrastructure.repository.PecaRepository;
import AutoBox_PartsService.infrastructure.repository.VeiculoRepository;
import AutoBox_PartsService.mapper.OrdemServicoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemServicoService {
    private final OrdemServicoRepository ordemRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final PecaRepository pecaRepository;
    private final OrdemServicoMapper mapper = new OrdemServicoMapper();

    public OrdemServicoService(OrdemServicoRepository ordemRepository, ClienteRepository clienteRepository,
                               VeiculoRepository veiculoRepository, PecaRepository pecaRepository) {
        this.ordemRepository = ordemRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.pecaRepository = pecaRepository;
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponse> listar() {
        return ordemRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse buscar(Long id) {
        var ordem = ordemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
        return mapper.toResponse(ordem);
    }

    @Transactional
    public OrdemServicoResponse criar(CriarOrdemServicoRequest request) {
        var ordem = new OrdemServico();
        var cliente = clienteRepository.findById(request.clienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
        ordem.setCliente(cliente);
        ordem.setNumeroOs("OS-" + System.currentTimeMillis());
        var veiculo = veiculoRepository.findById(request.veiculoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
        ordem.setVeiculo(veiculo);
        ordem.setDescricao(request.descricao());
        return mapper.toResponse(ordemRepository.save(ordem));
    }

    @Transactional
    public OrdemServicoResponse alterarStatus(Long id, AlterarStatusOsRequest request) {
        var ordem = ordemRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
        if (!transicaoPermitida(ordem.getStatus(), request.status())) {
            throw new RegraNegocioException("Transição inválida de status: " + ordem.getStatus() + " -> " + request.status());
        }
        if (request.status() == AutoBox_PartsService.domain.enums.StatusOrdemServico.CANCELADA
                && Boolean.TRUE.equals(ordem.getEstoqueBaixado())) {
            ordem.getItens().forEach(item -> {
                var peca = item.getPeca();
                peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() + item.getQuantidade());
                pecaRepository.save(peca);
            });
            ordem.setEstoqueBaixado(false);
        }
        ordem.setStatus(request.status());
        if (request.status() == AutoBox_PartsService.domain.enums.StatusOrdemServico.CONCLUIDA) {
            ordem.setDataConclusao(LocalDateTime.now());
        }
        return mapper.toResponse(ordemRepository.save(ordem));
    }

    @Transactional
    public OrdemServicoResponse adicionarPeca(Long id, AdicionarPecaOsRequest request) {
        var ordem = ordemRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
        if (ordem.getStatus() == AutoBox_PartsService.domain.enums.StatusOrdemServico.CONCLUIDA
                || ordem.getStatus() == AutoBox_PartsService.domain.enums.StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Não é possível adicionar peças a uma ordem finalizada");
        }
        var peca = pecaRepository.findById(request.pecaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
        if (peca.getQuantidadeEstoque() == null || peca.getQuantidadeEstoque() < request.quantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente");
        }
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - request.quantidade());
        pecaRepository.save(peca);

        var item = new ItemOrdemServico();
        item.setOrdemServico(ordem);
        item.setPeca(peca);
        item.setQuantidade(request.quantidade());
        item.setPrecoUnitario(peca.getPreco());
        ordem.getItens().add(item);
        ordem.setEstoqueBaixado(true);
        var valorPecas = ordem.getItens().stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ordem.setValorTotal(ordem.getValorMaoDeObra().add(valorPecas));
        return mapper.toResponse(ordemRepository.save(ordem));
    }

    private boolean transicaoPermitida(AutoBox_PartsService.domain.enums.StatusOrdemServico atual,
                                       AutoBox_PartsService.domain.enums.StatusOrdemServico proximo) {
        return switch (atual) {
            case ABERTA -> proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.EM_ANDAMENTO
                    || proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.AGUARDANDO_PECA
                    || proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.CANCELADA;
            case EM_ANDAMENTO -> proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.AGUARDANDO_PECA
                    || proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.CONCLUIDA
                    || proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.CANCELADA;
            case AGUARDANDO_PECA -> proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.EM_ANDAMENTO
                    || proximo == AutoBox_PartsService.domain.enums.StatusOrdemServico.CANCELADA;
            case CONCLUIDA, CANCELADA -> false;
        };
    }
}
