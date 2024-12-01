package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String CREATE_USER = "/api/auth/register";
    private static final String PASS_USER = "/api/auth/login";
    private static final String MODIFY_USER = "/api/auth/user";
    private static final String INFO_USER = "/api/auth/user";
    private static final String LOGOUT_USER = "/api/auth/logout";
    private static final String DELETE_USER = "/api/auth/user";

    @Step("API отправить запрос на создание клиента")
    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("API отправить запрос на вход клиента")
    public Response pass(UserPass pass) {
        return given()
                .header("Content-type", "application/json")
                .and().body(pass)
                .when()
                .post(PASS_USER);
    }

    @Step("API отправить запрос на изменение данных клиента")
    public Response modify(User user, String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and().body(user)
                .when()
                .patch(MODIFY_USER);
    }

    @Step("API получить информацию о клиенте")
    public Response info(String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(INFO_USER);
    }

    @Step("API отправить запрос на выход клиента")
    public Response logout(String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(LOGOUT_USER);
    }

    @Step("API удалить этого клиента")
    public Response delete(String token) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_USER);
    }
}
