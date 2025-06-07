package com.example.demo.global;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt를 사용한 비밀번호 암호화 및 검증 유틸리티 클래스
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordUtil() {
        // BCrypt 강도 설정 (기본값: 10, 범위: 4-31)
        // 높을수록 보안성은 좋지만 처리 시간이 오래 걸림
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    /**
     * 평문 비밀번호를 BCrypt로 암호화
     *
     * @param rawPassword 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encryptPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 평문 비밀번호와 암호화된 비밀번호를 비교하여 일치 여부 확인
     *
     * @param rawPassword 평문 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return 일치하면 true, 불일치하면 false
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 비밀번호 강도 검증
     *
     * @param password 검증할 비밀번호
     * @return 강도가 충족되면 true, 아니면 false
     */
    public boolean isValidPasswordStrength(String password) {
        if (password == null) {
            return false;
        }

        // 최소 8자 이상, 대소문자, 숫자, 특수문자 포함
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

    /**
     * 암호화된 비밀번호가 유효한 BCrypt 형식인지 확인
     *
     * @param encodedPassword 검증할 암호화된 비밀번호
     * @return 유효한 BCrypt 형식이면 true, 아니면 false
     */
    public boolean isValidBCryptFormat(String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        // BCrypt 해시는 $2a$, $2b$, $2x$, $2y$로 시작하고 총 60자
        return encodedPassword.matches("^\\$2[abxy]\\$\\d{2}\\$.{53}$");
    }
}
