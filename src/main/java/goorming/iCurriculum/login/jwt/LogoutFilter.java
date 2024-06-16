package goorming.iCurriculum.login.jwt;

import goorming.iCurriculum.login.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        doLogoutFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doLogoutFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {


        String requestUri = request.getRequestURI();
        if(!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //refresh token 발급
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if(cookie.getName().equals("refresh_token")) {
                refresh = cookie.getValue();
            }
        }

        // refresh token null check
        if(refresh == null) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE); // 406
            response.getWriter().println("refresh token not found");
            return;
        }

        // refresh token 만료 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);// 406
            response.getWriter().println("refresh token expired");
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        if( !category.equals("refresh_token")) {

            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE); // 406
            response.getWriter().println("invalid refresh token");
            return;
        }

        //DB에 있는지 확인
        Boolean isExist = tokenService.isExist(refresh);
        if(!isExist) {

            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE); // 406
            response.getWriter().println("refresh token is not exist");
            return;
        }

        //로그아웃 진행
        //Refresh Token DB에서 삭제
        tokenService.deleteToken(refresh);

        //Refresh token 쿠키 삭제
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK); // 200
    }
}
