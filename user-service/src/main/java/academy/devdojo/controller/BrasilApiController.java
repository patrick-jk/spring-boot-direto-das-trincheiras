package academy.devdojo.controller;

import academy.devdojo.config.BrasilApiConfigurationProperties;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;

    @GetMapping
    public ResponseEntity<Void> csrfToken() {
        log.info("request received to find cep");
        log.info("baseUrl: {}, cepUri {}", brasilApiConfigurationProperties.baseUrl(), brasilApiConfigurationProperties.cepUri());
        return ResponseEntity.noContent().build();
    }
}