package user;

import io.qameta.allure.Step;

public class UserPass {
    private final String email;
    private final String password;

    public UserPass(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Step("Записать email и пароль текущего клиента")
    public static UserPass passFrom(User user) {
        return new UserPass(user.getEmail(), user.getPassword());
    }

    @Step("Записать ошибочный email и пароль текущего клиента")
    public static UserPass passFromWithMistakeEmail(String addInEmail, User user) {
        return new UserPass(addInEmail + user.getEmail(), user.getPassword());
    }

    @Step("Записать email и ошибочный пароль текущего клиента")
    public static UserPass passFromWithMistakePassword(String addInPassword, User user) {
        return new UserPass(user.getEmail(), user.getPassword() + addInPassword);
    }
}
