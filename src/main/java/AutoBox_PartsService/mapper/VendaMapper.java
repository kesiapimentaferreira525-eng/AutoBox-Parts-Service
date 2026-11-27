package AutoBox_PartsService.mapper;

import AutoBox_PartsService.domain.entity.Venda;
import AutoBox_PartsService.dto.response.VendaResponse;

public class VendaMapper {
    public VendaResponse toResponse(Venda venda) {
        var itens = venda.getItens().stream().map(item -> new VendaResponse.ItemResponse(
                item.getPeca().getId(), item.getPeca().getNome(), item.getQuantidade(),
                item.getPrecoUnitario(), item.getPrecoUnitario().multiply(
                java.math.BigDecimal.valueOf(item.getQuantidade())))).toList();
        return new VendaResponse(
                venda.getId(),
                venda.getCliente() != null ? venda.getCliente().getId() : null,
                venda.getDataVenda(),
                itens,
                venda.getValorTotal());
    }
}
