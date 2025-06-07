package com.example.demo.service;

import com.example.demo.dto.UserSignUpRequestDto;
import com.example.demo.dto.UserSignUpResponseDto;
import com.example.demo.entity.User;
import com.example.demo.global.PasswordUtil;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    public UserSignUpResponseDto signUp(UserSignUpRequestDto dto) {
        //이미 가입된 이메일이 있다면
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 가입된 이메일입니다!");
        }

        if(userRepository.findBynickName(dto.getNickName()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다!");
        }

        String encodedPassword = passwordUtil.encryptPassword(dto.getPassword());
        User user = new User(dto.getUserName(), dto.getNickName(), dto.getEmail(), encodedPassword);

        User savedUser = userRepository.save(user);


        return new UserSignUpResponseDto(savedUser.getId());
    }
}
