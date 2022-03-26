package fr.funixgaming.api.server.converters;

import fr.funixgaming.api.server.configs.FunixApiConfig;
import fr.funixgaming.api.server.exceptions.FunixApiException;

import javax.persistence.Converter;

@Converter
public class EncryptionString extends Encryption<String> {
    public EncryptionString(FunixApiConfig funixApiConfig) throws FunixApiException {
        super(funixApiConfig);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return super.convertToDatabase(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return super.convertToEntity(dbData);
    }
}
