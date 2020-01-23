package com.testExam.authenticationSystem.utilities;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
/**
 * This is a ready made solution I added as it is.
 * I haven't found a better solution or a third party library to encrypt a column in the data base.
 */
public class CryptoConverter implements AttributeConverter<String, String> {
    @Value("${encryption_token}")
    public String TOKEN;
    private static final String keyAlgorithm = "AES";
    private static final String encryptAlgorithm = "AES/ECB/PKCS5Padding";

    public CryptoConverter() {
    }

    public String convertToDatabaseColumn(String credentialsPassword) {
        if (credentialsPassword != null) {
            SecretKeySpec key = new SecretKeySpec(this.TOKEN.getBytes(), "AES");

            try {
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                c.init(1, key);
                return (new Base64()).encodeAsString(c.doFinal(credentialsPassword.getBytes()));
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            return null;
        }
    }

    public String convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            SecretKeySpec key = new SecretKeySpec(this.TOKEN.getBytes(), "AES");

            try {
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                c.init(2, key);
                return new String(c.doFinal((new Base64()).decode(dbData)));
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            return null;
        }
    }
}
