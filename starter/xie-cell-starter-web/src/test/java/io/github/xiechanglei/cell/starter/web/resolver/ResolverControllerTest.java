package io.github.xiechanglei.cell.starter.web.resolver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResolverControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testDateResolver() throws Exception {
        mockMvc.perform(get("/api/resolver/date")
                        .param("date", "2024-06-01") // 发送参数
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)) // 设置请求内容类型
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("2024-06-01 00:00:00")); // 验证返回的 JSON 中 name 字段
    }

}
