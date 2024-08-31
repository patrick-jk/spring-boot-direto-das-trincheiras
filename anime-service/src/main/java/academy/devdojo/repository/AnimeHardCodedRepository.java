package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        var hellsing = Anime.builder().id(1L).name("Hellsing").build();
        var jojoBizarreAdventure = Anime.builder().id(2L).name("Jojo's Bizarre Adventure").build();
        var kimetsuNoYaiba = Anime.builder().id(3L).name("Kimetsu no Yaiba").build();

        ANIMES.addAll(List.of(hellsing, jojoBizarreAdventure, kimetsuNoYaiba));
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream().filter(producer -> producer.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    public Anime save(Anime producer) {
        ANIMES.add(producer);
        return producer;
    }

    public void delete(Anime producer) {
        ANIMES.remove(producer);
    }

    public void update(Anime producer) {
        delete(producer);
        save(producer);
    }
}