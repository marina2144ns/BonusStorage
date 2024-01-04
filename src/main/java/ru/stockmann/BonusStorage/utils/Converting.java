package ru.stockmann.BonusStorage.utils;


import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Converting {

    public static String convertUUIDToBinary(UUID uuid) {
        // Создаем ByteBuffer и записываем в него mostSignificant и leastSignificant
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        // Получаем массив байт
        byte[] binaryData = buffer.array();

        // Кодируем массив байт в шестнадцатеричную строку с префиксом "0x"
        return "0x" + Hex.encodeHexString(binaryData).toUpperCase();
    }
}

