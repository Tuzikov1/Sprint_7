import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.Orders;
import steps.ApiOrders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrders {
    ApiOrders step =new ApiOrders();
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String [] color;

    public TestCreateOrders(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                { "Ольга", "Ю", "Каменская, 36 кв.4","Павелецкая","+79639470523",5,"03-02-2023","Желательное после обеда", new String[]{"GREY"}},
                { "Ольга", "Иванов-Задира", "Каменская, 36/5 кв.4","Мира","+79639470523",1,"03-12-2023","Желательное после обеда", new String[]{"GREY","BLACK"}},
                { "ОльгаОльгаОльга", "Ю", "Каменская, 36 кв.4","Павелецкая","+79639470523",10,"03-02-2024","Желательное после обеда", null}
        };
    }

    @Test
    @Description("Создание заказа с различной вариацией цвета самоката")
    public void createOrdersWithDifferentColor(){
        step.getCreateOrders(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color)
                .then()
                .assertThat()
                .body("track",notNullValue());

    }

}
