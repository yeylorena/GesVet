package com.example.gesvet.service;

import com.example.gesvet.models.ServiciosUser;
import java.util.List;
import java.util.Optional;

public interface IServiciosUserService {

    public ServiciosUser save(ServiciosUser serviciosuser);

    public Optional<ServiciosUser> get(Integer id);

    public void update(ServiciosUser serviciosuser);

    public void delete(Integer id);

    public List<ServiciosUser> findAll();

    public ServiciosUser findById(Integer id);

}
