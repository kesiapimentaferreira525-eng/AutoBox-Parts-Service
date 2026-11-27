package AutoBox_PartsService.domain.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PecaTests {
    @Test
    void deveConfigurarDadosDaPeca() {
        Peca peca = new Peca();
        peca.setNome("Filtro de óleo");
        peca.setQuantidadeEstoque(10);
        peca.setPreco(new BigDecimal("49.90"));

        assertEquals("Filtro de óleo", peca.getNome());
        assertEquals(10, peca.getQuantidadeEstoque());
        assertEquals(new BigDecimal("49.90"), peca.getPreco());
    }
}
