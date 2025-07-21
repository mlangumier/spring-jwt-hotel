package fr.hb.mlang.hotel.room;

import fr.hb.mlang.hotel.booking.Booking;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "capacity", nullable = false)
  private int capacity;

  @Column(name = "price", nullable = false)
  private Double price;

  @ManyToMany(mappedBy = "rooms")
  private List<Booking> bookings = new ArrayList<>();
}
