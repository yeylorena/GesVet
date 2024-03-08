package com.example.gesvet.repository;

import com.example.gesvet.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    public List<User> findByRole(String role);

    List<User> findByRoleAndActivo(String role, boolean activo);

    public int countByRole(String role);

}
