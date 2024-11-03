package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        var hellsing = Anime.builder().id(1L).name("Hellsing").build();
        var jojoBizarreAdventure = Anime.builder().id(2L).name("JoJo's Bizarre Adventure").build();
        var kimetsuNoYaiba = Anime.builder().id(3L).name("Kimetsu no Yaiba").build();

        animes.addAll(List.of(hellsing, jojoBizarreAdventure, kimetsuNoYaiba));
    }
}