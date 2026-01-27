package com.Library.UserService.services;

import com.Library.UserService.models.AuthUser;
import com.Library.UserService.repositories.AuthUserRepository;
import com.Library.UserService.util.FailedToConnectWithRedisException;
import com.Library.UserService.util.FailedToConnectWithUserServiceException;
import com.Library.UserService.util.UsersNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
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
        log.info("Trying to find user to send a reset code. Searched: '{}'", param);
        Optional<AuthUser> optionalAuthUser = authUserService.findByParam(param);
        if (optionalAuthUser.isEmpty()){
            log.warn("The user not found: '{}'", param);
            throw new UsersNotFoundException("The user not found");
        }

        AuthUser authUser = optionalAuthUser.get();
        String code = generateCode();

        String key = RESET_CODE_PREFIX + authUser.getId();
        try{
            log.info("Trying to save the generated code to redis");
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, code, Duration.ofMinutes(15));
            log.info("The generated code saved to redis");
        }catch (Exception e){
            log.warn("Failed to save the generated code to redis");
            throw new FailedToConnectWithRedisException(e.getMessage());
        }
        emailService.send(authUser, code);
    }

    private boolean validateCode(String param, String code){
        log.info("Trying to find user to validate a reset code. Searched: '{}'", param);
        Optional<AuthUser> optionalAuthUser = authUserService.findByParam(param);
        if (optionalAuthUser.isEmpty()){
            log.warn("The user not found: '{}'", param);
            throw new UsersNotFoundException("The user not found");
        }
        AuthUser authUser = optionalAuthUser.get();

        String key = RESET_CODE_PREFIX + authUser.getId();
        try {
            log.info("Trying to get the generated code from redis");
            String sentCode = redisTemplate.opsForValue().get(key);
            log.info("Got the generated code from redis");
            return code.equals(sentCode);
        }catch (Exception e){
            log.warn("Failed to get the generated code from redis");
            throw new FailedToConnectWithRedisException(e.getMessage());
        }
    }

    public void resetPassword(String param, String code, String newPassword){
        if (!validateCode(param, code)){
            log.warn("Invalid the reset code. Searched: '{}'", param);
            throw new BadCredentialsException("Invalid the reset code");
        }
        log.info("Trying to find user to reset password. Searched: '{}'", param);
        Optional<AuthUser> optionalAuthUser = authUserService.findByParam(param);
        if (optionalAuthUser.isEmpty()){
            log.warn("The user not found: '{}'", param);
            throw new UsersNotFoundException("The user not found");
        }

        AuthUser authUser = optionalAuthUser.get();
        authUser.setPassword(passwordEncoder.encode(newPassword));
        authUserService.saveAfterResetPassword(authUser);
        log.info("The password is reset. Searched: '{}'", param);
        String key = RESET_CODE_PREFIX + authUser.getId();
        try {
            log.info("Trying to delete the generated code from redis");
            redisTemplate.delete(key);
            log.info("Deleted the generated code from redis");
        }catch (Exception e){
            log.warn("Failed to delete the generated code from redis");
            throw new FailedToConnectWithRedisException(e.getMessage());
        }
    }
    private String generateCode(){
        Random random = new Random();
        log.info("Generating code to reset password");
        return String.format("%06d", random.nextInt(999999));
    }
}
