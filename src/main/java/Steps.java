import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Steps {
    @Step("Создание курьера")
    public static Response creatingCourier(CreateCourier courier) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public static Response loginCourier(LoginCourier courier) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
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
                .when()
                .delete("/api/v1/courier/" + idCourier);
    }

    @Step("Создание заказа")
    public static Response createOrder(CreateOrder order) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public static Response getOrderList() {
        return given().log().all()
                .when()
                .get("/api/v1/orders");
    }
}
