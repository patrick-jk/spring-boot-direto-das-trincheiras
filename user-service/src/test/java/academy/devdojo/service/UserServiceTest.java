package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
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
        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(expectedUsersFound);

        var users = service.findAll(user.getFirstName());
        assertThat(users).containsAll(expectedUsersFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(emptyList());

        var users = service.findAll(name);
        assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an user with given id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        var user = service.findByIdOrThrowNotFound(expectedUser.getId());

        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

        assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());

        var savedUser = service.save(userToSave);

        Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = this.userList.getFirst().withFirstName("Inuyasha");
        var email = userToUpdate.getEmail();
        var id = userToUpdate.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsNotFoundException_WhenUserIsNotFound() {
        var userToUpdate = this.userList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update throws EmailAlreadyExistsException when email belongs to another user")
    @Order(11)
    void update_ThrowsEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
        var savedUser = userList.getLast();
        var userToUpdate = userList.getFirst().withEmail(savedUser.getEmail());
        var email = userToUpdate.getEmail();
        var id = userToUpdate.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save throws EmailAlreadyExistsException when email exists")
    @Order(12)
    void save_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        var savedUser = userList.getLast();
        var userToSave = userUtils.newUserToSave().withEmail(savedUser.getEmail());
        var email = userToSave.getEmail();

        BDDMockito.when(repository.findByEmail(email)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }
}