package net.yorksolutions.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// Jay's Q
// allows Mockito to do all that for you (when use the annotations below, you need @ExtendWith)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {
    @LocalServerPort
    int port;

    @Autowired // Give me the item that Spring created for me
    UserController controller;

    @Mock
    UserService service;

    @BeforeEach
    void setup() {
        controller.setService(service);
    }

    // *********** login ***********
    @Test
        // test login endpoint
    void itShouldRespondWithResultUUIDWhenLoginServiceCalledWithUsernameAndPassword() {
//    void itShouldRespondWithTokenWhenLoginValid() {
        final TestRestTemplate rest = new TestRestTemplate();
        final String username = "some username";
        final String password = "some password";
        String url = "http://localhost:" + port + "/login?username=" + username + "&password=" + password;
        final UUID token = UUID.randomUUID();
        // mock service.login call
        when(service.login(username, password)).thenReturn(token);
        // whenever I hit that endpoint, not only should I expect a status code of OK, but
        // also that the token I get back is equal to the token I mocked in my service
        final ResponseEntity<UUID> response = rest.getForEntity(url, UUID.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    // *********** register ***********

    // itShouldRespondWithUnAuthStatusWhenLoginInvalid

    // 19:00 in video 194546
//    @Test
//    void itShouldCallRegisterServiceWithUsernameAndPassword() {
//        final TestRestTemplate rest = new TestRestTemplate();
//        final String username = "some username";
//        final String password = "some password";
//        final String userType = "some type";
//        final UUID token = UUID.randomUUID();
//        String url = "http://localhost:" + port + "/register?token=" + token + "&username=" + username + "&password=" + password + "&userType=" + userType;
//        doThrow(new ResponseStatusException(HttpStatus.ACCEPTED)).when(service).register(token, username, password, userType);
//        final ResponseEntity<Void> response = rest.getForEntity(url, Void.class);
//        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//    }

    // *********** isOwner ***********
    @Test
    void itShouldCallIsOwnerWithAToken() {
        TestRestTemplate rest = new TestRestTemplate();
        final UUID token = UUID.randomUUID();
        final HttpStatus expected = HttpStatus.ACCEPTED;
        String url = "http://localhost:" + port + "/isOwner?token=" + token;

        doThrow(new ResponseStatusException(HttpStatus.ACCEPTED)).when(service).isOwner(token);
        final ResponseEntity<Void> response = rest.getForEntity(url, Void.class);
        assertEquals(expected, response.getStatusCode());
    }

    // *********** isAuthorized ***********

    @Test
    void itShouldCallIsAuthorizedWithAToken() {
        TestRestTemplate rest = new TestRestTemplate();  // like fetch; has more features (debugging, error msgs) than RestTemplate
        final UUID token = UUID.randomUUID();
        final HttpStatus expected = HttpStatus.ACCEPTED;
        String url = "http://localhost:" + port + "/isAuthorized?token=" + token;

        doThrow(new ResponseStatusException(HttpStatus.ACCEPTED)).when(service).isAuthorized(token);
        final ResponseEntity<Void> response = rest.getForEntity(url, Void.class);
        assertEquals(expected, response.getStatusCode());
    }
}
