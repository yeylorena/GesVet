package com.example.gesvet.repository;

import com.example.gesvet.models.ServiciosUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServiceUserRepository extends JpaRepository<ServiciosUser, Integer> {

}
