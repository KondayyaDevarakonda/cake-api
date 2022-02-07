package com.cake.api.cakeapi.controller;

import com.cake.api.cakeapi.gen.springbootserver.api.CakesApi;
import com.cake.api.cakeapi.gen.springbootserver.model.Cake;
import com.cake.api.cakeapi.service.ICakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CakeApiController implements CakesApi {

    @Autowired
    ICakeService cakeService;

    @Override
    public ResponseEntity<Void> addCake(Cake body) {
        cakeService.addCake(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteCake(String cakeTitle) {
        cakeService.deleteCake(cakeTitle);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Cake>> getAll() {
        List<Cake> all = cakeService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Cake> getCakeByTitle(String cakeTitle) {
        Cake byCakeName = cakeService.findByCakeName(cakeTitle);
        return new ResponseEntity<>(byCakeName, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateCake(Cake body) {
        cakeService.updateCake(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
