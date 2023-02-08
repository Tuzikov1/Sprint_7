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
import static org.hamcrest.CoreMatchers.notNullValue;


public class TestLoginCourier {

    private String login = "number"+new Random().nextInt(999);
    private String password = "Password"+new Random().nextInt(999);
    private String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        //Создание курьера
        given()
                .header("Content-type", "application/json")
                .body(new CreatingCourier(login, password, "Ivan Petrov"))
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @Description("успешный запрос возвращает id")
    public void returnId(){
           Response response= given()
                .header("Content-type", "application/json")
                .body(new LoginCourier(login, password))
                .post("/api/v1/courier/login");
            response.then()
                       .assertThat()
                       .body("id",notNullValue());
               // Id для удаления курьера в After
        id= response.then()
                 .extract().jsonPath().getString("id");
    }
    @Test
    @Description("Логин с некорректным паролем")
    public void loginWithWrongPassword(){
                given()
                .header("Content-type", "application/json")
                .body(new LoginCourier(login, "5164"))
                .post("/api/v1/courier/login")
               .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));

    }
    @Test
    @Description("Логин с некорректным логином")
    public void loginWithWrongLogin(){
        given()
                .header("Content-type", "application/json")
                .body(new LoginCourier("Орёл", password))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @Description("Авторизация без передачи логина")
    public void authorizationWithoutLogin(){
        given()
                .header("Content-type", "application/json")
                .body(new LoginCourier(null, password))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Авторизация без передачи пароля")
    public void authorizationWithoutPassword(){
        given()
                .header("Content-type", "application/json")
                .body(new LoginCourier(login, null))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void deleteCourier() {
        //Удаление курьера
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
    }
}
