package academy.devdojo.response;

import lombok.*;

@Getter
@Setter
@Builder

public class UserGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
