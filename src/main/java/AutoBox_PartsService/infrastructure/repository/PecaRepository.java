package AutoBox_PartsService.infrastructure.repository;

import AutoBox_PartsService.domain.entity.Peca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PecaRepository extends JpaRepository<Peca, Long> {}
