package academy.devdojo.producer;

import academy.devdojo.domain.Producer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

  List<Producer> findByName(String name);
}