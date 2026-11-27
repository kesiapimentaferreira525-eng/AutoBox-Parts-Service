package AutoBox_PartsService.mapper;

import AutoBox_PartsService.domain.entity.Peca;
import AutoBox_PartsService.dto.response.PecaResponse;

public class PecaMapper {
    public PecaResponse toResponse(Peca peca) {
        return new PecaResponse(peca.getId(), peca.getSku(), peca.getNome(), peca.getDescricao(),
                peca.getPreco(), peca.getQuantidadeEstoque(), peca.getEstoqueMinimo());
    }
}