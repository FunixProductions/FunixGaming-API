package fr.funixgaming.api.core.utils.encryption;

import javax.persistence.AttributeConverter;

public interface ApiConverter<T> extends AttributeConverter<T, String> {
}
