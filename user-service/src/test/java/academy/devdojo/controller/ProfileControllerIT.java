package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileControllerIT {
    private static final String URL = "/v1/profiles";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProfileUtils profileUtils;
    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/init_two_profiles.sql")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {};

        var responseEntity = testRestTemplate.exchange(URL, GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        responseEntity.getBody()
                .forEach(profileGetResponse -> assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsFound() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {};

        var responseEntity = testRestTemplate.exchange(URL, GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isEmpty();

        responseEntity.getBody()
                .forEach(profileGetResponse -> assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("POST v1/profiles creates an profile")
    @Order(3)
    void save_CreatesProfile_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var profileEntity = buildHttpEntity(request);
        var responseEntity = testRestTemplate.exchange(URL, POST, profileEntity, ProfilePostResponse.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, httpHeaders);
    }
}