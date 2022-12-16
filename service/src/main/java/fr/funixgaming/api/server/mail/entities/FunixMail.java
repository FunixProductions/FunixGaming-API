package fr.funixgaming.api.server.mail.entities;

import fr.funixgaming.api.core.mail.entities.ApiMailEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "funix_api_mails")
public class FunixMail extends ApiMailEntity {

    private boolean send;

}
