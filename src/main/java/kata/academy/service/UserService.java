package kata.academy.service;

import kata.academy.model.Role;
import kata.academy.model.User;
import kata.academy.repository.RoleRepository;
import kata.academy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    private void postConstruct() {

        if(roleRepository.findByRole("ROLE_USER") == null) {
            Role role_user = new Role("ROLE_USER");
            roleRepository.save(role_user);
        }

        if(roleRepository.findByRole("ROLE_ADMIN") == null) {
            Role role_user = new Role("ROLE_ADMIN");
            roleRepository.save(role_user);
        }

        if(userRepository.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("$2a$12$uiRlDZshsSDsmBGGAYtXReQDUGsVQIgkDCKd7QSdlT/iI5QRXR9Vi");
            user.setName("User_name");
            user.setSurname("User_surname");
            user.setAge(11);
            user.setEmail("user@user.com");
            Role roleUser = roleRepository.findByRole("ROLE_USER");
            user.addRole(roleUser);
            userRepository.save(user);
        }
        if(userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("$2a$12$i.jqnF7TZB7F1.3E7ui64uo7QkKgFGThkJE/7bKHQT9GSzbbWyWNa");
            admin.setName("Admin_name");
            admin.setSurname("Admin_surname");
            admin.setAge(22);
            admin.setEmail("admin@admin.com");
            Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
            admin.addRole(roleAdmin);
            userRepository.save(admin);
        }
    }



    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }


    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }


    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
   }
}
