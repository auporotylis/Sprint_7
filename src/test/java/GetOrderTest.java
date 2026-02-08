import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {

    private String paramOrders = "orders";
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("В тело ответа возвращается список заказов.")
    public void getOrders() {
        Steps.getOrderList()
                .then()
                .log().all()
                .statusCode(200)
                .body(paramOrders, notNullValue());
    }
}
