package AutoBox_PartsService.dto.response;

public record LoginResponse(String token, UsuarioResponse usuario) {
    public record UsuarioResponse(Long id, String nome, String email, String perfil) {}
}
