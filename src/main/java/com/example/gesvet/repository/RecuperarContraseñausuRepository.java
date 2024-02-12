package com.example.gesvet.repository;

import com.example.gesvet.models.RecuperarContraseñaTokenusu;
import com.example.gesvet.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecuperarContraseñausuRepository extends JpaRepository<RecuperarContraseñaTokenusu, Long> {

    RecuperarContraseñaTokenusu findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM RecuperarContraseñaTokenusu t WHERE t.user = :user")
    void eliminarTokensPorUsuario(User user);

}
