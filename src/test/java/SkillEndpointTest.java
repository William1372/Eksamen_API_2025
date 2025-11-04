import dat.config.ApplicationConfig;
import dat.config.Populate;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SkillEndpointTest {

    private Javalin app;
    private String jwtToken;
    private int skillId;

    @BeforeAll
    void setup(){

        app = ApplicationConfig.startServer(7072);
        baseURI = "http://localhost";
        port = 7072;

        given().contentType(ContentType.JSON)
                .body("{\"username\":\"Skill Test\",\"password\":\"Skill123!\"}")
                .post("/api/auth/register");

        jwtToken = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"Skill Test\",\"password\":\"Skill123!\"}")
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        Populate.main(null);

    }

    @AfterAll
    void tearDown(){

        app.stop();

    }

    @Test
    @Order(1)
    void getAllSkills(){

        given()
                .header("Authorization", "Bearer "+jwtToken)
                .when()
                .get("/api/skills")
                .then()
                .statusCode(200)
                .body("$", not(empty()));

    }

    @Test
    @Order(2)
    void getSkillById(){

        given()
                .header("Authorization", "Bearer "+jwtToken)
                .pathParam("id",1)
                .when()
                .get("/api/skills/{id}")
                .then()
                .statusCode(anyOf(is(200),is(404)));

    }

    @Test
    @Order(3)
    void fetchSkillsFromApiEndpoint(){

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/skills/fetch")
                .then()
                .statusCode(200)
                .body("$", not(empty()));

    }

}
