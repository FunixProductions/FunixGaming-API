package fr.funixgaming.api.client.mail.dtos;

import fr.funixgaming.api.core.mail.dtos.ApiMailDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunixMailDTO extends ApiMailDTO {

    private boolean send;

    @Override
    public String toString() {
        return String.format("Mail: from %s to %s subject %s", super.getFrom(), super.getTo(), super.getSubject());
    }
}
