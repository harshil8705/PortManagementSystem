package port.management.system.security.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.model.User;
import port.management.system.repository.UserRepository;

@Component
public class UserUtil {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<User> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {

            String username = authentication.getName();

            User loggedInUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

            return new ResponseEntity<>(loggedInUser, HttpStatus.FOUND);

        } else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }

    }

}
