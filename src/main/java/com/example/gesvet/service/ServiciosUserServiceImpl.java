/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.ServiciosUser;
import com.example.gesvet.repository.IServiceUserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiciosUserServiceImpl implements IServiciosUserService {
    
    @Autowired
    IServiceUserRepository serviceuserepository;

    @Override
    public ServiciosUser save(ServiciosUser serviciosuser) {
     return serviceuserepository.save(serviciosuser);
    }

    @Override
    public Optional<ServiciosUser> get(Integer id) {
     return serviceuserepository.findById(id);
             }

    @Override
    public void update(ServiciosUser serviciosuser) {
      serviceuserepository.save(serviciosuser);
    }

    @Override
    public void delete(Integer id) {
    serviceuserepository.deleteById(id);
    }

    @Override
    public List<ServiciosUser> findAll() {
    return serviceuserepository.findAll();
            }
    
}
