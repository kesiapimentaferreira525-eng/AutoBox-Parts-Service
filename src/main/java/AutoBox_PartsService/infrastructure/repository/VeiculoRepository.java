package AutoBox_PartsService.infrastructure.repository;

import AutoBox_PartsService.domain.entity.Veiculo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    @Override
    @EntityGraph(attributePaths = "cliente")
    List<Veiculo> findAll();

    @Override
    @EntityGraph(attributePaths = "cliente")
    Optional<Veiculo> findById(Long id);
}
