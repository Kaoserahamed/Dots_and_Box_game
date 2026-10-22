package com.makesquare.server.auth;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

/**
 * Keeps accounts in memory.
 *
 * <p>This is the storage the authentication endpoints run against until the database arrives.
 * It is deliberately the only place that knows accounts are not persisted: everything above it
 * talks to {@link UserRepository}.
 */
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, UserModel> byUsername = new ConcurrentHashMap<>();
    private final Map<Long, UserModel> byId = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public UserModel create(String username, String email, String passwordHash, String role) {
        UserModel user = new UserModel(
                nextId.getAndIncrement(),
                username,
                email,
                passwordHash,
                role,
                Instant.now());
        byUsername.put(user.username(), user);
        byId.put(user.id(), user);
        return user;
    }

    @Override
    public Optional<UserModel> findById(long id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return Optional.ofNullable(byUsername.get(username));
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return byUsername.values().stream()
                .filter(user -> user.email().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        return byUsername.containsKey(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return byUsername.values().stream()
                .anyMatch(user -> user.email().equalsIgnoreCase(email));
    }
}
