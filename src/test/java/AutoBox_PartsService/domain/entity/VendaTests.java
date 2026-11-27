package AutoBox_PartsService.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendaTests {
    @Test
    void deveIniciarComDataEListaDeItens() {
        Venda venda = new Venda();

        assertNotNull(venda.getDataVenda());
        assertTrue(venda.getItens().isEmpty());
    }
}
