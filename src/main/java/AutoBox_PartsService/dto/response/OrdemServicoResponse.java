package AutoBox_PartsService.dto.response;

import AutoBox_PartsService.domain.enums.StatusOrdemServico;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoResponse(Long id, ClienteResponse cliente, VeiculoResponse veiculo,
                                   String descricaoProblema, StatusOrdemServico status,
                                   BigDecimal valorMaoDeObra, List<ItemResponse> itens,
                                   BigDecimal valorPecas, BigDecimal valorTotal, LocalDateTime dataAbertura) {
    public record ClienteResponse(Long id, String nome, String telefone) {}

    public record VeiculoResponse(Long id, String placa, String marca, String modelo,
                                  Integer ano, Integer quilometragem) {}

    public record ItemResponse(Long id, Long pecaId, String sku, String nomePeca, Integer quantidade,
                               BigDecimal precoUnitario, BigDecimal subtotal) {}
}