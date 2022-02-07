package com.cake.api.cakeapi.controller;

import com.cake.api.cakeapi.entity.CakeEntity;
import com.cake.api.cakeapi.gen.springbootserver.model.Cake;
import com.cake.api.cakeapi.repository.CakeRepository;
import com.cake.api.cakeapi.service.Impl.CakeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CakeApiController.class)
@WithMockUser
public class CakeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CakeServiceImpl cakeService;
    @MockBean
    private CakeRepository cakeRepository;

    List<CakeEntity> cakeEntityList = new ArrayList<>();


    @Test
    public void test_all_cake_details() throws Exception {
        getCakeList();

        Mockito.when(cakeRepository.findAll()).thenReturn(cakeEntityList);
        List<Cake> cakeList = new ArrayList<>();

        if (cakeEntityList != null && !cakeEntityList.isEmpty()) {
            cakeEntityList.forEach(cakeItem -> {
                Cake cake = new Cake();
                cake.setTitle(cakeItem.getTitle());
                cake.setDescription(cakeItem.getDescription());
                cake.setImage(cakeItem.getImage());
                cakeList.add(cake);
            });
        }

        Mockito.when(cakeService.getAll()).thenReturn(cakeList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/cakes").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"title\":\"Sweet Cake 3\",\"description\":\"Sweet Cake Description 3\",\"image\":\"Sweet Cake Image 3\"},{\"title\":\"Sweet Cake\",\"description\":\"Sweet Cake Description\",\"image\":\"Sweet Cake Image\"}]";

        System.out.println("response returned : " + result.getResponse()
                .getContentAsString());

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void test_add_cake() throws Exception {

        Cake cake = new Cake();
        cake.setTitle("Sweet Cake");
        cake.setDescription("Sweet Cake Description");
        cake.setImage("Sweet Cake Image");

        CakeEntity cakeItem = new CakeEntity();
        cakeItem.setTitle(cake.getTitle());
        cakeItem.setDescription(cake.getDescription());
        cakeItem.setImage(cake.getImage());
        Mockito.when(cakeRepository.save(cakeItem)).thenReturn(cakeItem);
        cakeService.addCake(cake);

        String exampleCourseJson = "{\"title\":\"Sweet Cake 3\",\"description\":\"Sweet Cake Description 3\",\"image\":\"Sweet Cake Image 3\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cakes")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }

    private void getCakeList() {
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
    }


}
