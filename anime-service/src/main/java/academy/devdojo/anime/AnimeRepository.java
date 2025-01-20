package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

  List<Anime> findByName(String name);
}