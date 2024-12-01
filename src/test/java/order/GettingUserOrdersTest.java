package order;

import service.url.BaseUrl;
import user.User;
import user.UserClient;
import user.UserGenerator;
import user.UserToken;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static methods.MethodsCompare.*;

public class GettingUserOrdersTest {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private static User user;
    private String tokenExtract;
    private List<String> randomIngredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        user = UserGenerator.randomUser ();
        Response response = userClient.create(user);
        tokenExtract = UserToken.extractAccessToken(response);

        Response responseIngredients = orderClient.infoIngredients();
        randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, 4);
        randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, 3);
    }

    @Test
    @DisplayName("Проверка кода статуса для заказов клиента")
    @Description("Проверка заказов клиента")
    public void testGetUserOrdersCode() {
        Response response = orderClient.userOrders(tokenExtract);
        checkStatusCode(200, response);
    }

    @Test
    @DisplayName("Проверка тела ответа для заказов клиента")
    @Description("Проверка тела ответа")
    public void testGetUserOrdersBody() {
        Response response = orderClient.userOrders(tokenExtract);
        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullValueList("orders", jsonPath);
        checkNotNullValueString("total", jsonPath);
        checkNotNullValueString("totalToday", jsonPath);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела для заказов клиента без авторизации")
    @Description("Проверка неудачного ответа")
    public void testGetUserOrdersWithOutAuthorization() {
        Response response = orderClient.userOrders("");
        checkStatusCode(401, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("You should be authorised", errorMessage);
    }

    @After
    public void deleteUser () {
        userClient.delete(tokenExtract);
    }
}

