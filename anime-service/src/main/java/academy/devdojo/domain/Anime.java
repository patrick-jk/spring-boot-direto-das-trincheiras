package academy.devdojo.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Anime {
    private Long id;
    private String name;

    public Anime(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Anime> getAnimes() {
        var hellsing = new Anime(1L, "Hellsing");
        var naruto = new Anime(2L, "Naruto");
        var kimetsuNoYaiba = new Anime(3L, "Kimetsu no Yaiba");

        return List.of(hellsing, naruto, kimetsuNoYaiba);
    }
}
