import data.DataGeneratorAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGeneratorAPI.createRequest;

class AuthTest {
    private String login;
    private String password;
    private String status;

    @BeforeEach
    void setUpAll() {
        login = DataGeneratorAPI.RegistrationUser.generateUser().getLogin();
        password = DataGeneratorAPI.RegistrationUser.generateUser().getPassword();
        status = DataGeneratorAPI.RegistrationUser.generateUser().getStatus();

        open("http://localhost:9999/");
    }

    @Test
    public void testRegistrationPositive() {
        createRequest(login, password, status).then().statusCode(200);
        $("[name='login']").setValue(login);
        $("[name='password']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("h2").shouldBe(visible, text("  Личный кабинет"));
    }

    @Test
    public void testRegistrationNegativeLoginIncorrect() {
        createRequest(login, password, status).then().statusCode(200);
        $("[name='login']").setValue("test");
        $("[name='password']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible, text("Ошибка! \nНеверно указан логин или пароль"));
    }

    @Test
    public void testRegistrationNegativePasswordIncorrect() {
        createRequest(login, password, status).then().statusCode(200);
        $("[name='login']").setValue(login);
        $("[name='password']").setValue("password");
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible, text("Ошибка! \nНеверно указан логин или пароль"));
    }

    @Test
    public void testRegistrationUserBlocked() {
        createRequest(login, password, status = "blocked").then().statusCode(200);
        $("[name='login']").setValue(login);
        $("[name='password']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible, text("Ошибка! \nПользователь заблокирован"));
    }
}