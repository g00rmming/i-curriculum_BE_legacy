package goorming.iCurriculum.login.jwt;

import goorming.iCurriculum.login.dto.UserDetailsDTO;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.RoleType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String access_token = request.getHeader("Authorization");
        // 토큰 있는지 확인
        if (access_token == null) {
            filterChain.doFilter(request, response);
            return; // 토큰 없으면 다음 필터로 넘어감
        }

        //토큰 유효성 검사
        try {
            jwtUtil.isExpired(access_token);
        } catch (ExpiredJwtException e) {

            PrintWriter writer = response.getWriter();
            writer.println("access token expired");

            //response status
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);;// 400
            return;
        }

        String category = jwtUtil.getCategory(access_token);
        if(!category.equals("access_token")) {
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            return;
        }

        String clientId = jwtUtil.getUsername(access_token);
        String role = String.valueOf(jwtUtil.getRole(access_token));

        //일시적인 세션 생성
        Member member = new Member();
        member.setClientId(clientId);
        member.setRoleType(RoleType.valueOf(role));

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsDTO, null, userDetailsDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response); // 다음 필터로 넘어감
    }
}

