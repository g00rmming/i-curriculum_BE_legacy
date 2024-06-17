package goorming.iCurriculum.login.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorming.iCurriculum.login.dto.LoginDTO;
import goorming.iCurriculum.login.entity.Token;
import goorming.iCurriculum.login.repository.TokenRepository;
import goorming.iCurriculum.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String clientId = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(clientId, password, null);

        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 jwt 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        String clientId = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String access_token = jwtUtil.createJwt("access_token", clientId, role, 60 * 10000L);
        String refresh_token = jwtUtil.createJwt("refresh_token", clientId, role, 360 * 240000L);

        addToken(clientId, refresh_token, 360 * 240000L);

        Long memberId = memberRepository.findByClientId(clientId).getId();

        //로그인 성공 시 memberId와 clientId를 httpResponse body에 담아서 전달
        LoginDTO loginDTO = new LoginDTO(clientId,memberId);
        String result = objectMapper.writeValueAsString(loginDTO);

        //응답 설정
        response.setHeader("Authorization", access_token);
        response.getWriter().write(result);
        response.addCookie(createCookie("refresh_token", refresh_token));
        response.setStatus(HttpStatus.OK.value()); //성공하면 200 상태코드
    }

    //로그인 실패시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
        response.getWriter().write("login fail");
    }

    private Cookie createCookie(String key, String value) { //(key값, JWT)
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true); // javascript에서 접근 불가
//        cookie.setSecure(true); //https 사용시 추가
        cookie.setMaxAge(24*60*60); //쿠키 생명 주기
        cookie.setPath("/");
        return cookie;
    }

    private void addToken(String clientId, String refreshToken, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Token token = Token.builder()
                .clientId(clientId)
                .refreshToken(refreshToken)
                .expiredAt(date)
                .build();
        tokenRepository.save(token);
    }
}
