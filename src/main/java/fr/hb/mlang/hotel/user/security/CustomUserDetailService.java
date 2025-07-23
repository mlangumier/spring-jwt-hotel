package fr.hb.mlang.hotel.user.security;

import fr.hb.mlang.hotel.user.domain.CustomUserDetails;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

    return new CustomUserDetails(user);
  }
}
