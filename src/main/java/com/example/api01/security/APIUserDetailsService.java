package com.example.api01.security;


import com.example.api01.domain.APIUser;
import com.example.api01.dto.APIUserDTO;
import com.example.api01.repository.APIUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//P.781 Interface가 아니라 구현체로 만들어야함.
@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    //주입
    private final APIUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("----------------------------load User By Name----------------------------------------");
        //p.783
        Optional<APIUser> result= apiUserRepository.findById(username);

        //없으면 유저네임낫파운드 익셉션, 아니면 노서치 엘레먼트 익셉션이 터진다.
        APIUser apiUser = result.orElseThrow(()-> new UsernameNotFoundException("Cannnot find id"));

        APIUserDTO dto = new APIUserDTO(
                apiUser.getMid(),
                apiUser.getMpw(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        log.info(dto);
        log.info("----------------------------load User By Name end ----------------------------------------");

        return dto;

    }

}
