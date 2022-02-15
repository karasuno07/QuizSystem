package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.filter.UserFilter;
import com.fsoft.quizsystem.object.dto.mapper.UserMapper;
import com.fsoft.quizsystem.object.dto.request.UserRequest;
import com.fsoft.quizsystem.object.entity.Role;
import com.fsoft.quizsystem.object.entity.User;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.object.oauth2.OAuth2UserInfo;
import com.fsoft.quizsystem.repository.UserRepository;
import com.fsoft.quizsystem.repository.spec.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;
    private final CloudinaryService cloudinaryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }

    public Page<User> findAllUsers(UserFilter filter) {
        Specification<User> specification = UserSpecification.getSpecification(filter);
        return userRepository.findAll(specification, filter.getPagination().getPageAndSort());
    }

    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any user with id " + id));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new BadCredentialsException("User " + username + " not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new BadCredentialsException("User " + email + " not found"));
    }

    public User createUser(UserRequest requestBody) {
        User user = userMapper.userRequestToEntity(requestBody);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!ObjectUtils.isEmpty(requestBody.getRoleId())) {
            Role role = roleService.findRoleById(requestBody.getRoleId());
            user.setRole(role);
        }
        if (!ObjectUtils.isEmpty(requestBody.getImageFile())) {
            String image = cloudinaryService.uploadImage(null, requestBody.getImageFile());
            if (image != null) user.setImage(image);
        }

        return userRepository.save(user);
    }

    public User createUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setFullName(oAuth2UserInfo.getFirstName() + " " + oAuth2UserInfo.getLastName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImage(oAuth2UserInfo.getImageUrl());
        user.setUsername(oAuth2UserInfo.getUsername());
        user.setActive(true);
//        user.setRole(roleService.findRoleByName(SystemRole.INSTRUCTOR));
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));

        return userRepository.save(user);
    }


    public User updateUser(long id, UserRequest requestBody) {
        User user = this.findUserById(id);
        userMapper.updateEntity(user, requestBody);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!ObjectUtils.isEmpty(requestBody.getRoleId())) {
            Role role = roleService.findRoleById(requestBody.getRoleId());
            user.setRole(role);
        }
        if (!ObjectUtils.isEmpty(requestBody.getImageFile())) {
            String image = cloudinaryService.uploadImage(user.getImage(), requestBody.getImageFile());
            if (image != null) user.setImage(image);
        }

        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        User user = this.findUserById(id);

        userRepository.delete(user);
    }

    public void deactivateUser(long id) {
        User user = this.findUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public void activateUser(long id) {
        User user = this.findUserById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    public boolean validateConcurrentUsername(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean validateConcurrentEmail(String email) {
        return !userRepository.existsByEmail(email);
    }
}
