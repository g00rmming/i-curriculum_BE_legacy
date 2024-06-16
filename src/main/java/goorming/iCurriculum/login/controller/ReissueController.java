package goorming.iCurriculum.login.controller;

import goorming.iCurriculum.login.jwt.JWTUtil;
import goorming.iCurriculum.login.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = tokenService.extractResfreshToken(request);
        if (refreshToken == null){
            return new ResponseEntity<>("refresh token not found", HttpStatus.NOT_ACCEPTABLE); // 406
        }
        if (!tokenService.isValidRefreshToken(refreshToken)){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!tokenService.isExist(refreshToken)){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.NOT_ACCEPTABLE);
        }

        //새로운 토큰 생성
        String newAccessToken = tokenService.createToken(refreshToken,"access_token");
        // refresh token rotate
        String newRefreshToken = tokenService.createToken(refreshToken,"refresh_token");

        String clientId = jwtUtil.getUsername(refreshToken);

        tokenService.deleteToken(refreshToken);
        tokenService.addRefreshToken(clientId, newRefreshToken, 360 * 240000L);

        response.setHeader("Authorization", newAccessToken);
        response.addCookie(tokenService.createCookie("refresh_token", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
