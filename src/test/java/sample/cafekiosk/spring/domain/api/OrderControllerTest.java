package sample.cafekiosk.spring.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import sample.cafekiosk.spring.api.controller.order.OrderController;
import sample.cafekiosk.spring.api.controller.order.dto.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @DisplayName("신규 주문을 등록한다")
    @Test
    void createOrder() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002", "003"));
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isOk());
    }

    @DisplayName("신규 주문을 등록할 때 상품번호는 1개 이상이어야한다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(List.of());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 번호 리스트는 필수입니다."));
    }
}