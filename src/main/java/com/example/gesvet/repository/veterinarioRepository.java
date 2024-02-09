package com.example.gesvet.repository;

import com.example.gesvet.models.Veterinario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface veterinarioRepository extends CrudRepository<Veterinario, Long> {

}
