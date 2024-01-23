package ru.stockmann.BonusStorage.utils;


import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stockmann.BonusStorage.BonusStorageApplication;

import java.nio.ByteBuffer;
import java.util.UUID;

public class IdConverter implements AttributeConverter<UUID, byte[]> {

        private static final Logger logger = LoggerFactory.getLogger(BonusStorageApplication.class);

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
        public UUID convertToEntityAttribute(byte[] binaryData) {
                // Проверка наличия достаточного количества байт в массиве
                if (binaryData.length != 16) {
                        throw new IllegalArgumentException("Неверный размер массива. Ожидается 16 байт.");
                }

                // Извлечение 4 байт и создание UUID
                long mostSigBits = 0;
                for (int i = 0; i < 8; i++) {
                        mostSigBits = (mostSigBits << 8) | (binaryData[i] & 0xFF);
                }

                // Извлечение следующих 4 байт и создание UUID
                long leastSigBits = 0;
                for (int i = 8; i < 16; i++) {
                        leastSigBits = (leastSigBits << 8) | (binaryData[i] & 0xFF);
                }

                // Создание UUID
                UUID uuid = new UUID(mostSigBits, leastSigBits);

                logger.error("uuid ="+uuid.toString());
                return uuid;

        }
}
