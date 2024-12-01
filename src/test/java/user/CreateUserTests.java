package user;

import service.url.BaseUrl;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static user.UserGenerator.*;
import static methods.MethodsCompare.*;

public class CreateUserTests {

    private final UserClient userClient = new UserClient();
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }

    @Test
    @DisplayName("Проверьте код статуса после авторизации")
    @Description("Проверка правильности авторизации")
    public void testCreateUser() {

        User user = UserGenerator.randomUser();
        Response response = userClient.create(user);
        checkStatusCode(200, response);

        token = UserToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Проверьте структуру тела ответа после авторизации")
    @Description("Проверка тела ответа")
    public void testCreateUserWithResponse() {

        User user = UserGenerator.randomUser();
        Response response = userClient.create(user);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullAccessToken(jsonPath);
        checkNotNullRefreshToken(jsonPath);
        checkPresenceMailAndName(jsonPath);

        token = UserToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Проверьте код статуса при создании дубликата клиента")
    @Description("Проверка кода двойной регистрации")
    public void testCreateUserDuplicate() {
        User user = UserGenerator.randomUser();
        Response response = userClient.create(user);

        token = UserToken.extractAccessToken(response);

        Response responseDuplicate = userClient.create(user);
        checkStatusCode(403, responseDuplicate);
    }

    @Test
    @DisplayName("Проверьте тело ответа после попытки дублирования авторизации")
    @Description("Проверка тела двойной регистрации")
    public void testCreateUserDuplicateWithResponse() {

        User user = UserGenerator.randomUser();
        Response response = userClient.create(user);
        token = UserToken.extractAccessToken(response);
        Response responseDuplicate = userClient.create(user);

        JsonPath jsonPath = responseDuplicate.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("User already exists", errorMessage);
    }

    @Test
    @DisplayName("Проверьте код статуса и тело после авторизации без имени")
    @Description("Проверка некорректной регистрации")
    public void testCreateUserWithOutName() {
        User user = new User()
                .withEmail(randomEmail())
                .withPassword(randomPassword());
        Response response = userClient.create(user);

        checkStatusCode(403, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String actualErrorMessage = jsonPath.getString("message");
        checkErrorMassage("Email, password and name are required fields", actualErrorMessage);
    }

    @Test
    @DisplayName("Проверьте код статуса и тело после авторизации без электронной почты")
    @Description("Проверка некорректной регистрации")
    public void testCreateUserWithOutEmail() {
        User user = new User()
                .withEmail(randomName())
                .withPassword(randomPassword());
        Response response = userClient.create(user);

        checkStatusCode(403, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String actualErrorMessage = jsonPath.getString("message");
        checkErrorMassage("Email, password and name are required fields", actualErrorMessage);
    }

    @Test
    @DisplayName("Проверьте код статуса и тело после авторизации без пароля")
    @Description("Проверка некорректной регистрации")
    public void testCreateUserWithOutPassword() {
        User user = new User()
                .withPassword(randomName())
                .withEmail(randomEmail());
        Response response = userClient.create(user);

        checkStatusCode(403, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String actualErrorMessage = jsonPath.getString("message");
        checkErrorMassage("Email, password and name are required fields", actualErrorMessage);
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userClient.delete(token);
        }
    }
}
