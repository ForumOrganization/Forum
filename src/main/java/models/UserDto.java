package models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "First name can't be empty.")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols.")
    private String firstName;

    @NotNull(message = "Last name can't be empty.")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols.")
    private String lastName;

    @NotNull(message = "Email must be unique.")
    private String email;

    @NotNull(message = "Password can't be empty.")
    @Size(min = 4, max = 32, message = "Password should be between 8 and 32 symbols.")
    private String password;

    public UserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}