package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimeHardCodedRepository {
    private final AnimeData animeData;

    public List<Anime> findAll() {
        return animeData.getAnimes();
    }

    public Optional<Anime> findById(Long id) {
        return animeData.getAnimes().stream().filter(producer -> producer.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return animeData.getAnimes().stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    public Anime save(Anime producer) {
        animeData.getAnimes().add(producer);
        return producer;
    }

    public void delete(Anime producer) {
        animeData.getAnimes().remove(producer);
    }

    public void update(Anime producer) {
        delete(producer);
        save(producer);
    }
}