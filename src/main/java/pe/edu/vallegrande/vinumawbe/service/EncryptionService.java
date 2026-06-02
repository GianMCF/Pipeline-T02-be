package pe.edu.vallegrande.vinumawbe.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${aes.secret}")
    private String secret;

    private SecretKeySpec secretKey;

    @PostConstruct
    public void init() {

        int length = secret.length();

        if (
                length != 16 &&
                        length != 24 &&
                        length != 32
        ) {

            throw new RuntimeException(
                    "AES secret must contain 16, 24 or 32 characters"
            );
        }

        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "AES"
        );
    }

    // =========================
    // ENCRYPT
    // =========================

    public String encrypt(String data) {

        try {

            Cipher cipher =
                    Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey
            );

            byte[] encrypted =
                    cipher.doFinal(
                            data.getBytes(StandardCharsets.UTF_8)
                    );

            return Base64
                    .getEncoder()
                    .encodeToString(encrypted);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error encrypting data"
            );
        }
    }

    // =========================
    // DECRYPT
    // =========================

    public String decrypt(String encryptedData) {

        try {

            Cipher cipher =
                    Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey
            );

            byte[] decoded =
                    Base64.getDecoder()
                            .decode(encryptedData);

            byte[] decrypted =
                    cipher.doFinal(decoded);

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            // COMPATIBILIDAD TEMPORAL
            // PARA DATOS ANTIGUOS

            return encryptedData;
        }
    }
}