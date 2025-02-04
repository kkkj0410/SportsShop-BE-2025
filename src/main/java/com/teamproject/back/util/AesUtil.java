package com.teamproject.back.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
@Slf4j
public class AesUtil {

    private static final String ALGORITHM = "AES";
    @Value("${aes.secret}")
    private String SECRET;

    private static String SECRET_KEY;

    @PostConstruct
    public void init(){
        SECRET_KEY = SECRET;
    }

    public static String encrypt(String input){
        try{
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch(InvalidKeyException e){
            log.info("SECRET_KEY의 문자열 길이가 맞지 않습니다 -> 16, 24, 32");
            throw new IllegalArgumentException("SECRET_KEY의 문자열 길이가 맞지 않습니다 -> 16, 24, 32.", e);
        }catch(Exception e){
            log.info("암호화 실패");
            throw new SecurityException("암호화 실패", e);
        }

    }

    public static String decrypt(String encryptedInput) {

        try{
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedInput);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch(InvalidKeyException e){
            log.info("SECRET_KEY의 문자열 길이가 맞지 않습니다 -> 16, 24, 32");
            throw new IllegalArgumentException("SECRET_KEY의 문자열 길이가 맞지 않습니다 -> 16, 24, 32", e);
        }catch(BadPaddingException e){
            log.info("암호화에서 사용한 KEY 값과 다릅니다.");
            throw new SecurityException("암호화에서 사용한 KEY 값과 다릅니다.", e);
        } catch(Exception e){
            log.info("복호화 실패");
            throw new SecurityException("복호화 실패", e);
        }

    }

}
