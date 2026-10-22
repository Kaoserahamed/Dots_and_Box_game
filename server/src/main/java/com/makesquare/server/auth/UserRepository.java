package com.makesquare.server.auth;

import java.util.Optional;

/**
 * Where accounts are kept.
 *
 * <p>The interface is the seam that lets the storage change. The first implementation keeps
 * accounts in memory, which is enough to serve the authentication endpoints; the PostgreSQL
 * schema and its migrations arrive with the database work, and they replace this interface
 * without changing the service that uses it.
 */
public interface UserRepository {

    /**
     * Create an account and give it an identifier.
     *
     * @param username the name the player signs in with
     * @param email the address the account was registered with
     * @param passwordHash the hashed password
     * @param role the role the account holds
     * @return the stored account, with its identifier
     */
    UserModel create(String username, String email, String passwordHash, String role);

    /**
     * Find an account by its identifier.
     *
     * @param id the identifier of the account
     * @return the account, or empty when there is none
     */
    Optional<UserModel> findById(long id);

    /**
     * Find an account by the name it signs in with.
     *
     * @param username the name the player signs in with
     * @return the account, or empty when there is none
     */
    Optional<UserModel> findByUsername(String username);

    /**
     * Find an account by the address it was registered with.
     *
     * @param email the address the account was registered with
     * @return the account, or empty when there is none
     */
    Optional<UserModel> findByEmail(String email);

    /**
     * Say whether a name is already taken.
     *
     * @param username the name to check
     * @return true when the name is taken
     */
    boolean existsByUsername(String username);

    /**
     * Say whether an address is already registered.
     *
     * @param email the address to check
     * @return true when the address is registered
     */
    boolean existsByEmail(String email);
}
