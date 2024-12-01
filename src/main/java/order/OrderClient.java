package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String INFO_INGREDIENTS = "/api/ingredients";
    private static final String CREATE_ORDERS = "/api/orders";
    private static final String ALL_ORDERS = "/api/orders/all";
    private static final String USER_ORDERS = "/api/orders";

    @Step("API получить информацию об ингредиентах")
    public Response infoIngredients() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(INFO_INGREDIENTS);
    }

    @Step("API создать заказ клиента")
    public Response create(String Ids, String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(Ids)
                .when()
                .post(CREATE_ORDERS);
    }

    @Step("API получить все заказы")
    public Response infoAllOrders() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(ALL_ORDERS);
    }

    @Step("API получить заказы клиента")
    public Response userOrders(String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(USER_ORDERS);
    }
}

