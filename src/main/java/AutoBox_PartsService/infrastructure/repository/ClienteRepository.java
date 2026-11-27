package AutoBox_PartsService.infrastructure.repository;

import AutoBox_PartsService.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
