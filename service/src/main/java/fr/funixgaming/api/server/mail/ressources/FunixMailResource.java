package fr.funixgaming.api.server.mail.ressources;

import fr.funixgaming.api.client.mail.clients.FunixMailClient;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import fr.funixgaming.api.server.mail.services.FunixMailCrudService;
import fr.funixgaming.api.server.mail.services.FunixMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class FunixMailResource implements FunixMailClient {

    private final FunixMailCrudService crudService;
    private final FunixMailService mailService;

    @Override
    public FunixMailDTO sendMail(FunixMailDTO request) {
        return mailService.addMail(request);
    }

    @Override
    public FunixMailDTO getMailById(String id) {
        final FunixMailDTO mailDTO = crudService.findById(id);

        if (mailDTO != null) {
            return mailDTO;
        } else {
            throw new ApiNotFoundException(String.format("Le mail %s n'existe pas.", id));
        }
    }
}
