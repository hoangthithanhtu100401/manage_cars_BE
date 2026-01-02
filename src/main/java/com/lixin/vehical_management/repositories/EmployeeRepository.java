package com.lixin.vehical_management.repositories;

import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.entities.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByRole(Role role);
    Employee findEmployeeByUserName(String userName);
}