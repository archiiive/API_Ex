package com.example.api01.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
//p.785
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }

    //request로 값을 받아 response로 넘겨줘야한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("-------------------------APILoginFilter S T A R T -------------------------------\n");

        if(request.getMethod().equals("GET")){
            //get방식은 처리하지 않는다.
            log.info("-------GET METHOD NOT SUPPORT");
            return null;
        }
        Map<String, String> jsonData = parseRequestJSON(request);
        log.info("JSON DATA"+jsonData);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                        jsonData.get("mid"),
                        jsonData.get("mpw"));
        log.info("-------------------------APILoginFilter E N D -------------------------------\n");

        return getAuthenticationManager().authenticate(authenticationToken);

    }
    private Map<String, String> parseRequestJSON(HttpServletRequest request){
       //JSON 데이터를 분석해서 mid,mpw전달 값을 MAP으로 처리
        try (Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader,Map.class);

        }catch (Exception  e){
            log.error(e.getMessage());
        }
        return null;
    }

}
