import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private String login;
    private String password;
    private String id;

    private String paramId = "id";
    private String paramMessage = "message";
    private String notEnoughDataError = "Недостаточно данных для входа";
    private String accountNotFoundError = "Учетная запись не найдена";


    @Before
    public void setUp() {
        login = "CourierLogin" + System.currentTimeMillis();
        password = "password123";
        CreateCourier courier = new CreateCourier(login, password, "Dasha");
        Steps.creatingCourier(courier).then().log().all().statusCode(SC_CREATED).extract().response();
    }
    @After
    public void tearDown() {
        if (id == null) {
            Response loginCourier = Steps.loginCourier(new LoginCourier(login, password));
            id = Steps.getCourierId(loginCourier);
        }
        Steps.deleteCourier(id);
    }


    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверка успешной авторизации курьера с существующим набором данных")
    public void logInCourierTest() {
        LoginCourier courier = new LoginCourier(login, password);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(SC_OK)
                .body(paramId, notNullValue())
                .extract().response();
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    @Description("Проверка возникновения ошибки при попытке авторизации курьера без логина")
    public void courierCannotLogInWithoutLoginTest() {
        LoginCourier courier = new LoginCourier("", password);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body(paramMessage, equalTo(notEnoughDataError));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без пароля")
    @Description("Проверка возникновения ошибки при попытке авторизации курьера без пароля")
    public void courierCannotLogInWithoutPasswordTest() {
        LoginCourier courier = new LoginCourier(login, "");
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body(paramMessage, equalTo(notEnoughDataError));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с ошибкой в логине")
    @Description("Проверка возникновения ошибки при попытке авторизации курьера с ошибкой в логине")
    public void courierCannotLogInWithWrongLoginTest() {
        String wrongLogin = login + "a";
        LoginCourier courier = new LoginCourier(wrongLogin, password);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .body(paramMessage, equalTo(accountNotFoundError));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с ошибкой в пароле")
    @Description("Проверка возникновения ошибки при попытке авторизации курьера с ошибкой в пароле")
    public void courierCannotLogInWithWrongPasswordTest() {
        String wrongPassword = password + "a";
        LoginCourier courier = new LoginCourier(login, wrongPassword);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .body(paramMessage, equalTo(accountNotFoundError));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    @Description("Проверка возвращения id курьера при успешном запросе")
    public void successLogInReturnCourierIdTest() {
        LoginCourier courier = new LoginCourier(login, password);
        Response loginCourier = Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(SC_OK)
                .body(paramId, notNullValue())
                .extract().response();
        id = Steps.getCourierId(loginCourier);
    }
}
