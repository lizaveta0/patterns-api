package data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGeneratorAPI {
    private static Faker faker = new Faker(new Locale("ru"));

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    private DataGeneratorAPI() {
    }

    public static String generateLogin() {
        return faker.name().name();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static Response createRequest(RegistrationDto registrationDto) {
        return given()
                .spec(requestSpec)
                .body(registrationDto)
                .when() // "когда"
                .post("/api/system/users");// на какой путь относительно BaseUri отправляем запрос
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

    public static class RegistrationUser {
        private RegistrationUser() {
        }

        public static RegistrationDto generateUser(String status) {
            return new RegistrationDto(generateLogin(), generatePassword(), status);
        }
    }
}
