package fr.funixgaming.api.server.configs;

import com.funixproductions.core.tools.encryption.ApiConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptionString implements ApiConverter<String> {

    private final FunixGamingApiEncryption encryption;

    @Override
    public synchronized String convertToDatabaseColumn(String attribute) {
        return encryption.convertToDatabase(attribute);
    }

    @Override
    public synchronized String convertToEntityAttribute(String dbData) {
        return encryption.convertToEntity(dbData);
    }

}
