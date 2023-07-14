package ch.giuntini.coworkingspace;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import ch.giuntini.coworkingspace.model.CreatingUser;
import ch.giuntini.coworkingspace.model.Credentials;

import static io.restassured.RestAssured.given;

import io.quarkus.test.h2.H2DatabaseTestResource;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
public class RegisterAndLoginTest {

  @Test
  @Order(1)
  public void testRegisterEndpoint() {
    CreatingUser user = new CreatingUser();
    user.setEmail("markus@quarkus.com");
    user.setPassword("passwod4");
    user.setFirstName("Markus");
    user.setLastName("Quarkus");

    given().contentType(ContentType.JSON).body(user)
      .when().post("/user/register")
      .then()
        .statusCode(201);
  }

  @Test
  @Order(2)
  public void testRegisterMissingCredentials() {
    CreatingUser user = new CreatingUser();
    user.setPassword("passwod4");
    user.setFirstName("Markus");
    user.setLastName("Quarkus");

    given().contentType(ContentType.JSON).body(user)
      .when().post("/user/register")
      .then()
        .statusCode(400);
  }

  @Test
  @Order(3)
  public void testRegisterEmailAlreadyInuse() {
    CreatingUser user = new CreatingUser();
    user.setEmail("georg@meier.com");
    user.setPassword("passwod1");
    user.setFirstName("Greorg");
    user.setLastName("Meier");

    given().contentType(ContentType.JSON).body(user)
      .when().post("/user/register")
      .then()
        .statusCode(400);
  }

  @Test
  @Order(4)
  public void testLoginEndpoint() {
    Credentials credentials = new Credentials();
    credentials.email = "georg@meier.com";
    credentials.email = "passwod1";
    
    given().contentType(ContentType.JSON).body(credentials)
      .when().delete("/user/login")
      .then()
        .statusCode(201);
  }

  @Test
  @Order(5)
  public void testInvalidloginCredentials() {
    Credentials credentials = new Credentials();
    credentials.email = "safasdf@sgagf.sfg";
    credentials.email = "aaaaaaaaa";
    
    given().contentType(ContentType.JSON).body(credentials)
      .when().delete("/user/login")
      .then()
        .statusCode(401);
    
  }
}
