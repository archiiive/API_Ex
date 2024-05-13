package com.example.api01.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;


/*
토큰 만료/없을시 JWT에서 익셉션 발생을 시켰다
그건 프레임워크에서 있던것이고 토큰값은 사용자 서버가 인증하는방법이 여럭가지가 있다
/Tearer Token을 사용한다

runtime 익셉션은 구동중 예외발생이다. 정해진 내용이 아니라 정상동작 하다가 뭔가 문제가 발생하면 터지는 예외이다.

 */
public class AccessTokenException extends RuntimeException{

    TOKEN_ERROR token_error;

    //열거형 만들기
    public enum TOKEN_ERROR {
        UNACCEPT(401,"Token is null or too short"),
        BADTYPE(401,"Token type Bearer"),
        MALFORM(403,"malformed Token"),
        BADSIGN(403,"Badsignatured TOKEN"),
        EXPIRED(403,"EXpired TOKEN");

        private int status;
        private String msg;

        TOKEN_ERROR(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }


        public String getMsg() {
            return msg;
        }
    }
    public AccessTokenException(TOKEN_ERROR error){
        super(error.name());
        this.token_error = error;
    }

    public void  sendResponseError(HttpServletResponse response){
        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg",token_error.getMsg(),"time",new Date()));

        try {
            response.getWriter().println(responseStr);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
