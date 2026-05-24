package com.scms.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.dto.LoginRequest;
import com.scms.learning.dto.RegisterRequest;
import com.scms.learning.entity.User;
import com.scms.learning.mapper.UserMapper;
import com.scms.learning.service.AuthService;
import com.scms.learning.vo.LoginUserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginUserVO register(RegisterRequest request) {
        String username = request.getUsername().trim();

        User existUser = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (existUser != null) {
            throw new RuntimeException("该用户名已存在");
        }

        if (!"teacher".equals(request.getRole()) && !"student".equals(request.getRole())) {
            throw new RuntimeException("角色只能是 teacher 或 student");
        }

        if (request.getPassword().length() < 6) {
            throw new RuntimeException("密码长度不能少于6位");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setRole(request.getRole());
        user.setClassName(request.getClassName());

        userMapper.insert(user);

        return buildLoginUserVO(user);
    }

    @Override
    public LoginUserVO login(LoginRequest request) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, request.getUsername().trim())
        );

        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }

        boolean passwordCorrect = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordCorrect) {
            throw new RuntimeException("密码错误");
        }

        return buildLoginUserVO(user);
    }

    private LoginUserVO buildLoginUserVO(User user) {
        String token = UUID.randomUUID().toString();

        return new LoginUserVO(
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getRole(),
                user.getClassName(),
                token
        );
    }
}