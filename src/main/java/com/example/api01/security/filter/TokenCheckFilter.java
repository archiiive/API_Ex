package com.example.api01.security.filter;


import com.example.api01.security.exception.AccessTokenException;
import com.example.api01.utill.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

//P.808
///상속을 받아서 사용 (OncePerRequestFilter)//필터 작업을 해준다.

@RequiredArgsConstructor
@Log4j2
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        String path = request.getRequestURI();

        //만약 /api가 아니라면 다음 필터로 넘어간다.
        //필터는 하나가아니라 그 다음 체인 그 다음 체인 이런식으로 다음으로 넘긴다.

        if (!path.startsWith("/api")){
            filterChain.doFilter(request,response);
            return;
        }

        log.info("Token Check Filter");
        log.info("JWTUtil : "+jwtUtil);
        try {
            validateAccessToken(request);
            filterChain.doFilter(request,response);
        }catch (AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }
 
    }


    private Map<String , Object>validateAccessToken(HttpServletRequest request)
                                                    throws AccessTokenException{
        //토큰이 저장될 헤더와 아래에는 if문으로 검증
        String headerStr = request.getHeader("Authorization");
        //8보다 작은걸 검증하는이유가 일단 0 보다 작을수가 없다. Bearer Token <- 이게 띄어쓰기까지 8칸이다
        if (headerStr ==null || headerStr.length() < 8){// 토큰이 없는경우
            //이렇게하면 토큰 에러에 있는 언셉션을 발생시킨다
            throw  new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);



        }
        //Bearer을 생략 이유는 이 값까지 7글자는 토큰값이 아니다
        //결국 토큰값을 추려서 봐야한다 (필터 검증에는 당연히 토큰값을 봐야한다)
        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);

        //innnorecase는 대소문자를 구별하지 않는다.
        //false면 타입이 잘못된것이다.
        if (tokenType.equalsIgnoreCase("Bearer") == false){
            ///BadType
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }
        try {
            Map<String,Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch (MalformedJwtException malformedJwtException){
            log.info("---------------MalformedJwtException---------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch (SignatureException signatureException){
            log.info("------------------------SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch (ExpiredJwtException expiredJwtException){
            log.info("---------------ExpiredJwtException---------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }

}
