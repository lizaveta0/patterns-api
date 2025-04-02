import data.DataGeneratorAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGeneratorAPI.*;

class AuthTest {
    private String login;
    private String password;
    private String status;
    private RegistrationDto registrationDto;

    @BeforeEach
    void setUpAll() {
        status = "active";
        registrationDto = DataGeneratorAPI.RegistrationUser.generateUser(status);
        login = registrationDto.getLogin();
        password = registrationDto.getPassword();
        status = registrationDto.getStatus();

        open("http://localhost:9999/");
    }

    @Test
    public void testRegistrationPositive() {
        createRequest(registrationDto).then().statusCode(200);
        $("[name='login']").setValue(login);
        $("[name='password']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("h2").shouldBe(visible, text("  Личный кабинет"));
    }

    @Test
    public void testRegistrationNegativeLoginIncorrect() {
        createRequest(registrationDto).then().statusCode(200);
        String incorrectLogin = DataGeneratorAPI.generateLogin();
        $("[name='login']").setValue(incorrectLogin);
        $("[name='password']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible, text("Ошибка! \nНеверно указан логин или пароль"));
    }

    @Test
    public void testRegistrationNegativePasswordIncorrect() {
        createRequest(registrationDto).then().statusCode(200);
        String incorrectPassword = DataGeneratorAPI.generatePassword();
        $("[name='login']").setValue(login);
        $("[name='password']").setValue(incorrectPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible, text("Ошибка! \nНеверно указан логин или пароль"));
    }

    @Test
    public void testRegistrationUserBlocked() {
        registrationDto = new RegistrationDto(login, password, status = "blocked");
        createRequest(registrationDto).then().statusCode(200);
        $("[name='login']").setValue(login);
        $("[name='password']").setValue(password);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible, text("Ошибка! \nПользователь заблокирован"));
    }
}