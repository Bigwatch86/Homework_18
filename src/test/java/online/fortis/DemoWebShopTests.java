package online.fortis;

import org.junit.jupiter.api.Disabled;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class DemoWebShopTests {
    String cookie = "ARRAffinity=a1e87db3fa424e3b31370c78def315779c40ca259e77568bef2bb9544f63134e; " +
            "__utmc=78382081; __utmz=78382081.1646823575.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); " +
            "__utma=78382081.1804099171.1646823575.1646823575.1646828424.2; " +
            "__RequestVerificationToken=kLe-WpCDqCmapc8nLRsWOSHwyjJUW9oi59RHhiTA2pOCRgdW71CoQz_tZv4yPOj68NTTRBhCrSkXUWASxbXxJaSCeWL66pDi6q2nW-uwPmc1; " +
            "ASP.NET_SessionId=hndtz1a1gyqgqpxadqjgztd0; " +
            "NOPCOMMERCE.AUTH=DE707F0702556790CEF0FF962453E11BF1ED6714534E46021DDD15719AE6D1C01A6F9F8E70A6C5324604164788B6345647F7FAC1C47F9EBD578A7A139D24E7423433720FE814BA3F74094897E76BACFF3D64DD9257CD5F19F8E78BA1E8B929099E3EB24AB671B7F0B994A14E9633A7082ED35D3758C5131F6BDA46E77854F0E14A8745C044BACE0096146CE46BCFC341; " +
            "Nop.customer=33101246-6d39-449a-a36f-cec7c3555181; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=43&RecentlyViewedProductIds=72; " +
            "__atuvc=2%7C10; " +
            "__atuvs=6228a2888fb9aa54001; " +
            "__utmt=1; " +
            "__utmb=78382081.56.10.1646828424";

    @Test
    @DisplayName("Добавление товара в корзину.")
    void addToCartTest() {
        //добавление товара в корзину
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(cookie)
                .body("addtocart_43.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/43/1")
                .then()
                .log().body()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(1)"));

        //авторизация через АПИ
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", "test666@test.ru")
                        .formParam("Password", "123456789")
                        .formParam("RememberMe", "true")
                        .when()
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .log().body()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");


        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));

        //Проверка добавления в корзину
        open("http://demowebshop.tricentis.com/");
        $(".cart-qty").shouldHave(text("1"));

    }

    @Test
    @DisplayName("Добавление товара в избранное")
    void addToWishlistTest(){
        //добавление товара в избранное
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(cookie)
                .body("addtocart_43.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/43/2")
                .then()
                .log().body()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/wishlist\">wishlist</a>"));

        //авторизация через АПИ
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", "test666@test.ru")
                        .formParam("Password", "123456789")
                        .formParam("RememberMe", "true")
                        .when()
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .log().all()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");


        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));

        //Проверка добавления в избранное
        open("http://demowebshop.tricentis.com/");
        $(".wishlist-qty").shouldHave(text("1"));
    }


}

