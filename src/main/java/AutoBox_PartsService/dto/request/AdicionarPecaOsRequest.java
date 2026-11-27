package AutoBox_PartsService.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdicionarPecaOsRequest(
        @NotNull(message = "O ID da peça é obrigatório")
        Long pecaId,
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {}