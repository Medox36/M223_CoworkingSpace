package ch.giuntini.coworkingspace;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.giuntini.coworkingspace.model.Booking;
import ch.giuntini.coworkingspace.model.BookingStatus;
import ch.giuntini.coworkingspace.model.Section;
import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.model.UserRole;
import ch.giuntini.coworkingspace.util.PasswordUtil;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;

@IfBuildProfile("test")
@ApplicationScoped
public class TestDataService {

  @Inject
  EntityManager entityManager;

  @Transactional
  void generateTestData(@Observes StartupEvent event) {
    User user1 = new User();
    user1.setEmail("georg@meier.com");
    user1.setFirstName("Georg");
    user1.setLastName("Meier");
    user1.setRole(UserRole.ADMIN);
    String[] arr1;
    try {
      arr1 = PasswordUtil.hash("password1");
      user1.setPassword(arr1[0]);
      user1.setSalt(arr1[1]);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }

    User user2 = new User();
    user2.setEmail("max@schneider.com");
    user2.setFirstName("Max");
    user2.setLastName("Schneider");
    user2.setRole(UserRole.MEMBER);
    String[] arr2;
    try {
      arr2 = PasswordUtil.hash("password2");
      user2.setPassword(arr2[0]);
      user2.setSalt(arr2[1]);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }

    User user3 = new User();
    user3.setEmail("maria@swanson.com");
    user3.setFirstName("Maria");
    user3.setLastName("Swanson");
    user3.setRole(UserRole.MEMBER);
    String[] arr3;
    try {
      arr3 = PasswordUtil.hash("password3");
      user3.setPassword(arr3[0]);
      user3.setSalt(arr3[1]);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }

    entityManager.persist(user1);
    entityManager.persist(user2);
    entityManager.persist(user3);


    Booking booking1 = new Booking();
    booking1.setStartTime(LocalDateTime.of(2023, 7, 4, 8, 0, 0));
    booking1.setEndTime(LocalDateTime.of(2023, 7, 4, 16, 0, 0));
    booking1.setStatus(BookingStatus.PENDING);
    booking1.setWish("Window seat");
    booking1.setSection(Section.UNDEFINED);
    booking1.setBooker(user1);
    
    Booking booking2 = new Booking();
    booking2.setStartTime(LocalDateTime.of(2023, 7, 12, 8, 0, 0));
    booking2.setEndTime(LocalDateTime.of(2023, 7, 12, 16, 0, 0));
    booking2.setStatus(BookingStatus.PENDING);
    booking2.setWish("Seat near buffet");
    booking2.setSection(Section.C3);
    booking2.setBooker(user2);

    Booking booking3 = new Booking();
    booking3.setStartTime(LocalDateTime.of(2023, 7, 8, 8, 0, 0));
    booking3.setEndTime(LocalDateTime.of(2023, 7, 8, 12, 0, 0));
    booking3.setStatus(BookingStatus.PENDING);
    booking3.setWish("Window seat and blue Table");
    booking3.setSection(Section.B);
    booking3.setBooker(user3);

    Booking booking4 = new Booking();
    booking4.setStartTime(LocalDateTime.of(2023, 2, 2, 8, 0, 0));
    booking4.setEndTime(LocalDateTime.of(2023, 2, 2, 16, 0, 0));
    booking4.setStatus(BookingStatus.PENDING);
    booking4.setWish("Blue Tabel");
    booking4.setSection(Section.A1);
    booking4.setBooker(user3);

    Booking booking5 = new Booking();
    booking5.setStartTime(LocalDateTime.of(2023, 2, 3, 8, 0, 0));
    booking5.setEndTime(LocalDateTime.of(2023, 2, 3, 16, 0, 0));
    booking5.setStatus(BookingStatus.ACCEPTED);
    booking5.setSection(Section.C2);
    booking5.setBooker(user3);

    entityManager.persist(booking1);
    entityManager.persist(booking2);
    entityManager.persist(booking3);
    entityManager.persist(booking4);
    entityManager.persist(booking5);
  }
}
