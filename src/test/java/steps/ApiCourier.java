package steps;

import io.restassured.response.Response;
import pojo.CreatingCourier;
import pojo.LoginCourier;

import static io.restassured.RestAssured.given;

public class ApiCourier extends BaseApi {
    private final String API_DELETE_COURIER="/api/v1/courier/";
    private final String API_LOGIN_COURIER="/api/v1/courier/login";
    private final String API_CREATING_COURIER="/api/v1/courier";

    public Response getDeleteCourier(String id){
        return given()
                .spec(specification)
                .when()
                .delete(API_DELETE_COURIER+ id);
    }
    public Response getLoginCourier(String login, String password){
        return   given()
                .spec(specification)
                .body(new LoginCourier(login, password))
                .post(API_LOGIN_COURIER);
    }

    public Response getCreatingCourier(String login, String password, String firstName) {
        return given()
                .spec(specification)
                .body(new CreatingCourier(login, password, firstName))
                .when()
                .post(API_CREATING_COURIER);
    }
}
