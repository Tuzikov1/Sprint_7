import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import steps.ApiOrders;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestListOrders {
    ApiOrders step =new ApiOrders();
    @Test
    public void ordersInBodyResponse(){
        step.getListOrders()
                .then()
                .assertThat()
                .body("orders",notNullValue());
    }
}
