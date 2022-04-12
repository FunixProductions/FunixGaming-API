package fr.funixgaming.api.server.utils.mail;

import fr.funixgaming.api.core.mail.dtos.MailDTO;
import fr.funixgaming.api.core.mail.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class MailResource {

    private final MailService mailService;

    @PostMapping
    public MailDTO create(@RequestBody @Valid final MailDTO request) {
        mailService.sendMail(request);
        return request;
    }

}
