package AutoBox_PartsService.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CriarVendaRequest(
        Long clienteId,
        @NotEmpty(message = "A venda deve possuir pelo menos um item") @Valid List<ItemVendaRequest> itens
) {
    public record ItemVendaRequest(
            @NotNull(message = "O ID da peça é obrigatório") Long pecaId,
            @NotNull(message = "A quantidade é obrigatória") @Min(value = 1, message = "A quantidade deve ser maior que zero") Integer quantidade
    ) {}
}
