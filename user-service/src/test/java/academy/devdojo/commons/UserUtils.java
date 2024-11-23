package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public List<User> newUserList() {
        var john = User.builder().id(1L).firstName("John").lastName("Cooper").email("jcooper@skillet.com").build();
        var billy = User.builder().id(2L).firstName("Billy").lastName("Joe").email("billyjoe@greenday.com").build();
        var neil = User.builder().id(3L).firstName("Neil").lastName("Peart").email("neilpeart@rush.com").build();

        return new ArrayList<>(List.of(john, billy, neil));
    }

    public User newUserToSave() {
        return User.builder().id(99L)
                .firstName("John")
                .lastName("Bonham")
                .email("jbonham@gmail.com")
                .build();
    }
}