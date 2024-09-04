package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        var fullMetal = Anime.builder().id(1L).name("Full Metal Brotherhood").build();
        var steinsGate = Anime.builder().id(2L).name("Steins no Gate").build();
        var mashle = Anime.builder().id(3L).name("Mashle").build();
        animeList = new ArrayList<>(List.of(fullMetal, steinsGate, mashle));
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findById returns an anime with given id")
    @Order(2)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var expectedAnime = animeList.getFirst();
        var anime = repository.findById(expectedAnime.getId());
        Assertions.assertThat(anime).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.findByName(null);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    @Order(4)
    void findByName_ReturnsFoundAnimeInList_WhenNameIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectedAnime = animeList.getFirst();
        var animes = repository.findByName(expectedAnime.getName());
        Assertions.assertThat(animes).hasSize(1).contains(expectedAnime);
    }

    @Test
    @DisplayName("save creates an anime")
    @Order(5)
    void save_CreatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToSave = Anime.builder().id(99L).name("Pokemon").build();
        var anime = repository.save(animeToSave);

        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSavedOptional = repository.findById(animeToSave.getId());

        Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes an anime")
    @Order(6)
    void delete_RemovesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToDelete = animeList.getFirst();
        repository.delete(animeToDelete);

        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update updates an anime")
    @Order(7)
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var animeToUpdate = this.animeList.getFirst();
        animeToUpdate.setName("JoJo's Bizarre Adventure Part 7: Steel Ball Run");

        repository.update(animeToUpdate);

        Assertions.assertThat(this.animeList).contains(animeToUpdate);

        var animeUpdatedOptional = repository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeUpdatedOptional).isPresent();
        Assertions.assertThat(animeUpdatedOptional.get().getName()).isEqualTo(animeToUpdate.getName());
    }
}