package com.lixin.vehical_management.config.security.user;

import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.repositories.EmployeeRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    EmployeeRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        Employee user = userRepository.findEmployeeByUserName(userName);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("User not found : " + userName);

        }
        return UserPrincipal.create(user);
    }
}