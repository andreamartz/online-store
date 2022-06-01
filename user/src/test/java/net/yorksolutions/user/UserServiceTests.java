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
    // Mockito shall create this (the service) for me, inject all mocks that it can, and give me the created
    //     thing
    @Spy
    UserService service;

    // no accounts will be saved bc the repository is a mock
    @Mock
    UserAccountRepository repository;

    @Mock
    HashMap<UUID, Long> tokenMap;

    @Test
        // test the login
    void itShouldReturnUnauthWhenUserIsWrong() {  // nothing here about pw being wrong
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        // ArgumentMatcher - it is a test that can be run on an argument
        // any() - always return true
        // eq(<expected>) - the passed argument must match <expected>
        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());  // invalid login?? // creates the matchers for us
        // lenient() - I know that a stubbing will not be called in the passing case,
        //      but I want to test for it anyway
        // Q: what is a stub again?

        // following is a branch within the mock
        // tests for the bug of having a username hard-coded in the controller source code
        // the following code makes sure that the ONLY time we return an empty Optional is above.
        //    Initially, I was confused that we are returning a new user account, but the better way
        //    to think of it is that we are returning something different from the empty Optional above so that we
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
        when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(expected));
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        // captor is capturing the UUID below:
        when(tokenMap.put(captor.capture(), eq(id))).thenReturn(0L);  // when that happens, capture the first arg so we can query it later
        // when controller calls login...matches what it returned
        final var token = service.login(username, password);
        assertEquals(token, captor.getValue());
    }

    @Test
        // test register method
    void itShouldReturnInvalidIfUsernameExists() {
        final String username = "some username";
        when(repository.findByUsername(username)).thenReturn(Optional.of(
                new UserAccount()));
        assertThrows(ResponseStatusException.class, () -> service.register(username, "", ""));
    }

    @Test
        // test register method
        // Q: why is this test not passing?
    void itShouldSaveANewOwnerAccountWhenOwnerRegistersFirstTime() {
        final String username = "some username";
        final String password = "some password";
        final String userType = "owner";
        when(repository.findByUserType(userType)).thenReturn(Optional.empty());
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password, userType));
        Assertions.assertDoesNotThrow(() -> service.register(username, password, userType));
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

    void itShouldSaveANewOwnerAccountWhenTheSuperOwnerRegistersAnotherOwner() {
        final String username = "some username";
        final String password = "some password";
        final String userType = "owner";
        when(repository.findByUserType(userType)).thenReturn(Optional.of(new UserAccount()));
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password, userType));
        Assertions.assertDoesNotThrow(() -> service.register(username, password, userType));
        assertEquals(new UserAccount(username, password, userType), captor.getValue());
    }

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
