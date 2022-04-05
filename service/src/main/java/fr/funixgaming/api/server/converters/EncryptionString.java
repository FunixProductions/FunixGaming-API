package fr.funixgaming.api.server.converters;

import fr.funixgaming.api.core.utils.ApiConverter;
import fr.funixgaming.api.core.utils.Encryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptionString implements ApiConverter<String> {

    private final Encryption encryption;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryption.convertToDatabase(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryption.convertToEntity(dbData);
    }

}
