package com.example.api01.security.filter;

import com.example.api01.security.exception.RefreshTokenException;
import com.example.api01.utill.JWTUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(!path.equals(refreshPath)){ //refreshToken인 경우에만 접근 처리
            log.info("skip refresh token filter");
            filterChain.doFilter(request,response);
            return;
        }
        log.info("Refresth Token Filter...run........1");

        //검증
        //JSON 형식으로 전송된 accessToken과 refreshToken을 받기
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken : "+accessToken);
        log.info("refreshToken : "+refreshToken);

        try { //acceessToken 만료시에는 RefreshTokenExceprion이 전달되지 않아요
            checkAccessToken(accessToken);
        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }
        Map<String, Object>refreshClaims = null;

        try {
            refreshClaims = checkRefreshToken(refreshToken);
            log.info(refreshClaims);

            //새로운 Access Token 발행 ...
            //1.여기까지 진행되면 무조건AccessToken 발행...
            //2.refreshToken 만료시간 일이 얼마 남지 않은 경우 새로 발행...

            //Refresh Token의 유효시간이 얼마 남지 않은경우 ......
            Long exp = (Long) refreshClaims.get("exp");

            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
            Date current = new Date(System.currentTimeMillis());

            //만료 시간과 현재시간의 간격을 계산한다. 3일정도 남았을때 refresh와 AccessToken 둘다 발행하겠다는
            //정책을 세운다 . 만일 3일 이내라면 refresh토큰을 다시 발행한다.

            long gapTime = (expTime.getTime() - current.getTime());
            log.info("----------------------------------");
            log.info("current :  "+current);
            log.info("expTime : "+expTime);
            log.info("gap : "+ gapTime);

            String mid = (String) refreshClaims.get("mid");


            //여기까지 도착하면 무조건 Token 생성...
            String accessTokenValue = jwtUtil.generateToken(Map.of("mid",mid),1);
            String refreshTokenValue = tokens.get("refreshToken"); // 기존에 있던 refreshToken 값

            //기존에 있던 토큰값의 만료 시간이 3일 이내라면 다시 만들어서 집어 넣겠다는 의미다
            if(gapTime < (1000 * 60 * 60 * 24 * 3)){
                log.info("new Refresh Token required...");
                refreshTokenValue = jwtUtil.generateToken(Map.of("mid",mid),1);
            }
            log.info("Refresh Token result -----------");
            log.info("accessToken : "+accessToken);
            log.info("refreshToken  L" + refreshToken);


        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;//더이상 실행 x
        }
    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request){
        //JSON데이터를 분석해서 mid mpw 전달 값을 Map으로 처리
        try (Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();
            return  gson.fromJson(reader, Map.class);
        }catch (Exception e){
            log.info(e.getMessage());
        }
            return null;
    }

    //AccessToken 검증처리...
    private void checkAccessToken(String accessToken) throws RefreshTokenException{
        try {
            jwtUtil.validateToken(accessToken);//토큰 검증 /리프레쉬도 얘로 검증한다.
        }catch (ExpiredJwtException expiredJwtException){
            log.info("Access Token has expired");
        }catch (Exception e){
            //Access토큰 만료이외의 예외에 대한 RefreshToken예외 처리
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }

    }

    //AccessToken 검증처리...
    private Map<String,Object> checkRefreshToken(String refreshToken) throws RefreshTokenException{
        try {
            Map<String,Object> values = jwtUtil.validateToken(refreshToken); //토큰 검증
            return values;
        }catch (ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch (MalformedJwtException malformedJwtException){
            log.error("MalformedJwtException..................................");
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch (Exception exception){
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return  null;
    }


}
