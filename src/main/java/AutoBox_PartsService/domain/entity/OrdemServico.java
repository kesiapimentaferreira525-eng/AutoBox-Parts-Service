package AutoBox_PartsService.domain.entity;

import AutoBox_PartsService.domain.enums.StatusOrdemServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordens_servico")
@Getter @Setter
public class OrdemServico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero_os", nullable = false, unique = true, length = 20)
    private String numeroOs;
    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;
    @Transient
    private Cliente cliente;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status = StatusOrdemServico.ABERTA;
    @Column(name = "descricao_problema", nullable = false, columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "valor_mao_de_obra", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMaoDeObra = BigDecimal.ZERO;
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;
    @Column(name = "estoque_baixado", nullable = false)
    private Boolean estoqueBaixado = false;
    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrdemServico> itens = new ArrayList<>();
}