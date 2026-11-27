package AutoBox_PartsService.dto.request;

import jakarta.validation.constraints.NotNull;

public record CriarOrdemServicoRequest(
        @NotNull Long clienteId,
        @NotNull Long veiculoId,
        String descricao
) {}
