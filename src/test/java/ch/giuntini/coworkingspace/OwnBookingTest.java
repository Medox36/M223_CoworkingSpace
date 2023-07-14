package ch.giuntini.coworkingspace;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import ch.giuntini.coworkingspace.model.CreatingBooking;
import ch.giuntini.coworkingspace.model.UpdatingBooking;

import static io.restassured.RestAssured.given;

import java.time.LocalDateTime;

import io.quarkus.test.h2.H2DatabaseTestResource;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
@TestSecurity(user = "3", roles = {"User"})
public class OwnBookingTest {

  @Test
  @Order(1)
  @TestSecurity(authorizationEnabled = false)
  public void unauthorizedAPICall() {
    given()
      .when().get("/booking/my")
      .then()
        .statusCode(401);
  }

  @Test
  @Order(2)
  public void getAllMyBookings() {
    given()
      .when().get("/booking/my")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(3)
  public void getOneOfMyBookings() {
    given()
      .when().get("/booking/3")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(4)
  public void getBookingOfotherUser() {
    given()
      .when().get("/booking/2")
      .then()
        .statusCode(403);
  }

  @Test
  @Order(5)
  public void getNonExistentBooking() {
    given()
      .when().get("/booking/35498")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(6)
  public void cancelMyBooking() {
    given()
      .when().delete("/booking/cancel/3")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(7)
  public void cancelNonExistentBooking() {
    given()
      .when().delete("/booking/cancel/564654")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(8)
  public void cancelBookingOfOtheruser() {
    given()
      .when().delete("/booking/cancel/2")
      .then()
        .statusCode(403);
  }

  @Test
  @Order(9)
  public void cancelBookingInThePast() {
    given()
      .when().delete("/booking/cancel/4")
      .then()
        .statusCode(400);
  }

  @Test
  @Order(10)
  public void createBooking() {
    CreatingBooking booking = new CreatingBooking();
    booking.setStartTime(LocalDateTime.of(2024, 01, 01, 8, 0, 0 ));
    booking.setEndTime(LocalDateTime.of(2024, 01, 01, 16, 0, 0 ));

    given().contentType(ContentType.JSON).body(booking)
      .when().post("/booking")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(11)
  public void createBadBooking() {
    CreatingBooking booking = new CreatingBooking();
    booking.setStartTime(LocalDateTime.of(2022, 01, 01, 8, 0, 0 ));
    booking.setEndTime(LocalDateTime.of(2020, 01, 01, 16, 0, 0 ));

    given().contentType(ContentType.JSON).body(booking)
      .when().post("/booking")
      .then()
        .statusCode(400);
  }

  @Test
  @Order(12)
  public void addWishToBooking() {
    UpdatingBooking booking = new UpdatingBooking();
    booking.setWish("Something I desire");

    given().contentType(ContentType.JSON).body(booking)
      .when().put("/booking/3")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(13)
  public void addWishToNonPendingBooking() {
    CreatingBooking booking = new CreatingBooking();
    booking.setWish("would like some water-cans");

    given().contentType(ContentType.JSON).body(booking)
      .when().put("/booking/3")
      .then()
        .statusCode(400);
  }
}
