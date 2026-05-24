package com.scms.learning.controller;

import com.scms.learning.common.Result;
import com.scms.learning.dto.LoginRequest;
import com.scms.learning.dto.RegisterRequest;
import com.scms.learning.service.AuthService;
import com.scms.learning.vo.LoginUserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<LoginUserVO> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @PostMapping("/login")
    public Result<LoginUserVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }
}