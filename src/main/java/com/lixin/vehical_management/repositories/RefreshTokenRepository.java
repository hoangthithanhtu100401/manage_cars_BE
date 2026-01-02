package com.lixin.vehical_management.repositories;

import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByRefreshTokenCode(String refreshToken);

    List<RefreshToken> findAllByEmployee(Employee employee);
}
