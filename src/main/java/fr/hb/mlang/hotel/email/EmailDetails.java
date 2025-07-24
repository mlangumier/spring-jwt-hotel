package fr.hb.mlang.hotel.email;

import fr.hb.mlang.hotel.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EmailDetails {

  @NonNull
  private User recipient;

  @NonNull
  private String subject;

  @NonNull
  private String messageBody;

  private String attachment;
}
