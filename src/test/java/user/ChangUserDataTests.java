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

import static user.UserGenerator.randomName;
import static user.UserGenerator.randomPassword;
import static methods.MethodsCompare.*;

public class ChangUserDataTests {
    private static User user;
    private final UserClient userClient = new UserClient();
    private String tokenExtract;
    private String tokenRefresh;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        user = UserGenerator.randomUser();
        Response response = userClient.create(user);
        tokenExtract = UserToken.extractAccessToken(response);
        tokenRefresh = UserToken.extractRefreshToken(response);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела после изменения всех данных клиента")
    @Description("Проверка изменения email, имени и пароля")
    public void testChangAllUserData() {
        User user = UserGenerator.randomUser();
        Response response = userClient.modify(user, tokenExtract);

        checkStatusCode(200, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkPresenceMailAndName(jsonPath);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела после изменения данных клиента без авторизации")
    @Description("Проверка неудачного изменения")
    public void testChangUserDataWithOutAuth() {
        userClient.logout(tokenRefresh);

        User user = UserGenerator.randomUser();
        Response response = userClient.modify(user, "");

        checkStatusCode(401, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("You should be authorised", errorMessage);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела после изменения данных клиента с дублирующимся email")
    @Description("Проверка неудачного изменения")
    public void testChangUserDataWithDuplicateEmail() {
        User user2 = UserGenerator.randomUser();
        Response response2 = userClient.create(user2);
        String tokenExtract2;
        tokenExtract2 = UserToken.extractAccessToken(response2);

        User user = new User()
                .withEmail(user2.getEmail())
                .withPassword(randomPassword())
                .withName(randomName());
        Response response = userClient.modify(user, tokenExtract);

        checkStatusCode(403, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("User with such email already exists", errorMessage);
        userClient.delete(tokenExtract2);
    }

    @After
    public void deleteUser() {
        userClient.delete(tokenExtract);
    }
}
