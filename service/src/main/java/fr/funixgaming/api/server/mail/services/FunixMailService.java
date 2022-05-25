package fr.funixgaming.api.server.mail.services;

import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.mail.services.ApiMailService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Getter
@Service
public class FunixMailService extends ApiMailService {

    private final Queue<FunixMailDTO> mailQueue = new LinkedList<>();

    public FunixMailService(JavaMailSender javaMailSender) {
        super(javaMailSender);
        //TODO get mails in db not sended and set in queue
    }

    public FunixMailDTO addMail(final FunixMailDTO request) {
        this.mailQueue.add(request);
        //TODO register in database of mail sended
        return request;
    }

    @Scheduled(fixedRate = 10000)
    public void processMails() {
        FunixMailDTO mailDTO = mailQueue.poll();

        while (mailDTO != null) {
            try {
                super.sendMail(mailDTO);
                //TODO set in database mail sended
                log.info("Mail envoyé : {}", mailDTO);
            } catch (ApiException e) {
                mailQueue.add(mailDTO);
                log.error("Erreur lors de l'envoi d'un mail. Erreur : {}", e.getMessage(), e);
                return;
            }

            mailDTO = mailQueue.poll();
        }
    }

}
