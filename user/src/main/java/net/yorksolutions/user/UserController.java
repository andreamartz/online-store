package net.yorksolutions.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.UUID;

// The methods in this class are "easy" to test now, because there is one branch in each
@RestController
@RequestMapping("/")
public class UserController {
    // Q: why is service not final?
    // A: because we need a setter for it (see setService method below)
    // Q: why do we need the setter for it?
    private UserService service;

    @Autowired
    public UserController(@NonNull UserService service) {
        this.service = service;
    }

    // no need for a secondary constructor since we only need a service AND we'll need it every time
    //    whether or not we're testing
    // in other words, in both the real env and testing env, we'll use the same constructor
    // 1. testing - when we test, we'll mock the service OR
    // 2. when Spring starts up it will autowire the service for us

    @GetMapping("/login")
    @CrossOrigin
    UUID login(@RequestParam String username, @RequestParam String password) {
        return service.login(username, password);
    }

    // register owners
//    @GetMapping("/register")
//    public void register(@RequestParam(required = false) UUID token, @RequestParam String username, @RequestParam String password, @RequestParam String userType) {
//        service.register(token, username, password, userType);
//    }

    @GetMapping("/isOwner")
    @CrossOrigin
    public boolean isOwner(@RequestParam UUID token) {
      return service.isOwner(token);
    }

    @GetMapping("/isAuthorized")
    @CrossOrigin
    public void isAuthorized(@RequestParam UUID token) {
        service.isAuthorized(token);
    }

    // setService is only used in the test file
    //     that's the only purpose of this setService function
    public void setService(UserService service) {
        this.service = service;
    }
}
