import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private String colorTest;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    private String paramTrack = "track";
    String track;


    public CreateOrderTest(String colorTest, String firstName, String lastName, String address, String metroStation, String phone, int rentTime,
                           String deliveryDate, String comment, String[] color) {
        this.colorTest = colorTest;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @After
    public void tearDown() {
        if (track != null) {
            Steps.cancelOrder(track);
        }
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {"Черный", "Ivan", "Ivanov", "Festivalnaya, 4", "Rechnoy vokzal", "88005553535", 2, "05.05.2026", "test", new String[] {"BLACK"}},
                {"Серый", "Petr", "Petrov", "Flotskaya, 28", "Khovrino", "89008887766", 1, "02.03.2026", "test2", new String[] {"GREY"}},
                {"Черный и серый", "Lucas", "Feraz", "Brazilskaya, 250", "Sokol", "89879872233", 2, "03.04.2026", "test3", new String[] {"BLACK", "GREY"}},
                {"Без цвета", "Anna", "Ivanova", "Pushkina, 42", "Vodny stadion", "88005556677", 2, "12.09.2026", "test4", null},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с набором валидных данных")
    public void createOrderTest() {
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = Steps.createOrder(createOrder)
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .body(paramTrack, notNullValue())
                .extract().response();
        track = response.path(paramTrack).toString();
    }

}
