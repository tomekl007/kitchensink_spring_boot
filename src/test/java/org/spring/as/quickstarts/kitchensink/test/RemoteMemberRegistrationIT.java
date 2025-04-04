package org.spring.as.quickstarts.kitchensink.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RemoteMemberRegistrationIT {

    private static final Logger log = Logger.getLogger(RemoteMemberRegistrationIT.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    private URI getHttpEndpoint() {
        try {
            return new URI("http://localhost:" + port + "/kitchensink/rest/members");
        } catch (Exception ex) {
            throw new RuntimeException("Invalid URI", ex);
        }
    }

    @Test
    void shouldRegisterNewMemberSuccessfully() throws Exception {
        Map<String, Object> json = Map.of(
                "name", "Jane Doe",
                "email", "jane@mailinator.com",
                "phoneNumber", "2125551234"
        );

        HttpRequest request = HttpRequest.newBuilder(getHttpEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(json)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected HTTP status 200");
        assertTrue(response.body().isEmpty(), "Expected empty response body");
    }

    @Test
    void shouldReturnAllRegisteredMembers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(getHttpEndpoint())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected HTTP status 200");

        var members = objectMapper.readTree(response.body());
        assertTrue(members.isArray(), "Expected a JSON array");

        log.info("Found " + members.size() + " members");
        for (int i = 0; i < members.size(); i++) {
            log.info("Member " + i + ": " + members.get(i).toPrettyString());
        }
    }
}
