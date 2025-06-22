package com.acme.ezpark.platform.user.application.internal;

import com.acme.ezpark.platform.user.domain.model.aggregates.User;
import com.acme.ezpark.platform.user.domain.model.queries.GetAllUsersQuery;
import com.acme.ezpark.platform.user.domain.model.queries.GetUserByEmailQuery;
import com.acme.ezpark.platform.user.domain.model.queries.GetUserByIdQuery;
import com.acme.ezpark.platform.user.domain.services.UserQueryService;
import com.acme.ezpark.platform.user.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}
