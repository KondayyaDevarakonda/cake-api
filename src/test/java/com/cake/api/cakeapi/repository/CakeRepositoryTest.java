package com.cake.api.cakeapi.repository;

import com.cake.api.cakeapi.entity.CakeEntity;
import com.cake.api.cakeapi.repository.CakeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class CakeRepositoryTest {

    @Autowired
    private CakeRepository cakeRepository;

    @BeforeEach
    void initUseCase() {
        List<CakeEntity> cakeEntityList = new ArrayList<>();
        CakeEntity cakeEntity = new CakeEntity();
        cakeEntity.setTitle("Sweet Cake");
        cakeEntity.setDescription("Sweet Cake Description");
        cakeEntity.setImage("Sweet Cake Image");
        CakeEntity cakeEntity1 = new CakeEntity();
        cakeEntity1.setTitle("Sweet Cake 3");
        cakeEntity1.setDescription("Sweet Cake Description 3");
        cakeEntity1.setImage("Sweet Cake Image 3");
        cakeEntityList.add(cakeEntity1);

        cakeEntityList.add(cakeEntity);
        cakeRepository.saveAll(cakeEntityList);
    }

    @AfterEach
    public void destroyAll(){
        cakeRepository.deleteAll();
    }

    @Test
    void saveAll_Cake_Success() {
        List<CakeEntity> cakeEntityList = new ArrayList<>();
        CakeEntity cakeEntity = new CakeEntity();
        cakeEntity.setTitle("Sweet Cake 1");
        cakeEntity.setDescription("Sweet Cake Description 1");
        cakeEntity.setImage("Sweet Cake Image 1");
        cakeEntityList.add(cakeEntity);
        CakeEntity cakeEntity1 = new CakeEntity();
        cakeEntity1.setTitle("Sweet Cake 2");
        cakeEntity1.setDescription("Sweet Cake Description 2");
        cakeEntity1.setImage("Sweet Cake Image 2");
        cakeEntityList.add(cakeEntity1);

        cakeRepository.saveAll(cakeEntityList);

        Iterable<CakeEntity> allCakes = cakeRepository.findAll();

        AtomicInteger validIdFound = new AtomicInteger();
        allCakes.forEach(cake -> {
            if(cake.getTitle().equalsIgnoreCase("Sweet Cake")){
                validIdFound.getAndIncrement();
            }
        });

        assertThat(validIdFound.intValue()).isEqualTo(1);
    }

    @Test
    void getAll_Cake_Success() {
        List<CakeEntity> cakeEntList = cakeRepository.findAll();
        assertThat(cakeEntList.size()).isEqualTo(2);
    }

    @Test
    void delete_Cake_Success() {
        CakeEntity cakeItem = cakeRepository.findAll()
                .stream()
                .filter(cake -> cake.getTitle().equalsIgnoreCase("Sweet Cake"))
                .findFirst()
                .get();
        cakeRepository.delete(cakeItem);

        Iterable<CakeEntity> allCakes = cakeRepository.findAll();

        AtomicInteger validIdFound = new AtomicInteger();
        allCakes.forEach(cake -> {
            if(cake.getTitle().equalsIgnoreCase("Sweet Cake 3")){
                validIdFound.getAndIncrement();
            }
        });
        assertThat(validIdFound.intValue()).isEqualTo(1);
    }
}
