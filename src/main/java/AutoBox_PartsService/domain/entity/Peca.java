package AutoBox_PartsService.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "pecas")
@Getter @Setter
public class Peca {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String sku;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(length = 80)
    private String fabricante;
    @Column(name = "aplicacao_veiculo", length = 150)
    private String descricao;
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque = 0;
    @Column(name = "estoque_minimo", nullable = false)
    private Integer estoqueMinimo = 2;
    @Version
    @Column(nullable = false)
    private Long versao = 0L;
}