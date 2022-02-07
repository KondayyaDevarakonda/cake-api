package com.cake.api.cakeapi.service.Impl;

import com.cake.api.cakeapi.entity.CakeEntity;
import com.cake.api.cakeapi.gen.springbootserver.model.Cake;
import com.cake.api.cakeapi.repository.CakeRepository;
import com.cake.api.cakeapi.service.ICakeService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CakeServiceImpl implements ICakeService {
    @Autowired
    CakeRepository cakeRepository;

    @Override
    public void loadEmployeeData() {
        populateCakeData();
    }

    @Override
    public List<Cake> getAll() {
        List<CakeEntity> cakeEntList = cakeRepository.findAll();

        if (cakeEntList == null || cakeEntList.isEmpty()) {
            throw new RuntimeException("No Data found for Cakes");
        }

        List<Cake> cakeList = new ArrayList<>();

        if (cakeEntList != null && !cakeEntList.isEmpty()) {
            cakeEntList.forEach(cakeItem -> {
                Cake cake = new Cake();
                cake.setTitle(cakeItem.getTitle());
                cake.setDescription(cakeItem.getDescription());
                cake.setImage(cakeItem.getImage());
                cakeList.add(cake);
            });
        }
        return cakeList;
    }

    @Override
    public Cake findByCakeName(String cakeName) {
        CakeEntity cakeItem = cakeRepository.findAll()
                .stream()
                .filter(cake -> cake.getTitle().equalsIgnoreCase(cakeName))
                .findFirst()
                .get();
        if (cakeItem == null) {
            throw new RuntimeException("No Data found for Cakes");
        }
        Cake cake = new Cake();
        cake.setTitle(cakeItem.getTitle());
        cake.setDescription(cakeItem.getDescription());
        cake.setImage(cakeItem.getImage());

        return cake;
    }

    @Override
    public void addCake(Cake cake) {
        CakeEntity cakeItem = new CakeEntity();
        cakeItem.setTitle(cake.getTitle());
        cakeItem.setDescription(cake.getDescription());
        cakeItem.setImage(cake.getImage());
        try {
            cakeRepository.save(cakeItem);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save data");
        }

    }

    @Override
    public void updateCake(Cake body) {
        CakeEntity cakeItem = new CakeEntity();
        cakeItem.setTitle(body.getTitle());
        cakeItem.setDescription(body.getDescription());
        cakeItem.setImage(body.getImage());
        try {
            cakeRepository.save(cakeItem);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update data");
        }
    }

    @Override
    public void deleteCake(String cakeName) {
        CakeEntity cakeItem = cakeRepository.findAll()
                .stream()
                .filter(cake -> cake.getTitle().equalsIgnoreCase(cakeName))
                .findFirst()
                .get();
        try {
            cakeRepository.delete(cakeItem);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete data");
        }
    }

    private void populateCakeData() {
        log.info("downloading cake json");
        try (InputStream inputStream = new URL("https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json").openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }

            log.info("parsing cake json");
            JsonParser parser = new JsonFactory().createParser(buffer.toString());
            if (JsonToken.START_ARRAY != parser.nextToken()) {
                throw new Exception("bad token");
            }

            List<CakeEntity> cakelistval = new ArrayList<>();

            JsonToken nextToken = parser.nextToken();
            while(nextToken == JsonToken.START_OBJECT) {
                log.info("creating cake entity");

                CakeEntity cakeEntity = new CakeEntity();
                log.info(parser.nextFieldName());
                cakeEntity.setTitle(parser.nextTextValue());

                log.info(parser.nextFieldName());
                cakeEntity.setDescription(parser.nextTextValue());

                log.info(parser.nextFieldName());
                cakeEntity.setImage(parser.nextTextValue());

                cakelistval.add(cakeEntity);

                nextToken = parser.nextToken();

                nextToken = parser.nextToken();
            }

            log.info("----cakelistval----" + cakelistval.size());
            List<CakeEntity> cakelistvalFilter = cakelistval.stream().distinct().collect(Collectors.toList());
            log.info("----cakelistval----" + cakelistvalFilter.size());
            cakeRepository.saveAll(cakelistvalFilter);

        } catch (Exception ex) {
            log.info("Build {} ", ex.toString());
            throw new RuntimeException("Unable to load data");
        }
    }
}
