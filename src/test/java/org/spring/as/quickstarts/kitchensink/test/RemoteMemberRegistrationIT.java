package org.spring.as.quickstarts.kitchensink.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class RemoteMemberRegistrationIT {

    private static final Logger log = Logger.getLogger(RemoteMemberRegistrationIT.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private URI getHttpEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/kitchensink";
        }
        try {
            return new URI(host + "/rest/members");
        } catch (Exception ex) {
            throw new RuntimeException("Invalid URI", ex);
        }
    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
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
