package fr.funixgaming.api.server.mail.services;

import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.server.mail.entities.FunixMail;
import fr.funixgaming.api.server.mail.mappers.FunixMailMapper;
import fr.funixgaming.api.server.mail.repositories.FunixMailRepository;
import org.springframework.stereotype.Service;

@Service
public class FunixMailCrudService extends ApiService<FunixMailDTO, FunixMail, FunixMailMapper, FunixMailRepository> {

    public FunixMailCrudService(FunixMailMapper funixMailMapper,
                                FunixMailRepository funixMailRepository) {
        super(funixMailRepository, funixMailMapper);
    }

    public Iterable<FunixMail> getAllMailsNotSend() {
        return this.getRepository().findAllBySendFalse();
    }

}
