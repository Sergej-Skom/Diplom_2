package methods;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.Assert.*;

public class MethodsCompare {
    @Step("Проверка успешного ответа")
    public static void checkSuccessStatusTrue(JsonPath jsonPath) {
        assertTrue(jsonPath.getBoolean("success"));
    }

    @Step("Проверка неуспешного ответа")
    public static void checkSuccessStatusFalse(JsonPath jsonPath) {
        assertFalse(jsonPath.getBoolean("success"));
    }

    @Step("Проверка наличия email и имени в теле ответа")
    public static void checkPresenceMailAndName(JsonPath jsonPath) {
        Map<String, String> user = jsonPath.getMap("user");
        assertNotNull(user);
        assertTrue(user.containsKey("email"));
        assertTrue(user.containsKey("name"));
    }

    @Step("Проверка наличия не null AccessToken в теле")
    public static void checkNotNullAccessToken(JsonPath jsonPath) {
        assertNotNull(jsonPath.getString("accessToken"));
    }

    @Step("Проверка наличия не null RefreshToken в теле")
    public static void checkNotNullRefreshToken(JsonPath jsonPath) {
        assertNotNull(jsonPath.getString("refreshToken"));
    }

    @Step("Проверка сообщения об ошибке")
    public static void checkErrorMassage(String expected, String actual) {
        assertEquals("Неверное сообщение об ошибке", expected, actual);
    }

    @Step("Проверка наличия номера заказа в теле ответа")
    public static void checkPresenceNumberOrder(JsonPath jsonPath) {
        Map<String, String> order = jsonPath.getMap("order");
        assertNotNull(order);
        assertTrue(order.containsKey("number"));
    }

    @Step("Проверка кода статуса")
    public static void checkStatusCode(int code, Response response) {
        assertEquals("Неверный статус код", code, response.statusCode());
    }

    @Step("Проверка наличия не null значения строки в теле")
    public static void checkNotNullValueString(String value, JsonPath jsonPath) {
        assertNotNull(jsonPath.getString(value));
    }

    @Step("Проверка наличия не null значения списка в теле")
    public static void checkNotNullValueList(String value, JsonPath jsonPath) {
        assertNotNull(jsonPath.getList(value));
    }
}
