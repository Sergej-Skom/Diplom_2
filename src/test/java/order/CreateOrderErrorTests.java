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
import static order.OrderGenerator.createInvalidIngredients;
import static order.OrderGenerator.createNullIngredients;

public class CreateOrderErrorTests {

    private static User user;
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private String tokenExtract;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        user = UserGenerator.randomUser();
        Response response = userClient.create(user);
        tokenExtract = UserToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Проверка кода статуса для недопустимых ингредиентов")
    @Description("Проверка заказа с недопустимым ингредиентом")
    public void testCreateOrderWithInvalidIngredients() {
        List<String> invalidIngredients = createInvalidIngredients();
        JSONObject requestBody = new JSONObject();

        String requestForBody = requestBody.put("ingredients", new JSONArray(invalidIngredients)).toString();
        Response response = orderClient.create(requestForBody, tokenExtract);

        checkStatusCode(500, response);
    }

    @Test
    @DisplayName("Проверка кода статуса и тела для null ингредиентов")
    @Description("Проверка заказа без ингредиентов")
    public void testCreateOrderWithOutIngredients() {
        Response response = orderClient.create(createNullIngredients(), tokenExtract);

        checkStatusCode(400, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("Ingredient ids must be provided", errorMessage);
    }

    @After
    public void deleteUser() {
        userClient.delete(tokenExtract);
    }
}