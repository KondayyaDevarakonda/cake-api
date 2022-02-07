package com.cake.api.cakeapi.service;

import com.cake.api.cakeapi.gen.springbootserver.model.Cake;

import java.util.List;

public interface ICakeService {
    void loadEmployeeData();

    List<Cake> getAll();

    Cake findByCakeName(String cakeName);

    void addCake(Cake cake);

    void updateCake(Cake body);

    void deleteCake(String cakeName);
}
