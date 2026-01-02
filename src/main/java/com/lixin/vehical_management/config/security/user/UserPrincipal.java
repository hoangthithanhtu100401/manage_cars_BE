package com.lixin.vehical_management.config.security.user;

import com.lixin.vehical_management.entities.Employee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class UserPrincipal implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String userName;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(){
    }
    public UserPrincipal(Long id, String password,String userName, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.userName =userName;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Employee user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserPrincipal(
                user.getId(),
                user.getPassword(),
                user.getUserName(),
                authorities
        );
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
