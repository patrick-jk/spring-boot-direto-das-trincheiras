package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;
    @Mock
    private UserData userData;
    private List<User> userList;
    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findAll();
        assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns an user with given id")
    @Order(2)
    void findById_ReturnsUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var expectedUser = userList.getFirst();
        var user = repository.findById(expectedUser.getId());
        Assertions.assertThat(user).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByFirstName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenFirstNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findByFirstName(null);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByFirstName returns list with found object when first name exists")
    @Order(4)
    void findByName_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = repository.findByFirstName(expectedUser.getFirstName());
        Assertions.assertThat(users).hasSize(1).contains(expectedUser);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(5)
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToSave = userUtils.newUserToSave();
        var user = repository.save(userToSave);

        Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var userSavedOptional = repository.findById(userToSave.getId());

        Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
    }
}