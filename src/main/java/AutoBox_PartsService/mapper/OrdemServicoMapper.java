package AutoBox_PartsService.mapper;

import AutoBox_PartsService.domain.entity.OrdemServico;
import AutoBox_PartsService.dto.response.OrdemServicoResponse;
import java.math.BigDecimal;

public class OrdemServicoMapper {
    public OrdemServicoResponse toResponse(OrdemServico ordem) {
        var veiculo = ordem.getVeiculo();
        var cliente = ordem.getCliente() != null ? ordem.getCliente() : veiculo.getCliente();
        var itens = ordem.getItens().stream().map(item -> {
            var peca = item.getPeca();
            var subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
            return new OrdemServicoResponse.ItemResponse(item.getId(), peca.getId(), peca.getSku(),
                    peca.getNome(), item.getQuantidade(), item.getPrecoUnitario(), subtotal);
        }).toList();
        var valorPecas = itens.stream().map(OrdemServicoResponse.ItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrdemServicoResponse(
                ordem.getId(),
                new OrdemServicoResponse.ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getTelefone()),
                new OrdemServicoResponse.VeiculoResponse(veiculo.getId(), veiculo.getPlaca(), veiculo.getMarca(),
                        veiculo.getModelo(), veiculo.getAno(), veiculo.getQuilometragem()),
                ordem.getDescricao(), ordem.getStatus(), ordem.getValorMaoDeObra(), itens, valorPecas,
                ordem.getValorMaoDeObra().add(valorPecas), ordem.getDataAbertura());
    }
}