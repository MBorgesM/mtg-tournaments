package br.com.mborgesm.mtgtournaments.services;

import br.com.mborgesm.mtgtournaments.exceptions.EmailInUseException;
import br.com.mborgesm.mtgtournaments.models.ERole;
import br.com.mborgesm.mtgtournaments.models.Role;
import br.com.mborgesm.mtgtournaments.models.User;
import br.com.mborgesm.mtgtournaments.payload.request.LoginRequest;
import br.com.mborgesm.mtgtournaments.payload.request.SignupRequest;
import br.com.mborgesm.mtgtournaments.repositories.RoleRepository;
import br.com.mborgesm.mtgtournaments.repositories.UserRepository;
import br.com.mborgesm.mtgtournaments.security.jwt.JwtUtils;
import br.com.mborgesm.mtgtournaments.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailInUseException("User not created: Email is already in use!");
        }
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new EmailInUseException("User not created: Username is already in use!");
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);
    }

    public Object[] login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new Object[]{jwt, userDetails};
    }
}
