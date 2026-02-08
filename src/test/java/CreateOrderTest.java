import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

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


    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime,
                           String deliveryDate, String comment, String[] color) {
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

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Ivan", "Ivanov", "Festivalnaya, 4", "Rechnoy vokzal", "88005553535", 2, "05.05.2026", "test", new String[] {"BLACK"}},
                {"Petr", "Petrov", "Flotskaya, 28", "Khovrino", "89008887766", 1, "02.03.2026", "test2", new String[] {"GREY"}},
                {"Lucas", "Feraz", "Brazilskaya, 250", "Sokol", "89879872233", 2, "03.04.2026", "test3", new String[] {"BLACK", "GREY"}},
                {"Anna", "Ivanova", "Pushkina, 42", "Vodny stadion", "88005556677", 2, "12.09.2026", "test4", null},
        };
    }

    @Test
    public void createOrder() {
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Steps.createOrder(createOrder)
                .then()
                .log().all()
                .statusCode(201)
                .body(paramTrack, notNullValue());
    }

}
