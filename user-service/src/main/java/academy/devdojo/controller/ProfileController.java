package academy.devdojo.controller;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> listAll() {
        log.debug("Request received to list all profiles");

        var profiles = service.findAll();

        var profileGetResponses = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(profileGetResponses);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest postRequest) {
        log.debug("Request to save profile: {}", postRequest);

        var profileToSave = mapper.toProfile(postRequest);

        var profile = service.save(profileToSave);

        var profilePostResponse = mapper.toProfilePostResponse(profile);

        return ResponseEntity.ok(profilePostResponse);
    }
}