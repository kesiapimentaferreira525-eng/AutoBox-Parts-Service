package AutoBox_PartsService.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(Long id, Long clienteId, LocalDateTime dataVenda, List<ItemResponse> itens,
                            BigDecimal valorTotal) {
    public record ItemResponse(Long pecaId, String nomePeca, Integer quantidade,
                               BigDecimal precoUnitario, BigDecimal subtotal) {}
}