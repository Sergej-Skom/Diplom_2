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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static methods.MethodsCompare.*;

public class CreateOrderTest {

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
        randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, 3);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела после создания заказа")
    @Description("Проверка создания заказа")
    public void testCreateOrder() {
        JSONObject requestBody = new JSONObject();
        String requestForBody = requestBody.put("ingredients", new JSONArray(randomIngredients)).toString();
        Response response = orderClient.create(requestForBody, tokenExtract);

        JsonPath jsonPath = response.jsonPath();
        checkStatusCode(200, response);
        checkSuccessStatusTrue(jsonPath);
        checkNotNullValueString("name", jsonPath);
        checkPresenceNumberOrder(jsonPath);
    }

    @Test
    @DisplayName("Проверка кода статуса для создания заказа без токена")
    @Description("Проверка неудачного ответа")
    public void testCreateOrderWithOutToken() {
        JSONObject requestBody = new JSONObject();
        String request = requestBody.put("ingredients", new JSONArray(randomIngredients)).toString();
        Response response = orderClient.create(request, "");

        JsonPath jsonPath = response.jsonPath();
        checkStatusCode(200, response);
        checkSuccessStatusTrue(jsonPath);
        checkNotNullValueString("name", jsonPath);
        checkPresenceNumberOrder(jsonPath);
    }

    @After
    public void deleteUser () {
        userClient.delete(tokenExtract);
    }
}
