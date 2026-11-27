package AutoBox_PartsService.domain.entity;

import AutoBox_PartsService.domain.enums.StatusOrdemServico;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrdemServicoTests {
    @Test
    void deveIniciarComStatusAberta() {
        OrdemServico ordem = new OrdemServico();

        assertEquals(StatusOrdemServico.ABERTA, ordem.getStatus());
        assertNotNull(ordem.getDataAbertura());
        assertTrue(ordem.getItens().isEmpty());
    }

    @Test
    void deveAdicionarItemNaOrdem() {
        OrdemServico ordem = new OrdemServico();
        ItemOrdemServico item = new ItemOrdemServico();

        ordem.getItens().add(item);

        assertEquals(1, ordem.getItens().size());
    }
}
