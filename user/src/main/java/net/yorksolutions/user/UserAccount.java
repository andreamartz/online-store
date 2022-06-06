package net.yorksolutions.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

// Entity is a Java Class that represents a Table in a Database
// An object of type <insert entity here> represents a row in that table
// I think that this class is what provides
@Entity
// an entity requires an explicit constructor that takes no arguments
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @JsonProperty
    String userType;

    @JsonProperty
    String username;

    @JsonProperty
    String password;

    public UserAccount(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // this is the constructor needed by the entity
    //    would this ever contain a body? not sure
    public UserAccount() {

    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserAccount that = (UserAccount) o;
//        return Objects.equals(id, that.id) && Objects.equals(userType, that.userType) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userType, username, password);
    }

    public void setId(Long id) {
        this.id = id;
    }

// used in UserControllerTests.java

}
