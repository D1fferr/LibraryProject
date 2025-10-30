package com.Library.UserService.services;

import com.Library.UserService.models.AuthUser;
import com.Library.UserService.repositories.AuthUserRepository;
import com.Library.UserService.util.UsersNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final AuthUserService authUserService;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final AuthUserRepository authUserRepository;

    private static final String RESET_CODE_PREFIX = "password_reset:";
    private final PasswordEncoder passwordEncoder;


    public void sendResetCode(String param){
        AuthUser authUser = authUserService.findByParam(param)
                .orElseThrow(()-> new UsersNotFoundException("User not found"));

        String code = generateCode();

        String key = RESET_CODE_PREFIX + authUser.getId();
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, code, Duration.ofMinutes(15));

        emailService.send(authUser, code);
    }
    private boolean validateCode(String param, String code){
        AuthUser authUser = authUserService.findByParam(param)
                .orElseThrow(()-> new UsersNotFoundException("User not found"));
        String key = RESET_CODE_PREFIX + authUser.getId();
        String sentCode = redisTemplate.opsForValue().get(key);

        return code.equals(sentCode);
    }
    public void resetPassword(String param, String code, String newPassword){
        if (!validateCode(param, code))
            throw new BadCredentialsException("Invalid reset code");

        AuthUser authUser = authUserService.findByParam(param)
                .orElseThrow(()-> new UsersNotFoundException("User not found"));

        authUser.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(authUser);

        String key = RESET_CODE_PREFIX + authUser.getId();
        redisTemplate.delete(key);
    }
    private String generateCode(){
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
