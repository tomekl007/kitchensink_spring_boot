package org.jboss.as.quickstarts.kitchensink.test;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class RemoteMemberRegistrationIT {

    private static final Logger log = Logger.getLogger(RemoteMemberRegistrationIT.class.getName());

    private URI getHttpEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/kitchensink";
        }
        try {
            return new URI(host + "/rest/members");
        } catch (URISyntaxException ex) {
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
        JsonObject json = Json.createObjectBuilder()
                .add("name", "Jane Doe")
                .add("email", "jane@mailinator.com")
                .add("phoneNumber", "2125551234")
                .build();

        HttpRequest request = HttpRequest.newBuilder(getHttpEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
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

        try (JsonReader jsonReader = Json.createReader(new StringReader(response.body()))) {
            JsonArray members = jsonReader.readArray();
            assertNotNull(members, "Response should be a JSON array");

            log.info("Found " + members.size() + " members");
            for (int i = 0; i < members.size(); i++) {
                JsonObject member = members.getJsonObject(i);
                log.info("Member " + i + ": " + member);
            }
        }
    }
}
