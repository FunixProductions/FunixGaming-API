package fr.funixgaming.api.core.mail.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class ApiMailEntity extends ApiEntity {

    @Column(nullable = false, name = "to_mail")
    private String to;

    @Column(nullable = false, name = "from_mail")
    private String from;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 10000)
    private String text;

}
