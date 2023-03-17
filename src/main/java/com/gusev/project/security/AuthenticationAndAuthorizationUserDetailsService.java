package com.gusev.project.security;

import com.gusev.project.domain.Administration;
import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.User;
import com.gusev.project.repository.AdministrationRepository;
import com.gusev.project.repository.TrainerRepository;
import com.gusev.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationAndAuthorizationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final AdministrationRepository administrationRepository;

    @Autowired
    public AuthenticationAndAuthorizationUserDetailsService(UserRepository userRepository, TrainerRepository trainerRepository, AdministrationRepository administrationRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.administrationRepository = administrationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        Optional<Trainer> trainerOpt = trainerRepository.findByUsername(username);
        Optional<Administration> admin = administrationRepository.findByUsername(username);
        if (admin.isPresent()) {
            Administration adm = admin.get();
            return getUserDetails(adm.getUsername(), adm.getPassword(), adm.isEnabled(), adm.getRole().getName());
        }
        if (userOpt.isEmpty() && trainerOpt.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            return getUserDetails(user.getUsername(), user.getPassword(), user.isEnabled(), user.getRole().getName());
        }
        Trainer trainer = trainerOpt.get();
        return getUserDetails(trainer.getUsername(), trainer.getPassword(), trainer.isEnabled(), trainer.getRole().getName());
    }

    private org.springframework.security.core.userdetails.User getUserDetails(String username, String password, boolean enabled, String roleName) {
        return new org.springframework.security.core.userdetails.User(
                username,
                password,
                enabled, true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(roleName)));
    }
}