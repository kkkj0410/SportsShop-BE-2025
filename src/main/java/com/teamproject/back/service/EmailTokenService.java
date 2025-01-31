package com.teamproject.back.service;


import com.teamproject.back.dto.EmailTokenDTO;
import com.teamproject.back.entity.EmailToken;
import com.teamproject.back.repository.EmailTokenRepository;
import com.teamproject.back.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;
    private final JavaMailSender javaMailSender;
    private final int EXPIRE_MINUTES = 5;
    private final int AUTH_CODE_SIZE = 6;


    @Autowired
    public EmailTokenService(EmailTokenRepository emailTokenRepository, JavaMailSender javaMailSender) {
        this.emailTokenRepository = emailTokenRepository;
        this.javaMailSender = javaMailSender;
    }


    public boolean sendEmail(String email){
        EmailToken emailToken = createEmailToken(email);
        if(emailTokenRepository.save(emailToken) == null){
            log.info("{} : emailToken 저장 실패", email);
            return false;
        }

        try{
            javaMailSender.send(createMessage(email, emailToken));
        }catch(Exception e){
            log.error("{} : 메시지 전송 실패", email);
            return false;
        }

        return true;
    }


    public boolean vertify(EmailTokenDTO emailTokenDTO){
        EmailToken emailToken = emailTokenRepository.findByEmailAndAuthCode(emailTokenDTO.getEmail(), emailTokenDTO.getAuthCode());
        if(emailToken == null){
            return false;
        }

        if(emailToken.getExpiredTime().isBefore(LocalDateTime.now())){
            return false;
        }

        return true;
    }

    private SimpleMailMessage createMessage(String email, EmailToken emailToken){

        SimpleMailMessage message = new SimpleMailMessage();

        String title = "[스포츠 쇼핑몰] 회원 가입 인증 URL";
        String content =
                "이메일 인증 절차(아래 번호를 기입하세요)" +
                "\n\n" +
                emailToken.getAuthCode();


        message.setTo(email);
        message.setSubject(title);
        message.setText(content);

        return message;
    }


    private EmailToken createEmailToken(String email){

        return EmailToken.builder()
                .expiredTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES))
                .email(email)
                .authCode(RandomUtil.getRandom(AUTH_CODE_SIZE))
                .build();
    }


}
