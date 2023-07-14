package ch.giuntini.coworkingspace;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import ch.giuntini.coworkingspace.model.UpdatingUser;

import static io.restassured.RestAssured.given;

import io.quarkus.test.h2.H2DatabaseTestResource;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
@TestSecurity(user = "1", roles = {"User", "Admin"})
public class UserManagementTest {
  @Test
  @Order(1)
  public void updateUser() {
    UpdatingUser user = new UpdatingUser();
    user.setFirstName("Hans");

    given().contentType(ContentType.JSON).body(user)
      .when().put("/booking/1")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(2)
  public void updateNonExistentUser() {
    UpdatingUser user = new UpdatingUser();
    user.setFirstName("Peter");

    given().contentType(ContentType.JSON).body(user)
      .when().put("/booking/5475431")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(3)
  public void deleteNonExistentUser() {
    given()
      .when().delete("/booking/5475431")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(4)
  public void deleteUser() {
    given()
      .when().delete("/booking/3")
      .then()
        .statusCode(404);
  }
}
