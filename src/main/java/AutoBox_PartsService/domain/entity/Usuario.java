package AutoBox_PartsService.domain.entity;

import AutoBox_PartsService.domain.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter @Setter
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(unique = true, nullable = false, length = 150)
    private String email;
    @Column(name = "senha_hash", nullable = false)
    private String senha;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PerfilUsuario perfil;
    @Column(nullable = false)
    private Boolean ativo = true;
}
