package AutoBox_PartsService.infrastructure.repository;

import AutoBox_PartsService.domain.entity.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
}