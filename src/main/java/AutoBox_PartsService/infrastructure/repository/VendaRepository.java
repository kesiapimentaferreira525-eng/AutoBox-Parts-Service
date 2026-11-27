package AutoBox_PartsService.infrastructure.repository;

import AutoBox_PartsService.domain.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}