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
import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;
import sample.cafekiosk.spring.api.controller.product.dto.response.ProductResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    //objectMapper -> Json과 Object간의 직렬화와 역직렬화를 도와줌

    @MockitoBean
    private ProductService productService;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isOk());

    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();

        // when & then
        when(productService.getSellingProducts()).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling"))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("성공적으로 조회하였습니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수 값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                //.productType(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."));
    }

    @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수 값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                //.sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."));
    }

    @DisplayName("신규 상품을 등록할 때 상품 판매이름은 필수 값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                //.name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."));
    }

    @DisplayName("신규 상품을 등록할 때 상품 판매이름은 필수 값이다.")
    @Test
    void createProductWithZeroPrice() throws Exception {
        // givenㅋ
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) //요청이 어떻게 날라갔는지 상세하게 로그를 띄울 수 있음
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."));
    }

}