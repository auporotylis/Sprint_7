import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        login = "CourierLogin" + System.currentTimeMillis();
        password = "password123";
        CreateCourier courier = new CreateCourier(login, password, "Dasha");
        Steps.creatingCourier(courier).then().log().all().statusCode(201).extract().response();
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
    public void logInCourier() {
        LoginCourier courier = new LoginCourier(login, password);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(200)
                .body(paramId, notNullValue())
                .extract().response();
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    public void courierCannotLogInWithoutLogin() {
        LoginCourier courier = new LoginCourier("", password);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(400)
                .body(paramMessage, equalTo(notEnoughDataError));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без пароля")
    public void courierCannotLogInWithoutPassword() {
        LoginCourier courier = new LoginCourier(login, "");
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(400)
                .body(paramMessage, equalTo(notEnoughDataError));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с ошибкой в логине")
    public void courierCannotLogInWithWrongLogin() {
        String wrongLogin = login + "a";
        LoginCourier courier = new LoginCourier(wrongLogin, password);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(404)
                .body(paramMessage, equalTo(accountNotFoundError));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с ошибкой в пароле")
    public void courierCannotLogInWithWrongPassword() {
        String wrongPassword = password + "a";
        LoginCourier courier = new LoginCourier(login, wrongPassword);
        Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(404)
                .body(paramMessage, equalTo(accountNotFoundError));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void successLogInReturnCourierId() {
        LoginCourier courier = new LoginCourier(login, password);
        Response loginCourier = Steps.loginCourier(courier).then()
                .log().all()
                .statusCode(200)
                .body(paramId, notNullValue())
                .extract().response();
        id = Steps.getCourierId(loginCourier);
    }

}
