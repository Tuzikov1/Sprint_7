package steps;

import io.restassured.response.Response;
import pojo.Orders;

import static io.restassured.RestAssured.given;

public class ApiOrders extends BaseApi {
    private final String API_ORDERS="/api/v1/orders";

    public Response getCreateOrders(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color){
        return given()
                .spec(specification)
                .body(new Orders(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color))
                .post(API_ORDERS);
    }
    public Response getListOrders(){
        return given()
                .spec(specification)
                .get(API_ORDERS);
    }
}
