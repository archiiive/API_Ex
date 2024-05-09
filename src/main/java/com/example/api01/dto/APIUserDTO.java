package com.example.api01.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//
@Setter
@Getter
@ToString

public class APIUserDTO extends User {

    private String mid;
    private String mpw;

    //P.782~783
    //기본생성자는 만들지 않았다.
    public APIUserDTO(String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid=username;
        this.mpw=password;

    }
}
