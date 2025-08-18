package fr.hb.mlang.hotel.config;

import fr.hb.mlang.hotel.booking.Booking;
import fr.hb.mlang.hotel.booking.BookingRepository;
import fr.hb.mlang.hotel.user.domain.Role;
import fr.hb.mlang.hotel.room.Room;
import fr.hb.mlang.hotel.room.RoomRepository;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component that generates data if the database is empty / doesn't have any users. This component
 * will only work in "dev" and "test" profile (see comment bellow).
 */
@Component
@Profile({"dev"})
//@Profile({"dev", "test"}) // Replace line above with this one after implementing Test files & test database setup
@RequiredArgsConstructor
@Log4j2
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final BookingRepository bookingRepository;
  private final PasswordEncoder encoder;
  private final LocalDate today = LocalDate.now();

  @Value("${app.load-data:false}")
  private boolean shouldLoad;

  @Override
  public void run(String... args) throws Exception {
    if (!shouldLoad) {
      return;
    }

    if (userRepository.count() == 0) {
      try {
        log.info("Generation data for the database...");

        User user1 = User
            .builder()
            .email("user@test.com")
            .password(encoder.encode("password"))
            .role(Role.USER)
            .verified(true)
            .build();
        User user2 = User
            .builder()
            .email("admin@test.com")
            .password(encoder.encode("password"))
            .role(Role.ADMIN)
            .verified(true)
            .build();
        userRepository.saveAll(List.of(user1, user2));

        Room room1 = new Room(null, "110", 2, 90.00, List.of());
        Room room2 = new Room(null, "415", 4, 220.00, List.of());
        roomRepository.saveAll(List.of(room1, room2));

        Booking booking1 = new Booking(
            null,
            2,
            today.plusDays(4),
            today.plusDays(6),
            room1.getPrice() * 2,
            user1,
            List.of(room1)
        );
        Booking booking2 = new Booking(
            null,
            3,
            today.plusDays(5),
            today.plusDays(6),
            room2.getPrice() * 2,
            user1,
            List.of(room2)
        );
        bookingRepository.saveAll(List.of(booking1, booking2));

        log.info("Data generated. Database is ready for use!");
      } catch (Exception e) {
        log.error("Couldn't persist data to the database: {}", e.getMessage());
      }
    } else {
      log.info("Database already contains data. Skipping data generation.");
    }
  }
}
