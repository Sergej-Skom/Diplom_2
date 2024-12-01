package order;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderGenerator {
    @Step("Создать список с извлеченными Id из списка заказов")
    public static List<String> extractIdsFromResponse(Response response) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.getBody().asString(), JsonObject.class);

        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        List<String> idList = new ArrayList<>();
        for (JsonElement element : dataArray) {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.has("_id")) {
                    String id = obj.get("_id").getAsString();
                    idList.add(id);
                }
            }
        }
        return idList;
    }

    @Step("Создать список с случайными ингредиентами")
    public static List<String> createRandomIngredients(Response response, int countIngredients) {
        List<String> idList = extractIdsFromResponse(response);
        List<String> randomIngredients = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < countIngredients; i++) {
            int randomIndex = random.nextInt(idList.size());
            String selectedId = idList.get(randomIndex);
            randomIngredients.add(selectedId);
        }
        return randomIngredients;
    }

    @Step("Создать недействительные ингредиенты")
    public static List<String> createInvalidIngredients() {
        List<String> invalidIngredients = new ArrayList<>();
        invalidIngredients.add("error000id000ingredient");
        return invalidIngredients;
    }

    @Step("Создать ингредиенты с null id")
    public static String createNullIngredients() {
        return "";
    }
}
