package ch.giuntini.coworkingspace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "application_user")
@NamedQueries(value = { 
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM application_user AS u WHERE u.email = :email") 
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(readOnly = true)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(length = 127)
    private String firstName;

    @Column(length = 127)
    private String lastName;

    @Column(nullable = false, length = 32)
    private String salt;

    @Column(nullable = false, length = 7)
    @ColumnDefault(value = "'VISITOR'")
    @Enumerated(value = EnumType.STRING)
    private UserRole role;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }    

    public static User ofCreatingUser(CreatingUser creatingUser) {
        User user = new User();
        user.setEmail(creatingUser.getEmail());
        user.setFirstName(creatingUser.getFirstName());
        user.setLastName(creatingUser.getLastName());
        user.setPassword(creatingUser.getPassword());
        return user;
    }
}
