package fr.funixgaming.api.client.mail.dtos;

import fr.funixgaming.api.core.mail.dtos.ApiMailDTO;

public class FunixMailDTO extends ApiMailDTO {

    @Override
    public String getTo() {
        return String.format("Mail: from %s to %s subject %s", super.getFrom(), super.getTo(), super.getSubject());
    }
}
