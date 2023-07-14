package ch.giuntini.coworkingspace.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CreatingUser {

    @NotNull
    @Length(max = 255)
    private String email;

    @NotNull
    private String password;

    @Length(max = 127)
    private String firstName;

    @Length(max = 127)
    private String lastName;


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
