package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardCodedRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserHardCodedRepository repository;
    private List<User> userList;
    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);
        assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findByName returns list with found object when firstName exists")
    @Order(2)
    void findByName_ReturnsFoundUserInList_WhenNameIsFound() {
        var user = userList.getFirst();

        List<User> expectedUsersFound = singletonList(user);
        BDDMockito.when(repository.findByFirstName(user.getFirstName())).thenReturn(expectedUsersFound);

        var users = service.findAll(user.getFirstName());
        assertThat(users).containsAll(expectedUsersFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByFirstName(name)).thenReturn(emptyList());

        var users = service.findAll(name);
        assertThat(users).isNotNull().isEmpty();
    }
}