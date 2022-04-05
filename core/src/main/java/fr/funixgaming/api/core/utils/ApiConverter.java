package fr.funixgaming.api.core.utils;

import javax.persistence.AttributeConverter;

public interface ApiConverter<T> extends AttributeConverter<T, String> {
}
