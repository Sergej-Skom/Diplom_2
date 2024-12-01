package user;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserToken {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;

    public static UserToken fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserToken.class);
    }

    @Step("Извлечение AccessToken из данных ответа")
    public static String extractAccessToken(Response response) {
        String responseBody = response.getBody().asString();
        UserToken authResponse = UserToken.fromJson(responseBody);
        return authResponse.getAccessToken();
    }

    @Step("Извлечение RefreshToken из данных ответа")
    public static String extractRefreshToken(Response response) {
        String responseBody = response.getBody().asString();
        UserToken authResponse = UserToken.fromJson(responseBody);
        return authResponse.getRefreshToken();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
