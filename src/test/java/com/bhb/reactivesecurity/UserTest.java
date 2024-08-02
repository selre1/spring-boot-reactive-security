package com.bhb.reactivesecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @WithMockUser(username = "alice", roles = {"SOME_OTHER_ROLE"})
    void verifyWithoutProperRoleFails(){
        webTestClient.post().uri("/")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(username = "bob", roles = {"QKD"})
    void verifyWithoutProperRoleSucceeds(){
        webTestClient.post().uri("/")
                .exchange()
                .expectStatus().isForbidden();
    }
}
