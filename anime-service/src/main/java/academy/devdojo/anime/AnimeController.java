package academy.devdojo.anime;

import academy.devdojo.api.AnimeControllerApi;
import academy.devdojo.dto.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Anime API", description = "Anime related endpoints")
@SecurityRequirement(name = "basicAuth")
public class AnimeController implements AnimeControllerApi {
    private final AnimeMapper mapper;
    private final AnimeService service;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnimeGetResponse>> findAllAnimes(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);

        var animes = service.findAll(name);

        var animeGetResponses = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponses);
    }

    @Override
    @GetMapping("/paginated")
    public ResponseEntity<PageAnimeGetResponse> findAllAnimesPaginated(
            @Min(0) @Parameter(name = "page", description = "Zero-based page index (0..N)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Min(1) @Parameter(name = "size", description = "The size of the page to be returned", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Parameter(name = "sort", description = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "sort", required = false) List<String> sort,
            @ParameterObject final Pageable pageable
    ) {
        log.debug("Request received to list all animes paginated");

        var jpaPageAnimeGetResponse = service.findAllPaginated(pageable);
        var pageAnimeGetResponse = mapper.toPageAnimeGetResponse(jpaPageAnimeGetResponse);

        return ResponseEntity.ok(pageAnimeGetResponse);
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findAnimeById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var anime = service.findByIdOrThrowNotFound(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AnimePostResponse> saveAnime(@RequestBody @Valid AnimePostRequest request) {
        log.debug("Request to save anime: {}", request);

        var anime = mapper.toAnime(request);

        var animeSaved = service.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @Override
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnimeById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping
    public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime: {}", request);

        var anime = mapper.toAnime(request);

        service.update(anime);

        return ResponseEntity.noContent().build();
    }
}