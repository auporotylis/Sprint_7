import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Steps {
    @Step("Создание курьера")
    public static Response creatingCourier(CreateCourier courier) {
        return given()
                .spec(RestAssuredSpecs.requestSpec)
                .body(courier)
                .when()
                .post(Endpoints.courier);
    }

    @Step("Логин курьера")
    public static Response loginCourier(LoginCourier courier) {
        return given()
                .spec(RestAssuredSpecs.requestSpec)
                .body(courier)
                .when()
                .post(Endpoints.logInCourier);
    }

    @Step("Получение id курьера")
    public static String getCourierId(Response loginCourier) {
        return loginCourier
                .then()
                .log().all()
                .extract()
                .path("id").toString();
    }

    @Step("Удаление курьера")
    public static void deleteCourier(String idCourier) {
        given()
                .spec(RestAssuredSpecs.requestSpec)
                .when()
                .delete(Endpoints.courier + "/" + idCourier);
    }

    @Step("Создание заказа")
    public static Response createOrder(CreateOrder order) {
        return given()
                .spec(RestAssuredSpecs.requestSpec)
                .body(order)
                .when()
                .post(Endpoints.orders);
    }

    @Step("Получение списка заказов")
    public static Response getOrderList() {
        return given()
                .spec(RestAssuredSpecs.requestSpec)
                .when()
                .get(Endpoints.orders);
    }

    @Step("Отмена заказа")
    public static void cancelOrder(String track) {
        given()
                .spec(RestAssuredSpecs.requestSpec)
                .body("{\"track\": \"" + track + "\"}")
                .when()
                .put(Endpoints.cancelOrder);
    }
}
