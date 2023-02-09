import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.ApiCourier;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class TestCreatingCourier {
 ApiCourier step= new ApiCourier();
    private String login = "numbers"+new Random().nextInt(999);
    private String password = "Password"+new Random().nextInt(999);
    private String firsName = "Ivan Petrov"+new Random().nextInt(999);



    @Test
    @Description("Тест для проверки кода ответа")
    public void returnCorrectStatusCode() {
        step.getCreatingCourier(login,password,firsName)
                .then()
                .statusCode(SC_CREATED);
    }

    @Test
    @Description("Успешный запрос возвращает ok: true")
    public void checkAnswer() {
        boolean answer =step.getCreatingCourier(login,password,firsName)
                .then().extract().jsonPath().getBoolean("ok");
        assertEquals(true, answer);
    }

    @Test
    @Description("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        step.getCreatingCourier(null,password,firsName)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        step.getCreatingCourier(login,null,firsName)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    @Test
    @Description("Создание курьера без имени")
    public void createCourierWithoutFirstName() {
        step.getCreatingCourier(login,password,null)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @Description("Проверка появления ошибки при создании курьеров с одним логином")
    public void createTwoCourierWithSameLogin(){
           //Создаем первого курьера
        step.getCreatingCourier(login,password,firsName);
            //Создаем второго курьера
        step.getCreatingCourier(login,password,firsName)
                 .then()
                 .statusCode(SC_CONFLICT)
                 .and()
                 .assertThat()
                 .body("message", equalTo("Этот логин уже используется"));


    }

    @After
    public void deleteCourier() {
        //Логин курьера для получения ID для DELETE
        String id = step.getLoginCourier(login,password)
                .then()
                .extract().jsonPath().getString("id");
        //Удаление курьера
        step.getDeleteCourier(id);
    }
}

