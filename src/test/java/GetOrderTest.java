import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {

    private String paramOrders = "orders";

    @Test
    @DisplayName("В тело ответа возвращается список заказов")
    @Description("Проверка списка заказов")
    public void getOrdersTest() {
        Steps.getOrderList()
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body(paramOrders, notNullValue());
    }
}
