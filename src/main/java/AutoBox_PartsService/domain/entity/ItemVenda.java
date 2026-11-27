package AutoBox_PartsService.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_venda")
@Getter @Setter
public class ItemVenda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
    @ManyToOne(optional = false)
    @JoinColumn(name = "peca_id", nullable = false)
    private Peca peca;
    @Column(nullable = false)
    private Integer quantidade;
    @Column(name = "preco_unitario_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;
}
