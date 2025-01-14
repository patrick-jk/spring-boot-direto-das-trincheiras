package academy.devdojo.anime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimeGetResponse {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Overlord")
    private String name;
}