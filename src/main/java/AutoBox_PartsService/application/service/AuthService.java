package AutoBox_PartsService.application.service;

import AutoBox_PartsService.domain.exception.RegraNegocioException;
import AutoBox_PartsService.dto.request.LoginRequest;
import AutoBox_PartsService.dto.response.LoginResponse;
import AutoBox_PartsService.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponse login(LoginRequest request) {
        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas"));
        if (!usuario.getSenha().equals(request.senha())) {
            throw new RegraNegocioException("Credenciais inválidas");
        }
        return new LoginResponse("sem-token-configurado", new LoginResponse.UsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil().name()));
    }
}
