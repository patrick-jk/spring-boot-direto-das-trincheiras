package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnimePutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "The field 'name' is required")
    private String name;
}