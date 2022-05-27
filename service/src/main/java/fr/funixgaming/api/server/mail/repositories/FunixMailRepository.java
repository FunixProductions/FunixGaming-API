package fr.funixgaming.api.server.mail.repositories;

import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.server.mail.entities.FunixMail;
import org.springframework.stereotype.Repository;

@Repository
public interface FunixMailRepository extends ApiRepository<FunixMail> {
    Iterable<FunixMail> findAllBySendFalse();
}
