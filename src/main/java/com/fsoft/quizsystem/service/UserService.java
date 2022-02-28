package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.filter.UserFilter;
import com.fsoft.quizsystem.object.dto.mapper.UserMapper;
import com.fsoft.quizsystem.object.dto.request.UserRequest;
import com.fsoft.quizsystem.object.entity.es.UserES;
import com.fsoft.quizsystem.object.entity.jpa.Category;
import com.fsoft.quizsystem.object.entity.jpa.Role;
import com.fsoft.quizsystem.object.entity.jpa.User;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.object.oauth2.OAuth2UserInfo;
import com.fsoft.quizsystem.repository.es.UserEsRepository;
import com.fsoft.quizsystem.repository.jpa.UserRepository;
import com.fsoft.quizsystem.repository.jpa.spec.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final ElasticsearchOperations restTemplate;

    private final UserRepository userRepository;
    private final UserEsRepository userEsRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }

    public Page<User> findAllUsers(UserFilter filter) {
        Page<User> users;

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (!ObjectUtils.isEmpty(filter.getUsername())) {
            query.should(QueryBuilders.termQuery("username", filter.getUsername()));
        }
        if (!ObjectUtils.isEmpty(filter.getFullName())) {
            query.should(QueryBuilders.matchQuery("full_name", filter.getFullName()));
        }
        if (!ObjectUtils.isEmpty(filter.getEmail())) {
            query.should(QueryBuilders.matchQuery("email", filter.getEmail()));
        }
        if (!ObjectUtils.isEmpty(filter.getPhoneNumber())) {
            query.should(QueryBuilders.matchQuery("phone_number", filter.getPhoneNumber()));
        }
        if (!ObjectUtils.isEmpty(filter.getActive())) {
            query.should(QueryBuilders.matchBoolPrefixQuery("active", filter.getActive()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(filter.getPagination().getPageAndSort())
                .build();
        SearchHits<UserES> hits = restTemplate.search(searchQuery, UserES.class,
                                                      IndexCoordinates.of("users"));
        if (hits.hasSearchHits()) {
            SearchPage<UserES> page = SearchHitSupport.searchPageFor(hits, filter.getPagination()
                                                                                 .getPageAndSort());

            users = page.map(SearchHit::getContent).map(userMapper::esEntityToJpa);
        } else {
            Specification<User> specification = UserSpecification.getSpecification(filter);
            users = userRepository.findAll(specification, filter.getPagination().getPageAndSort());
        }

        return users;
    }

    public User findUserById(long id) {
        Optional<UserES> optional = userEsRepository.findById(id);

        if (optional.isPresent()) {
            return userMapper.esEntityToJpa(optional.get());
        } else {
            return userRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any user with id " + id));
        }

    }

    public User findUserByUsername(String username) {
        Optional<UserES> optional = userEsRepository.findByUsername(username);

        if (optional.isPresent()) {
            return userMapper.esEntityToJpa(optional.get());
        } else {
            return userRepository.findByUsername(username).orElseThrow(
                    () -> new BadCredentialsException("User " + username + " not found"));
        }
    }

    public User findUserByEmail(String email) {
        Optional<UserES> optional = userEsRepository.findByEmail(email);

        if (optional.isPresent()) {
            return userMapper.esEntityToJpa(optional.get());
        } else {
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new BadCredentialsException("User " + email + " not found"));
        }
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
        return !(userEsRepository.existsByUsername(username)  || userRepository.existsByUsername(username));
    }

    public boolean validateConcurrentEmail(String email) {
        return !(userEsRepository.existsByEmail(email) || userRepository.existsByEmail(email));
    }

    public void updateUserFavoriteCategories(Long userId, Set<Long> categoryIds) {
        User user = this.findUserById(userId);
        if (ObjectUtils.isEmpty(categoryIds)) {
            user.getFavoriteCategories().clear();
        } else {
            Set<Category> set = categoryIds.stream()
                                           .map(categoryService::findCategoryById)
                                           .collect(Collectors.toSet());
            user.setFavoriteCategories(set);
        }
        userRepository.save(user);
    }

    public void subscribeNotification(Long id) {
        User user = this.findUserById(id);
        user.setNotification(true);
        userRepository.save(user);
    }

    public void unsubscribeNotification(Long id) {
        User user = this.findUserById(id);
        user.setNotification(false);
        userRepository.save(user);
    }
}
