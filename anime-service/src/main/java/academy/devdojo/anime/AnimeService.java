package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import academy.devdojo.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeService {

  private final AnimeRepository repository;

  public List<Anime> findAll(String name) {
    return name == null ? repository.findAll() : repository.findByName(name);
  }

  public Page<Anime> findAllPaginated(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Anime findByIdOrThrowNotFound(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Anime not Found"));
  }

  public Anime save(Anime anime) {
    return repository.save(anime);
  }

  public void delete(Long id) {
    var anime = findByIdOrThrowNotFound(id);
    repository.delete(anime);
  }

  public void update(Anime animeToUpdate) {
    assertAnimeExists(animeToUpdate.getId());
    repository.save(animeToUpdate);
  }

  public void assertAnimeExists(Long id) {
    findByIdOrThrowNotFound(id);
  }
}