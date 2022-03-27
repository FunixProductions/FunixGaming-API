package fr.funixgaming.api.server.user.repositories;

import fr.funixgaming.api.core.repositories.ApiRepository;
import fr.funixgaming.api.server.user.entities.UserToken;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends ApiRepository<UserToken> {
}
