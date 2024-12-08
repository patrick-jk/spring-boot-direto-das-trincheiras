package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo"})
class UserControllerTest {
    private static final String URL = "/v1/users";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserData userData;
    @MockitoSpyBean
    private UserHardCodedRepository repository;
    private List<User> userList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?firstName=John returns list with found object when first name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-john-first-name-200.json");
        var firstName = "John";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/users?firstName=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
        var firstName = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns an user with given id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws ResponseStatusException 404 when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @DisplayName("POST v1/users creates an user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");
        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var id = userList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users/99 throws ResponseStatusException when user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @DisplayName("PUT v1/users updates a user")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-empty-fields-400.json");

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        var firstNameError = "The field 'firstName' is required";
        var lastNameError = "The field 'lastName' is required";
        var emailError = "The field 'email' is required";

        Assertions.assertThat(resolvedException.getMessage())
                .contains(firstNameError, lastNameError, emailError);
    }

    @Test
    @DisplayName("POST v1/users returns bad request when fields are blank")
    @Order(12)
    void save_ReturnsBadRequest_WhenFieldsAreBlank() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-blank-fields-400.json");

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        var firstNameError = "The field 'firstName' is required";
        var lastNameError = "The field 'lastName' is required";
        var emailError = "The field 'email' is required";

        Assertions.assertThat(resolvedException.getMessage())
                .contains(firstNameError, lastNameError, emailError);
    }
}