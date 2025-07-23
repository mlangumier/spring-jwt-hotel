package fr.hb.mlang.hotel.booking;

import fr.hb.mlang.hotel.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String> {

  /**
   * Finds all {@link Booking} made by a specific {@link User}.
   *
   * @param user <code>User</code> for whom we want to get all bookings
   * @return the list of <code>bookings</code> made by the given <code>user</code>
   */
  List<Booking> findByUser(User user);
}
