package com.cake.api.cakeapi.repository;

import com.cake.api.cakeapi.entity.CakeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CakeRepository extends CrudRepository<CakeEntity, Long> {
    List<CakeEntity> findAll();
    CakeEntity findByTitle(String cakeTitle);
}
