package com.bulka.OnlineShop

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import com.jayway.jsonpath.JsonPath
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class OnlineShopApplicationTests {

	@Test
	fun contextLoads() {
	}

}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ProductControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("Проверка на пустоту БД при первом запуске")
    fun testGetProducts() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    @DisplayName("Проверка на несуществующий товар")
    fun testProductNotFound() {
        val mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        mockMvc.perform(get("/api/products/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    @DisplayName("Проверка создания товара")
    fun testCreateAndGetProduct() {
        val mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

        val productJson = """
        {
            "name": "Apples Global Village",
            "description": "Juicy, taste, red",
            "price": 170.0,
            "stock": 5000
        }
        """.trimIndent()

        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Apples Global Village"))

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Apples Global Village"))
    }

    @Test
    @DisplayName("Проверка обновления товара")
    fun testUpdateProduct() {
        val mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

        val productJson = """
        {
            "name": "Apples Global Village",
            "description": "Juicy, taste, red",
            "price": 170.0,
            "stock": 5000
        }
        """.trimIndent()

        val result = mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andReturn()

        val productId = JsonPath.parse(result.response.contentAsString)
            .read("$.id", Long::class.java)

        val productUpdJson = """
        {
            "name": "Apples Global Village",
            "description": "Juicy, taste, red",
            "price": 150.0,
            "stock": 3000
        }
        """.trimIndent()

        mockMvc.perform(
            put("/api/products/$productId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productUpdJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.price").value(150.0))
            .andExpect(jsonPath("$.stock").value(3000))

        mockMvc.perform(get("/api/products/$productId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.price").value(150.0))
            .andExpect(jsonPath("$.stock").value(3000))
    }

    @Test
    @DisplayName("Проверка получения продукта по id")
    fun testGetProductById() {
        val mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

        val productJson = """
        {
            "name": "Apples Global Village",
            "description": "Juicy, taste, red",
            "price": 170.0,
            "stock": 5000
        }
        """.trimIndent()

        val result = mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andReturn()

        val productId = JsonPath.parse(result.response.contentAsString)
            .read("$.id", Long::class.java)

        mockMvc.perform(get("/api/products/$productId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.price").value(170.0))
            .andExpect(jsonPath("$.stock").value(5000))
    }

    @Test
    @DisplayName("Проверка удаления продукта")
    fun testDeleteProduct() {
        val mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

        val productJson = """
        {
            "name": "Apples Global Village",
            "description": "Juicy, taste, red",
            "price": 170.0,
            "stock": 5000
        }
        """.trimIndent()

        val result = mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andReturn()

        val productId = JsonPath.parse(result.response.contentAsString)
            .read("$.id", Long::class.java)

        mockMvc.perform(delete("/api/products/$productId"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/products/$productId"))
            .andExpect(status().isNotFound)
    }
}