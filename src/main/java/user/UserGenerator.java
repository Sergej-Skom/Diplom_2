package user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserGenerator {
    @Step("Генерация случайного email")
    public static String randomEmail() {
        Faker faker = new Faker();
        return faker.internet().safeEmailAddress();
    }

    @Step("Генерация случайного пароля")
    public static String randomPassword() {
        Faker faker = new Faker();
        return faker.internet().password(8, 9);
    }

    @Step("Генерация случайного имени")
    public static String randomName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    @Step("Генерация случайного клиента для создания")
    public static User randomUser() {
        return new User()
                .withEmail(randomEmail())
                .withPassword(randomPassword())
                .withName(randomName());
    }
}
