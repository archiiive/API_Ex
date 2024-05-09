package com.example.api01.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIUser {

    //DB에 연관되서 작업
    //id와 pw를 가지고있는 테이블이 생성된다.
    @Id//P.779
    private String mid;
    private String mpw;

    public void changePassword(String mpw){
        this.mpw = mpw;

    }


}
