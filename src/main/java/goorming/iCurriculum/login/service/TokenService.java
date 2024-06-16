package goorming.iCurriculum.login.service;

import goorming.iCurriculum.login.entity.Token;
import goorming.iCurriculum.login.jwt.JWTUtil;
import goorming.iCurriculum.login.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public String extractResfreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public Boolean isValidRefreshToken(String refreshToken) {
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            log.error("refresh token expired");
            return false;
        }

        String category = jwtUtil.getCategory(refreshToken);
        return category.equals("refresh_token");
    }

    public Boolean isExist (String refreshToken) {
        Boolean isExist = tokenRepository.existsByRefreshToken(refreshToken);
        if(!isExist) {
            log.error("invalid refresh token");
            return false;
        }
        return true;
    }

    public String createToken(String refreshToken,String category) {
        String clientId = jwtUtil.getUsername(refreshToken);
        String role = String.valueOf(jwtUtil.getRole(refreshToken));
        return jwtUtil.createJwt(category, clientId, role, 60 * 10000L);
    }

    public void deleteToken(String refreshToken) {
        tokenRepository.deleteByRefreshToken(refreshToken);
    }

    public void addRefreshToken(String clientId, String refreshToken, Long expireTime) {

        Date date = new Date(System.currentTimeMillis() + expireTime);
        Token token = Token.builder()
                .clientId(clientId)
                .refreshToken(refreshToken)
                .expiredAt(date)
                .build();
        tokenRepository.save(token);
    }

    public Cookie createCookie(String key, String value) { //(key값, JWT)
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true); // javascript에서 접근 불가
//        cookie.setSecure(true); //https 사용시 추가
        cookie.setMaxAge(24*60*60); //쿠키 생명 주기
        cookie.setPath("/");
        return cookie;
    }
}
