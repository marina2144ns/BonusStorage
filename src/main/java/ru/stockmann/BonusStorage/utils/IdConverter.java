package ru.stockmann.BonusStorage.utils;


import jakarta.persistence.AttributeConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

public class IdConverter implements AttributeConverter<UUID, byte[]> {


@Override
public byte[] convertToDatabaseColumn(UUID uuid) {
        if (uuid == null) {
        return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
        }
@Override
public UUID convertToEntityAttribute(byte[] bytes) {
        if (bytes == null) {
        return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
        }

}
