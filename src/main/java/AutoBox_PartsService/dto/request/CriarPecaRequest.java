package AutoBox_PartsService.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CriarPecaRequest(
        @NotBlank(message = "O nome da peça é obrigatório") String nome,
        String descricao,
        @NotNull(message = "A quantidade em estoque é obrigatória") @Min(value = 0, message = "A quantidade em estoque não pode ser negativa") Integer quantidadeEstoque,
        @NotNull(message = "O preço é obrigatório") @Min(value = 0, message = "O preço não pode ser negativo") BigDecimal preco
) {}