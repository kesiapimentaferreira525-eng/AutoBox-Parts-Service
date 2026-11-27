package AutoBox_PartsService.dto.response;

import java.math.BigDecimal;

public record PecaResponse(Long id, String sku, String nome, String aplicacao,
                           BigDecimal precoVenda, Integer quantidadeEstoque, Integer estoqueMinimo) {}