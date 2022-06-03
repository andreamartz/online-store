package net.yorksolutions.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserAccountRepository repository;

    private HashMap<UUID, Long> tokenMap;

    // we need the @Autowired below, bc we still have a case where this service
    //    will be instantiated by Spring and we still want Spring to provide
    //    the repository for us.
    @Autowired
    public UserService(@NonNull UserAccountRepository repository) {
        this.repository = repository;
        tokenMap = new HashMap<>();
    }

    public UserService(UserAccountRepository repository,
                       HashMap<UUID, Long> tokenMap) {
        this.repository = repository;
        this.tokenMap = tokenMap;
    }

    UUID login(String username, String password) {
        // check to see if the username exists
        // Search for a UserAccount with the given username and password
        Optional<UserAccount> result = repository.findByUsernameAndPassword(username, password);

        // If not found, inform the client that they are unauthorized
        if (result.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // If found:
        // Generate a token that is to be used for all future requests that are associated
        //     w/ this user account
        final UUID token = UUID.randomUUID();
        // Associate the generated token w/ the user account
        tokenMap.put(token, result.get().id);
        // Provide the generated token to the client for future use
        return token;  // // from now on, use this uuid to let me know who you are
    }

    // register owners
    public void register(UUID token, String username, String password, String userType) {
        // user not logged in (but is the user an owner or customer or neither?)

//        if (token.equals(null) && repository.findAll().)



//        if (userType.equals("owner"))
//            if (repository.findByUserType(userType).isEmpty())
//                repository.save(new UserAccount(username, password, userType));
//            else
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//
////            if (repository.findByUserType())
//        }
//        // ...isPresent: If a value is present, returns true, otherwise false.
//        if (repository.findByUsername(username).isPresent())
//            throw new ResponseStatusException(HttpStatus.CONFLICT);
//
//        UserAccount newUser = new UserAccount(username, password, userType);
//
//        repository.save(newUser);
    }

    public boolean isOwner(UUID token) {
        Optional<UserAccount> mightBeUser;
        // throw error if tokenMap does not contain token
        if (!tokenMap.containsKey(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Long id = tokenMap.get(token);
        mightBeUser = repository.findById(id);
//
        // throw error if mightBeUser is empty
        if (mightBeUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        UserAccount user = mightBeUser.get();
//
//        // return whether user is owner or not
        if (user.userType == "owner") {
            return true;
        } else {
            return false;
        }
    }

    public void isAuthorized(UUID token) {
        if (tokenMap.containsKey(token))
            return;

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public void setRepository(UserAccountRepository repository) {
        this.repository = repository;
    }

    public void setTokenMap(HashMap<UUID, Long> tokenMap) {
        this.tokenMap = tokenMap;
    }
}
