package goorming.iCurriculum.login.config;

import goorming.iCurriculum.login.jwt.JWTFilter;
import goorming.iCurriculum.login.jwt.JWTUtil;
import goorming.iCurriculum.login.jwt.LoginFilter;
import goorming.iCurriculum.login.jwt.LogoutFilter;
import goorming.iCurriculum.login.repository.TokenRepository;
import goorming.iCurriculum.login.service.TokenService;
import goorming.iCurriculum.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8081"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));

        //csrf disable
        httpSecurity
                .csrf((auth) -> auth.disable());

        //Form login 방식 disable
        httpSecurity
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        httpSecurity
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers( "/","/swagger-ui/**", "/v3/api-docs/**","/v2/swagger-config").permitAll()
                        .requestMatchers("/login", "/api/v1/members/join", "/members/**").permitAll()
                        .requestMatchers("/login", "/", "/api/v1/members/isExistId", "/members/**").permitAll()
                        .requestMatchers("/reissue").permitAll() //access token 만료 되어도 접근 해야함
                        .anyRequest()
                        .authenticated());

        //JWT 필터 추가
        httpSecurity
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class); // loginFilter 이전에 jwtFilter 추가

        //로그인 필터 추가
        httpSecurity
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil,tokenRepository,memberRepository),
                        UsernamePasswordAuthenticationFilter.class);


        //session 설정 (jwt 사용 -> stateless)
        httpSecurity
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //로그아웃 필터 추가
        httpSecurity
                .addFilterBefore(new LogoutFilter(jwtUtil,tokenService),org.springframework.security.web.authentication.logout.LogoutFilter.class);

        return httpSecurity.build();
    }
}
