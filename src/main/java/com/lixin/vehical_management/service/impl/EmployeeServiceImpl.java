package com.lixin.vehical_management.service.impl;

import com.lixin.vehical_management.dto.EmployeeRequest;
import com.lixin.vehical_management.dto.ResponseMessage;
import com.lixin.vehical_management.dto.UpdateEmployeeRequest;
import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.entities.Enum.Role;
import com.lixin.vehical_management.exception.ApplicationRuntimeException;
import com.lixin.vehical_management.repositories.EmployeeRepository;
import com.lixin.vehical_management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository userRepository;
    private final PasswordEncoder bCrypt;

    @Override
    public ResponseEntity<?> registerUser(EmployeeRequest request) {
//        if (ObjectUtils.isNotEmpty(userRepository.findById(request.getUserId()))) {
//            throw new ApplicationRuntimeException("User already exists");
//        }
        Role role;
        if (!StringUtils.isEmpty(request.getRole())) {
            role = Role.getByShortName(request.getRole());
            if (Objects.isNull(role)) {
                throw new ApplicationRuntimeException("Invalid role", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ApplicationRuntimeException("Invalid role", HttpStatus.BAD_REQUEST);
        }
        String hashPw = bCrypt.encode(request.getPassword());
        Employee user = userRepository.save(Employee.builder()
                .role(role)
                .password(hashPw)
                .userName(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .hiredAt(LocalDateTime.now())
                .build());
        userRepository.save(user);
        return ResponseEntity.ok()
                .body(new ResponseMessage<>("Registration successful!", HttpStatus.OK));
    }

    @Override
    public void updatePassWord(UpdateEmployeeRequest updateUserRequest, Employee user) {
        String passwordEncoder = bCrypt.encode(updateUserRequest.getNewPassword());
        if (BooleanUtils.isFalse(bCrypt.matches(updateUserRequest.getOldPassword(), user.getPassword()))) {
            throw new ApplicationRuntimeException("Old password is incorrect!", HttpStatus.BAD_REQUEST);
        }
        if (BooleanUtils.isTrue(bCrypt.matches(updateUserRequest.getNewPassword(), user.getPassword()))) {
            throw new ApplicationRuntimeException("New password and old password cannot be the same!", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder);
        userRepository.save(user);
    }

}
