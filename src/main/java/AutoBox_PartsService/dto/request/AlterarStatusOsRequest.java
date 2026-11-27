package AutoBox_PartsService.dto.request;

import AutoBox_PartsService.domain.enums.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusOsRequest(
        @NotNull(message = "O novo status é obrigatório")
        StatusOrdemServico status
) {}