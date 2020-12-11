package com.byakuya.boot.factory.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


/**
 * Created by ganzl on 2020/12/9.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationPageControllerTest {

    @Test
    void changePassword(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/changePassword")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "xxxx")
                .param("oldPassword", "xxxx")
                .param("newPassword", "xxxx"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void register(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}