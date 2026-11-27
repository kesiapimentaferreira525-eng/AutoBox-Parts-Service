package AutoBox_PartsService.application.service;

import AutoBox_PartsService.domain.entity.Peca;
import AutoBox_PartsService.domain.entity.Venda;
import AutoBox_PartsService.domain.exception.EstoqueInsuficienteException;
import AutoBox_PartsService.dto.request.CriarVendaRequest;
import AutoBox_PartsService.infrastructure.repository.ClienteRepository;
import AutoBox_PartsService.infrastructure.repository.PecaRepository;
import AutoBox_PartsService.infrastructure.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {
    @Mock VendaRepository vendaRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock PecaRepository pecaRepository;

    private VendaService service;

    @BeforeEach
    void setUp() {
        service = new VendaService(vendaRepository, clienteRepository, pecaRepository);
    }

    @Test
    void deveCriarVendaSemCliente() {
        Peca peca = new Peca(); peca.setId(1L); peca.setNome("Óleo"); peca.setPreco(BigDecimal.TEN); peca.setQuantidadeEstoque(10);
        Venda venda = new Venda(); venda.setId(20L);

        when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);

        var request = new CriarVendaRequest(null,
                List.of(new CriarVendaRequest.ItemVendaRequest(1L, 3)));

        var resposta = service.criar(request);

        assertEquals(20L, resposta.id());
        assertEquals(7, peca.getQuantidadeEstoque());
        verify(vendaRepository).save(any(Venda.class));
    }

    @Test
    void deveFalharQuandoQuantidadeDaVendaExcedeEstoque() {
        Peca peca = new Peca(); peca.setId(1L); peca.setNome("Óleo"); peca.setQuantidadeEstoque(2);
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));

        var request = new CriarVendaRequest(null,
                List.of(new CriarVendaRequest.ItemVendaRequest(1L, 3)));

        assertThrows(EstoqueInsuficienteException.class, () -> service.criar(request));
    }

    @Test
    void deveFalharQuandoQuantidadeDaVendaForZero() {
        Peca peca = new Peca(); peca.setId(1L); peca.setNome("Óleo"); peca.setQuantidadeEstoque(5);
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));

        var request = new CriarVendaRequest(null,
                List.of(new CriarVendaRequest.ItemVendaRequest(1L, 0)));

        assertThrows(EstoqueInsuficienteException.class, () -> service.criar(request));
    }
}
