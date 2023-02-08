import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CreatingCourier;
import pojo.LoginCourier;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class TestCreatingCourier {
    private String login = "numbers"+new Random().nextInt(999);
    private String password = "Password"+new Random().nextInt(999);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @Description("Тест для проверки кода ответа")
    public void returnCorrectStatusCode() {
        given()
                .header("Content-type", "application/json")
                .body(new CreatingCourier(login, password, "Ivan Petrov"))
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Test
    @Description("Успешный запрос возвращает ok: true")
    public void checkAnswer() {
        boolean answer =
                given()
                        .header("Content-type", "application/json")
                        .body(new CreatingCourier(login, password, "Ivan Petrov"))
                        .when()
                        .post("/api/v1/courier")
                        .then().extract().jsonPath().getBoolean("ok");
        assertEquals(true, answer);
    }

    @Test
    @Description("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        given()
                .header("Content-type", "application/json")
                .body(new CreatingCourier(null, password, "Ivan Petrov"))
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        given()
                .header("Content-type", "application/json")
                .body(new CreatingCourier(login, null, "Ivan Petrov"))
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    @Test
    @Description("Создание курьера без имени")
    public void createCourierWithoutFirstName() {
        given()
                .header("Content-type", "application/json")
                .body(new CreatingCourier(login, password, null))
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @Description("Проверка появления ошибки при создании курьеров с одним логином")
    public void createTwoCourierWithSameLogin(){
           //Создаем первого курьера
                 Response courier1=  given()
                .header("Content-type", "application/json")
                .body(new CreatingCourier(login, password, "Петров Иван"))
                .when()
                .post("/api/v1/courier");

            //Создаем второго курьера
                 given()
                .header("Content-type", "application/json")
                 .body(new CreatingCourier(login, password, "Петров Иван"))
                 .when()
                 .post("/api/v1/courier")
                 .then()
                 .statusCode(409)
                 .and()
                 .assertThat()
                 .body("message", equalTo("Этот логин уже используется"));


    }

    @After
    public void deleteCourier() {
        //Логин курьера для получения ID для DELETE
        String id = given()
                .header("Content-type", "application/json")
                .body(new LoginCourier(login, password))
                .post("/api/v1/courier/login")
                .then()
                .extract().jsonPath().getString("id");
        //Удаление курьера
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
    }
}

