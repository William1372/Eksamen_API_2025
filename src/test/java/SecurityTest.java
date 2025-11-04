import dat.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityTest {

    private static Javalin app;
    private static String jwtToken;

    @BeforeAll
    static void setup(){

        app = ApplicationConfig.startServer(7070);
        baseURI = "http://localhost";
        port = 7070;

    }

    @AfterAll
    static void tearDown(){

        app.stop();

    }

    @Test
    @Order(1)
    void registerUser(){

        jwtToken = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"Test Bruger\",\"password\":\"Test123!\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .extract()
                .path("token");

    }

    @Test
    @Order(2)
    void loginUser(){

        jwtToken = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"Test Bruger\",\"password\":\"Test123!\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

    }

    @Test
    @Order(3)
    void accessUserProtectedEndpoint(){

        given()
                .header("Authorization", "Bearer "+jwtToken)
                .when()
                .get("/api/protected/user_demo")
                .then()
                .statusCode(200)
                .body("msg", containsString("USER"));

    }

    @Test
    @Order(4)
    void accessAdminProtectedEndpoint(){

        given()
                .header("Authorization", "Bearer "+jwtToken)
                .when()
                .get("/api/protected/admin_demo")
                .then()
                .statusCode(401);

    }

}
