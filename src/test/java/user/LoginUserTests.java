package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.url.BaseUrl;

import static user.UserPass.passFromWithMistakeEmail;
import static user.UserPass.passFromWithMistakePassword;
import static methods.MethodsCompare.*;

public class LoginUserTests {
    private String token;
    private final UserClient userClient = new UserClient();
    private static User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        user = UserGenerator.randomUser ();
        userClient.create(user);
    }

    @Test
    @DisplayName("Проверка кода статуса после входа")
    @Description("Проверка успешного входа")
    public void testLoginUser () {
        UserPass courierPass = UserPass.passFrom(user);
        Response response = userClient.pass(courierPass);

        checkStatusCode(200, response);
        token = UserToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Проверка структуры тела ответа после входа")
    @Description("Проверка тела ответа")
    public void testLoginUserWithResponse() {
        UserPass courierPass = UserPass.passFrom(user);
        Response response = userClient.pass(courierPass);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullAccessToken(jsonPath);
        checkNotNullRefreshToken(jsonPath);
        checkPresenceMailAndName(jsonPath);

        token = UserToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела после входа с ошибкой в email")
    @Description("Проверка неудачного входа")
    public void testLoginUserWithMistakeEmail() {
        UserPass courierPass = passFromWithMistakeEmail("3", user);
        Response response = userClient.pass(courierPass);

        checkStatusCode(401, response);
        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("email or password are incorrect", errorMessage);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела после входа с ошибкой в пароле")
    @Description("Проверка неудачного входа")
    public void testLoginUserWithMistakePassword() {
        UserPass courierPass = passFromWithMistakePassword("3", user);
        Response response = userClient.pass(courierPass);

        checkStatusCode(401, response);
        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("email or password are incorrect", errorMessage);
    }

    @After
    public void deleteUser () {
        if (token != null) {
            userClient.delete(token);
        }
    }
}
