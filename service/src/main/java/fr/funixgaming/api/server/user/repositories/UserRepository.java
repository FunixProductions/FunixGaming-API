package fr.funixgaming.api.server.user.repositories;

import fr.funixgaming.api.core.repositories.ApiRepository;
import fr.funixgaming.api.server.user.entities.User;

import java.util.Optional;

public interface UserRepository extends ApiRepository<User> {
    Optional<User> findByUsername(String username);
}
