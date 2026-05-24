package com.scms.learning.service;

import com.scms.learning.dto.LoginRequest;
import com.scms.learning.dto.RegisterRequest;
import com.scms.learning.vo.LoginUserVO;

public interface AuthService {

    LoginUserVO register(RegisterRequest request);

    LoginUserVO login(LoginRequest request);
}