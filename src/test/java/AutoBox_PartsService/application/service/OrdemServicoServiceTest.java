package AutoBox_PartsService.application.service;

import AutoBox_PartsService.domain.entity.Cliente;
import AutoBox_PartsService.domain.entity.OrdemServico;
import AutoBox_PartsService.domain.entity.Peca;
import AutoBox_PartsService.domain.entity.Veiculo;
import AutoBox_PartsService.domain.enums.StatusOrdemServico;
import AutoBox_PartsService.domain.exception.EstoqueInsuficienteException;
import AutoBox_PartsService.dto.request.AdicionarPecaOsRequest;
import AutoBox_PartsService.dto.request.AlterarStatusOsRequest;
import AutoBox_PartsService.dto.request.CriarOrdemServicoRequest;
import AutoBox_PartsService.infrastructure.repository.ClienteRepository;
import AutoBox_PartsService.infrastructure.repository.OrdemServicoRepository;
import AutoBox_PartsService.infrastructure.repository.PecaRepository;
import AutoBox_PartsService.infrastructure.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {
    @Mock OrdemServicoRepository ordemRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock VeiculoRepository veiculoRepository;
    @Mock PecaRepository pecaRepository;

    private OrdemServicoService service;

    @BeforeEach
    void setUp() {
        service = new OrdemServicoService(ordemRepository, clienteRepository, veiculoRepository, pecaRepository);
    }

    @Test
    void deveCriarOrdemDeServico() {
        Cliente cliente = new Cliente(); cliente.setId(1L);
        Veiculo veiculo = new Veiculo(); veiculo.setId(2L); veiculo.setCliente(cliente);
        OrdemServico ordem = new OrdemServico(); ordem.setId(10L); ordem.setCliente(cliente); ordem.setVeiculo(veiculo);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(veiculoRepository.findById(2L)).thenReturn(Optional.of(veiculo));
        when(ordemRepository.save(any(OrdemServico.class))).thenReturn(ordem);

        var resposta = service.criar(new CriarOrdemServicoRequest(1L, 2L, "Troca de óleo"));

        assertEquals(10L, resposta.id());
        verify(ordemRepository).save(any(OrdemServico.class));
    }

    @Test
    void deveAlterarStatusDaOrdem() {
        OrdemServico ordem = ordemComClienteEVeiculo();
        ordem.setId(10L);
        when(ordemRepository.findById(10L)).thenReturn(Optional.of(ordem));
        when(ordemRepository.save(ordem)).thenReturn(ordem);

        var resposta = service.alterarStatus(10L, new AlterarStatusOsRequest(StatusOrdemServico.EM_ANDAMENTO));

        assertEquals(StatusOrdemServico.EM_ANDAMENTO, resposta.status());
    }

    @Test
    void deveAdicionarPecaEAtualizarEstoque() {
        OrdemServico ordem = ordemComClienteEVeiculo();
        ordem.setId(10L);
        Peca peca = new Peca(); peca.setId(3L); peca.setSku("FLT-001"); peca.setNome("Filtro"); peca.setPreco(BigDecimal.TEN); peca.setQuantidadeEstoque(5);
        when(ordemRepository.findById(10L)).thenReturn(Optional.of(ordem));
        when(pecaRepository.findById(3L)).thenReturn(Optional.of(peca));
        when(ordemRepository.save(ordem)).thenReturn(ordem);

        service.adicionarPeca(10L, new AdicionarPecaOsRequest(3L, 2));

        assertEquals(3, peca.getQuantidadeEstoque());
        verify(pecaRepository).save(peca);
    }

    @Test
    void deveFalharQuandoNaoHaEstoqueSuficiente() {
        OrdemServico ordem = ordemComClienteEVeiculo();
        ordem.setId(10L);
        Peca peca = new Peca(); peca.setId(3L); peca.setQuantidadeEstoque(1);
        when(ordemRepository.findById(10L)).thenReturn(Optional.of(ordem));
        when(pecaRepository.findById(3L)).thenReturn(Optional.of(peca));

        assertThrows(EstoqueInsuficienteException.class,
                () -> service.adicionarPeca(10L, new AdicionarPecaOsRequest(3L, 2)));
    }

    private OrdemServico ordemComClienteEVeiculo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setTelefone("11999999999");

        Veiculo veiculo = new Veiculo();
        veiculo.setId(2L);
        veiculo.setCliente(cliente);
        veiculo.setPlaca("ABC1234");
        veiculo.setMarca("Marca");
        veiculo.setModelo("Modelo");
        veiculo.setAno(2020);
        veiculo.setQuilometragem(1000);

        OrdemServico ordem = new OrdemServico();
        ordem.setVeiculo(veiculo);
        return ordem;
    }
}
