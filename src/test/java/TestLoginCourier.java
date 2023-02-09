import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CreatingCourier;
import pojo.LoginCourier;
import steps.ApiCourier;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class TestLoginCourier {
    ApiCourier step=new ApiCourier();
    private String login = "number"+new Random().nextInt(999);
    private String password = "Password"+new Random().nextInt(999);
    private String firsName = "Ivan Petrov"+new Random().nextInt(999);
    private String id;

    @Before
    public void setUp() {
        //Создание курьера
        step.getCreatingCourier(login,password,firsName);
    }

    @Test
    @Description("успешный запрос возвращает id")
    public void returnId(){
           Response response= step.getLoginCourier(login,password);
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
        step.getLoginCourier(login,"password")
               .then()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));

    }
    @Test
    @Description("Логин с некорректным логином")
    public void loginWithWrongLogin(){
        step.getLoginCourier("Орёл",password)
                .then()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @Description("Авторизация без передачи логина")
    public void authorizationWithoutLogin(){
        step.getLoginCourier(null,password)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Авторизация без передачи пароля")
    public void authorizationWithoutPassword(){
        step.getLoginCourier(login,null)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void deleteCourier() {
        //Удаление курьера
        step.getDeleteCourier(id);
    }
}
