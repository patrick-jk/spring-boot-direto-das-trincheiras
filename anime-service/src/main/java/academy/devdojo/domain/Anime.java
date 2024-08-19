package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;
    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        var hellsing = new Anime(1L, "Hellsing");
        var naruto = new Anime(2L, "Naruto");
        var kimetsuNoYaiba = new Anime(3L, "Kimetsu no Yaiba");

        animes.addAll(List.of(hellsing, naruto, kimetsuNoYaiba));
    }
}
