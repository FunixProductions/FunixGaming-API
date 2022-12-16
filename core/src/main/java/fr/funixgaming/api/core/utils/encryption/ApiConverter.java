package fr.funixgaming.api.core.utils.encryption;

import jakarta.persistence.AttributeConverter;

public interface ApiConverter<T> extends AttributeConverter<T, String> {
}
