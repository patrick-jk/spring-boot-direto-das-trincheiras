package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import academy.devdojo.dto.AnimeGetResponse;
import academy.devdojo.dto.AnimePostRequest;
import academy.devdojo.dto.AnimePostResponse;
import academy.devdojo.dto.AnimePutRequest;
import academy.devdojo.dto.PageAnimeGetResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Anime toAnime(AnimePostRequest postRequest);

  Anime toAnime(AnimePutRequest request);

  AnimeGetResponse toAnimeGetResponse(Anime anime);

  AnimePostResponse toAnimePostResponse(Anime anime);

  List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animeList);

  PageAnimeGetResponse toPageAnimeGetResponse(Page<Anime> jpaPageAnimeGetResponse);
}