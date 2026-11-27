package AutoBox_PartsService.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionTests {
    @Test
    void devePreservarMensagemDasExcecoesDeDominio() {
        assertEquals("Sem estoque", new EstoqueInsuficienteException("Sem estoque").getMessage());
        assertEquals("Não encontrado", new RecursoNaoEncontradoException("Não encontrado").getMessage());
        assertEquals("Operação inválida", new RegraNegocioException("Operação inválida").getMessage());
    }
}
