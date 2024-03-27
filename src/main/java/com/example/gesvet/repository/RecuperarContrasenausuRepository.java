package com.example.gesvet.repository;

import com.example.gesvet.models.RecuperarContrasenaTokenusu;
import com.example.gesvet.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecuperarContrasenausuRepository extends JpaRepository<RecuperarContrasenaTokenusu, Long> {

    RecuperarContrasenaTokenusu findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM RecuperarContrasenaTokenusu t WHERE t.user = :user")
    void eliminarTokensPorUsuario(User user);

}
