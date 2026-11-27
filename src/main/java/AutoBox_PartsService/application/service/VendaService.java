package AutoBox_PartsService.application.service;

import AutoBox_PartsService.domain.entity.ItemVenda;
import AutoBox_PartsService.domain.entity.Venda;
import AutoBox_PartsService.domain.exception.EstoqueInsuficienteException;
import AutoBox_PartsService.domain.exception.RecursoNaoEncontradoException;
import AutoBox_PartsService.dto.request.CriarVendaRequest;
import AutoBox_PartsService.dto.response.VendaResponse;
import AutoBox_PartsService.infrastructure.repository.ClienteRepository;
import AutoBox_PartsService.infrastructure.repository.PecaRepository;
import AutoBox_PartsService.infrastructure.repository.VendaRepository;
import AutoBox_PartsService.mapper.VendaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final PecaRepository pecaRepository;
    private final VendaMapper mapper = new VendaMapper();

    public VendaService(VendaRepository vendaRepository, ClienteRepository clienteRepository, PecaRepository pecaRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.pecaRepository = pecaRepository;
    }

    @Transactional
    public VendaResponse criar(CriarVendaRequest request) {
        var venda = new Venda();
        if (request.clienteId() != null) {
            venda.setCliente(clienteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado")));
        }
        for (var itemRequest : request.itens()) {
            var peca = pecaRepository.findById(itemRequest.pecaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
            if (itemRequest.quantidade() <= 0 || peca.getQuantidadeEstoque() == null
                    || peca.getQuantidadeEstoque() < itemRequest.quantidade()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para a peça " + peca.getNome());
            }
            peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - itemRequest.quantidade());
            pecaRepository.save(peca);
            var item = new ItemVenda();
            item.setVenda(venda);
            item.setPeca(peca);
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(peca.getPreco());
            venda.getItens().add(item);
        }
        return mapper.toResponse(vendaRepository.save(venda));
    }
}