package fr.hb.mlang.hotel.user;

import fr.hb.mlang.hotel.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Couldn't find user with email: " + email));
  }

  public User updateUser(User user) {
    return userRepository.save(user);
  }
}
