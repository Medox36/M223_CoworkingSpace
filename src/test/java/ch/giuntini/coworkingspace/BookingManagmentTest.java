package ch.giuntini.coworkingspace;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import ch.giuntini.coworkingspace.model.UpdatingBooking;

import static io.restassured.RestAssured.given;

import io.quarkus.test.h2.H2DatabaseTestResource;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
@TestSecurity(user = "1", roles = {"User", "Admin"})
public class BookingManagmentTest {

  @Test
  @Order(1)
  public void getAllUsers() {
    given()
      .when().get("/booking")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(2)
  public void acceptBooking() {
    given()
      .when().get("/booking/accept/2")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(3)
  public void acceptNonExistentBooking() {
    given()
      .when().get("/booking/accept/4645646")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(4)
  public void addWishFeedbackToBooking() {
    UpdatingBooking booking = new UpdatingBooking();
    booking.setWishFeedback("Seat will be provided");

    given().contentType(ContentType.JSON).body(booking)
      .when().put("/booking/2")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(5)
  public void addWishFeedbackToNonExistentBooking() {
    UpdatingBooking booking = new UpdatingBooking();
    booking.setWishFeedback("Seat will be provided");

    given().contentType(ContentType.JSON).body(booking)
      .when().put("/booking/54872")
      .then()
        .statusCode(403);
  }

  @Test
  @Order(6)
  @TestSecurity(user = "2", roles = {"User"})
  public void addWishFeedbackToBookingAsMember() {
    UpdatingBooking booking = new UpdatingBooking();
    booking.setWishFeedback("Seat will be provided");

    given().contentType(ContentType.JSON).body(booking)
      .when().put("/booking/2")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(7)
  public void acceptAlreadyAcceptedBooking() {
    given()
      .when().get("/booking/accept/2");
    given()
      .when().get("/booking/accept/2")
      .then()
        .statusCode(304);
  }

  @Test
  @Order(8)
  public void declineBooking() {
    given()
      .when().get("/booking/decline/1")
      .then()
        .statusCode(200);
  }

  @Test
  @Order(9)
  public void declineNonExistentBooking() {
    given()
      .when().get("/booking/decline/467756")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(10)
  public void declineAlreadyDeclinedBooking() {
    given()
      .when().get("/booking/accept/1");
    given()
      .when().get("/booking/accept/1")
      .then()
        .statusCode(304);
  }

  @Test
  @Order(11)
  public void deleteNonExistentBooking() {
    given()
      .when().delete("/booking/465465")
      .then()
        .statusCode(404);
  }

  @Test
  @Order(12)
  public void deleteBooking() {
    given()
      .when().delete("/booking/1")
      .then()
        .statusCode(404);
  }
}