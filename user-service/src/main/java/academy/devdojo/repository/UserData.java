package academy.devdojo.repository;

import academy.devdojo.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class UserData {
    private final List<User> users = new ArrayList<>(3);

    {
        var dave = User.builder().id(1L).firstName("Dave").lastName("Mustaine").email("dave@gmail.com").build();
        var tom = User.builder().id(2L).firstName("Tom").lastName("Araya").email("tomaraya@gmail.com").build();
        var john = User.builder().id(3L).firstName("John").lastName("Galt").email("johngalt@gmail.com").build();
        users.addAll(List.of(dave, tom, john));
    }
}