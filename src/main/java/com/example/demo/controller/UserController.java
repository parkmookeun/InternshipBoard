package com.example.demo.controller;

import com.example.demo.dto.UserSignUpRequestDto;
import com.example.demo.dto.UserSignUpResponseDto;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponseDto> signUp(
            @RequestBody UserSignUpRequestDto dto
    ){
        UserSignUpResponseDto responseDto = userService.signUp(dto);

        return new ResponseEntity<UserSignUpResponseDto>(responseDto, HttpStatus.CREATED);
    }
}
