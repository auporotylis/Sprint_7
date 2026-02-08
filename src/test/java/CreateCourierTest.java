import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private String courierLogin;
    private String courierPass;

    private String paramId = "id";
    private String paramOk = "ok";
    private String paramMessage = "message";

    private String loginError = "Этот логин уже используется. Попробуйте другой.";
    private String notEnoughDataError = "Недостаточно данных для создания учетной записи";

    private String firstName = "Alex";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierLogin = "CourierLogin" + System.currentTimeMillis();
        courierPass = "password123";
    }

    @After
    public void tearDown() {
        if (courierLogin != null && courierPass != null) {
            String idCourier = Steps.loginCourier(new LoginCourier(courierLogin, courierPass)).then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .path(paramId).toString();
            Steps.deleteCourier(idCourier);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    public void createNewCourier() {
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(201)
                .body(paramOk, equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCourier() {
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));

        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(409)
                .body(paramMessage, equalTo(loginError));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void cannotCreateCourierWithoutLogin() {
        courierLogin = null;
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(400)
                .body(paramMessage, equalTo(notEnoughDataError));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void cannotCreateCourierWithoutPassword() {
        courierPass = null;
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(400)
                .body(paramMessage, equalTo(notEnoughDataError));
    }
}
