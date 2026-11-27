package AutoBox_PartsService.dto.response;

public record VeiculoResponse(Long id, String placa, String marca, String modelo,
                              Integer ano, Integer quilometragem, ClienteResponse cliente) {
    public record ClienteResponse(Long id, String nome, String telefone) {}
}
