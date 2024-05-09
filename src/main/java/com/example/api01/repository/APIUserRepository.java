package com.example.api01.repository;

import com.example.api01.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;


//String인 이유는 jpa user type이 스트링이다. p.780
public interface APIUserRepository extends JpaRepository<APIUser, String> {



}
