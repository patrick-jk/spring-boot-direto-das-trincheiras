package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerRestAssuredIT extends IntegrationTestConfig {
    private static final String URL = "/v1/users";
    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .and(users -> {
                    users.node("[0].id").asNumber().isPositive();
                    users.node("[1].id").asNumber().isPositive();
                    users.node("[2].id").asNumber().isPositive();
                });

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users?firstName=John returns list with found object when first name exists")
    @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-john-first-name-200.json");
        var firstName = "John";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .queryParam("firstName", firstName)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users?firstName=x returns empty list when name is not found")
    @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(3)
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
        var firstName = "x";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .queryParam("firstName", firstName)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users/1 returns an user with given id")
    @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var users = repository.findByFirstNameIgnoreCase("John");

        Assertions.assertThat(users).hasSize(1);

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", users.getFirst().getId())
                .when()
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users/99 throws NotFound 404 when user is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenUserIsNotFound() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-404.json");

        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/users creates an user")
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var id = repository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("DELETE v1/users/99 throws NotFound when user is not found")
    @Order(8)
    void delete_ThrowsNotFound_WhenUserIsNotFound() {
        var expectedResponse = fileUtils.readResourceFile("user/delete-user-by-id-404.json");

        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("PUT v1/users updates a user")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        var users = repository.findByFirstNameIgnoreCase("John");

        Assertions.assertThat(users).hasSize(1);

        request = request.replace("1", users.getFirst().getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("PUT v1/users throws NotFound when user is not found")
    @Order(10)
    void update_ThrowsNotFound_WhenUserIsNotFound() {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var response = fileUtils.readResourceFile("user/put-user-by-id-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> postUserBadRequestSource() {

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", "post-response-user-empty-fields-400.json"),
                Arguments.of("post-request-user-blank-fields-400.json", "post-response-user-blank-fields-400.json"),
                Arguments.of("post-request-user-invalid-email-400.json", "post-response-user-invalid-email-400.json")
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", "put-response-user-empty-fields-400.json"),
                Arguments.of("put-request-user-blank-fields-400.json", "put-response-user-blank-fields-400.json"),
                Arguments.of("put-request-user-invalid-email-400.json", "put-response-user-invalid-email-400.json")
        );
    }
}