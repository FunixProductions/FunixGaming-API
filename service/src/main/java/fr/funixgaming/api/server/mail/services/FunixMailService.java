package fr.funixgaming.api.server.mail.services;

import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.mail.services.ApiMailService;
import fr.funixgaming.api.server.mail.entities.FunixMail;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Service
public class FunixMailService extends ApiMailService {

    private final FunixMailCrudService mailCrudService;
    private final Queue<FunixMailDTO> mailQueue = new LinkedList<>();

    public FunixMailService(JavaMailSender javaMailSender,
                            FunixMailCrudService mailCrudService) {
        super(javaMailSender);
        this.mailCrudService = mailCrudService;

        for (final FunixMail mail : mailCrudService.getAllMailsNotSend()) {
            mailQueue.add(this.mailCrudService.getMapper().toDto(mail));
        }
    }

    public FunixMailDTO addMail(final FunixMailDTO request) {
        final FunixMailDTO res = this.mailCrudService.create(request);

        this.mailQueue.add(res);
        return res;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void processMails() {
        final List<FunixMailDTO> mails = new ArrayList<>();
        FunixMailDTO mailDTO = mailQueue.poll();
        int mailsSend = 0;

        while (mailDTO != null) {
            if (!mailDTO.isSend()) {
                try {
                    super.sendMail(mailDTO);
                    mailDTO.setSend(true);
                    mailsSend++;
                } catch (ApiException e) {
                    mailQueue.add(mailDTO);
                    log.error("Erreur lors de l'envoi d'un mail. Erreur : {}", e.getMessage(), e);
                    return;
                }

                mails.add(mailDTO);
            }

            mailDTO = mailQueue.poll();
        }

        this.mailCrudService.update(mails);
        if (mailsSend > 0) {
            log.info("{} mails envoyés.", mailsSend);
        }
    }

}
