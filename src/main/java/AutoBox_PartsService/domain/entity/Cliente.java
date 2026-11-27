package AutoBox_PartsService.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter @Setter
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(length = 150)
    private String email;
    @Column(name = "cpf_cnpj", unique = true, length = 20)
    private String cpf;
}
