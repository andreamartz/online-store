package net.yorksolutions.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    @Spy
    UserService service;

    @Mock
    UserAccountRepository repository;

    @Mock
    HashMap<UUID, Long> tokenMap;


    // ******* login ***************
    @Test
        // test the login
    void itShouldReturnUnauthWhenUserIsWrong() {  // nothing here about pw being wrong
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();

        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());  // invalid login?? // creates the matchers for us

        lenient().when(repository.findByUsernameAndPassword(not(eq(username)), eq(password)))
                .thenReturn(Optional.of(new UserAccount()));   // valid login // remember user account represents a row in the table // not is expecting another matcher, not a raw argument

        assertThrows(ResponseStatusException.class, () -> service.login(username, password));
    }

    @Test
        // test the login
    void itShouldReturnUnauthWhenPassIsWrong() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        // ArgumentMatcher - a test that can be run on an argument
        // any() - always returns true
        // eq(<expected>) - the passed argument must match <expected>
        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());  // invalid login?? // creates the matchers for us
        lenient().when(repository.findByUsernameAndPassword(eq(username), not(eq(password))))
                .thenReturn(Optional.of(new UserAccount()));  // valid login // remember user account represents a row in the table // not is expecting another matcher, not a raw argument
        assertThrows(ResponseStatusException.class, () -> service.login(username, password));  // can't call this callback fn directly, because then it would throw in our function and the test would not catch it // we want to give the test a chance to catch it
    }

    @Test
        // test the login
    void itShouldMapTheUUIDToTheIdWhenLoginSuccess() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        final Long id = (long) (Math.random() * 9999999); // the id of the user account associated with username, password
        final UserAccount expected = new UserAccount();
        expected.id = id;
        expected.username = username;
        expected.password = password;

        // setup
        when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(expected));
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        // captor is capturing the UUID below:
        when(tokenMap.put(captor.capture(), eq(id))).thenReturn(0L);  // when that happens, capture the first arg so we can query it later
        // when controller calls login...matches what it returned
        final var token = service.login(username, password);

        // asserts
        assertEquals(token, captor.getValue());
    }

    // ******* register ***************
    @Test
        // test register method
    void itShouldReturnInvalidIfUsernameExists() {
        final String username = "some username";
        final UUID token = UUID.randomUUID();
        when(repository.findByUsername(username)).thenReturn(Optional.of(
                new UserAccount()));
        assertThrows(ResponseStatusException.class, () -> service.register(token, username, "", ""));
    }

    @Test
        // test register method
        // Q: why is this test not passing?
    void itShouldSaveANewOwnerAccountWhenOwnerRegistersFirstTime() {
        final String username = "some username";
        final String password = "some password";
        final String userType = "owner";
        final UUID token = UUID.randomUUID();
        when(repository.findByUserType(userType)).thenReturn(Optional.empty());
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password, userType));
        Assertions.assertDoesNotThrow(() -> service.register(token, username, password, userType));
        assertEquals(new UserAccount(username, password, userType), captor.getValue());
    }

//     void itShouldSaveANewOwnerAccountWhenTheSuperOwnerRegistersAnotherOwner() {
//         final String username = "some username";
//         final String password = "some password";
//         final String userType = "owner";
//         when(repository.findByUserType(userType)).thenReturn(Optional.of(new UserAccount()));
//         ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
//         when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password, userType));
//         Assertions.assertDoesNotThrow(() -> service.register(username, password, userType));
//         assertEquals(new UserAccount(username, password, userType), captor.getValue());
//    }

//    void itShouldSaveANewOwnerAccountWhenTheSuperOwnerRegistersAnotherOwner() {
//        final String username = "some username";
//        final String password = "some password";
//        final String userType = "owner";
//        final UUID token = UUID.randomUUID();
//        when(repository.findByUserType(userType)).thenReturn(Optional.of(new UserAccount()));
//        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
//        when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password, userType));
//        Assertions.assertDoesNotThrow(() -> service.register(token, username, password, userType));
//        assertEquals(new UserAccount(username, password, userType), captor.getValue());
//    }

//    @Test
//        // test register method
//        // Q: why is this test not passing?
//    void itShouldSaveANewUserAccountWhenUserIsUnique() {
//        final String username = "some username";
//        final String password = "some password";
//        final Boolean owner = true;
//        when(repository.findByUsername(username)).thenReturn(Optional.empty());
//        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
//        when(repository.save(captor.capture())).thenReturn(new UserAccount());
//        Assertions.assertDoesNotThrow(() -> service.register(username, password));
//        assertEquals(new UserAccount(username, password), captor.getValue());
//    }


    // *********** isOwner ***********
    @Test
    void itShouldThrowWhenTokenNotInTokenMap() {
        // var
        UUID token = UUID.randomUUID();
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        // setup
//        when(tokenMap.containsKey(token)).thenReturn(false);
        when(tokenMap.containsKey(token)).thenReturn(false);

        // assert &/or call
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> service.isOwner(token));
        assertEquals(expected, exception.getStatus());
    }

    @Test
    void itShouldThrowWhenNoUserFoundWIthGivenId() {
        // var
        UUID token = UUID.randomUUID();
        Optional<UserAccount> mightBeUser = Optional.empty();
        Long id = 1L;
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        // setup
        when(tokenMap.containsKey(token)).thenReturn(true);
        when(tokenMap.get(token)).thenReturn(id);
        when(repository.findById(id)).thenReturn(mightBeUser);

        // assert &/or call
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.isOwner(token));
        assertEquals(expected, exception.getStatus());
    }

    @Test
    void itShouldReturnTrueIfUserIsAnOwner() {
        // variables
        UUID token = UUID.randomUUID();
//        final UserAccount userAccount = new UserAccount();
        final String username = "user";
        final String password = "password";
        final String userType = "owner";
        Optional<UserAccount> mightBeUser = Optional.of(new UserAccount(username, password, userType));
        Long id = 1L;
        boolean expected = true;

        // setup
        when(tokenMap.containsKey(token)).thenReturn(true);
        when(tokenMap.get(token)).thenReturn(id);
        when(repository.findById(id)).thenReturn(mightBeUser);

        // assert &/or call
        final boolean actual = service.isOwner(token);
        assertEquals(expected, actual);
    }

    @Test
    void itShouldReturnFalseIfUserIsNotAnOwner() {
        UUID token = UUID.randomUUID();
//        final UserAccount userAccount = new UserAccount();
        final String username = "user";
        final String password = "password";
        final String userType = "customer";
        Optional<UserAccount> mightBeUser = Optional.of(new UserAccount(username, password, userType));
        Long id = 1L;
        boolean expected = false;

        // setup
        when(tokenMap.containsKey(token)).thenReturn(true);
        when(tokenMap.get(token)).thenReturn(id);
        when(repository.findById(id)).thenReturn(mightBeUser);

        // assert &/or call
        final boolean actual = service.isOwner(token);
        assertEquals(expected, actual);
    }

    // *********** isAuthorized ***********

    @Test
    void itShouldNotThrowWhenTokenIsCorrect() {
        final UUID token = UUID.randomUUID();
        when(tokenMap.containsKey(token)).thenReturn(true);
        assertDoesNotThrow(() -> service.isAuthorized(token));
    }

    @Test
        // Q: replaces FizzBuzzControllerTests.java  itShouldThrowUnauthWhenFizzBuzzCalledWithBadToken, right?
        //    Q: seems to be a single unauth test now instead of one for every endpoint in controller, right?
        //    Q: this is a test of isAuthorized in UserService, right?
    void itShouldThrowUnauthWhenTokenIsBad() {
        final UUID token = UUID.randomUUID();
        // Q: below, we are mocking the function call inside when, right?
        when(tokenMap.containsKey(token)).thenReturn(false);
        // Q: walk through what this next line does please
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.isAuthorized(token));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }
}
// ***********  ***********

// ******* test the login 2 *************

//
