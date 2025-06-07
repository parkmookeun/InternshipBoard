package com.example.demo.service;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.UserSignUpRequestDto;
import com.example.demo.dto.UserSignUpResponseDto;
import com.example.demo.entity.User;
import com.example.demo.global.exception.NotFoundException;
import com.example.demo.global.util.JwtUtil;
import com.example.demo.global.util.PasswordUtil;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JwtUtil jwtUtil;

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

    public LoginResponseDto login(LoginRequestDto dto) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword())
            );

        UserDetails userDetails = userDetailService.loadUserByUsername(dto.getEmail());

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new NotFoundException("해당 유저가 존재하지 않습니다!")
        );

        String jwt = jwtUtil.generateToken(userDetails);

        return new LoginResponseDto(jwt, user.getNickName());
    }
}
