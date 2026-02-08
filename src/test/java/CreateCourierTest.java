import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
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
        courierLogin = "CourierLogin" + System.currentTimeMillis();
        courierPass = "password123";
    }

    @After
    public void tearDown() {
        if (courierLogin != null && courierPass != null) {
            String idCourier = Steps.loginCourier(new LoginCourier(courierLogin, courierPass)).then()
                    .log().all()
                    .statusCode(SC_OK)
                    .extract()
                    .path(paramId).toString();
            Steps.deleteCourier(idCourier);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка успешного создания курьера с набором валидных данных")
    public void createNewCourierTest() {
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .body(paramOk, equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка возникновения ошибки при попытке создать курьера с уже существующим набором валидных данных")
    public void cannotCreateDuplicateCourierTest() {
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CONFLICT)
                .body(paramMessage, equalTo(loginError));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Проверка возникновения ошибки при попытке создать курьера без логина")
    public void cannotCreateCourierWithoutLoginTest() {
        courierLogin = null;
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body(paramMessage, equalTo(notEnoughDataError));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Проверка возникновения ошибки при попытке создать курьера без пароля")
    public void cannotCreateCourierWithoutPasswordTest() {
        courierPass = null;
        CreateCourier courier = new CreateCourier(courierLogin, courierPass, firstName);
        Steps.creatingCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body(paramMessage, equalTo(notEnoughDataError));
    }
}
