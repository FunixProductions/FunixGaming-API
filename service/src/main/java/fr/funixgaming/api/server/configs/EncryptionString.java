package fr.funixgaming.api.server.configs;

import fr.funixgaming.api.core.utils.encryption.ApiConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptionString implements ApiConverter<String> {

    private final FunixApiEncryption encryption;

    @Override
    public synchronized String convertToDatabaseColumn(String attribute) {
        return encryption.convertToDatabase(attribute);
    }

    @Override
    public synchronized String convertToEntityAttribute(String dbData) {
        return encryption.convertToEntity(dbData);
    }

}
