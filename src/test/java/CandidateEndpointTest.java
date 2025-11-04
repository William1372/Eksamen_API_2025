import dat.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CandidateEndpointTest {

    private Javalin app;
    private int candidateId;
    private String jwtToken;


    @BeforeAll
    void setup(){

        app = ApplicationConfig.startServer(7071);
        baseURI = "http://localhost";
        port = 7071;

        given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"William Øster\",\"password\":\"Test123!\"}")
                .when()
                .post("/api/auth/register");

        jwtToken = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"William Øster\",\"password\":\"Test123!\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        Assertions.assertNotNull(jwtToken, "Token should not be null after login");

    }

    @AfterAll
    void tearDown(){

        app.stop();

    }

    @Test
    @Order(1)
    void createCandidate(){

        candidateId = given()
                .header("Authorization", "Bearer " + jwtToken) // <-- vigtigt!
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "candidateName": "William Øster",
                          "candidatePhoneNumber": "31520223",
                          "candidateEducation": "Datamatiker"
                        }
                        """)
                .when()
                .post("/api/candidates")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("candidateName", equalTo("William Øster"))
                .extract()
                .path("id");

    }

    @Test
    @Order(2)
    void getCandidateById(){

        given()
                .pathParam("id", candidateId)
                .when()
                .get("/api/candidates/{id}")
                .then()
                .statusCode(200)
                .body("candidateName", equalTo("William Øster"));

    }

    @Test
    @Order(3)
    void updateCandidate(){

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", candidateId)
                .body("""
                        {
                          "candidateName": "William Danø",
                          "candidatePhoneNumber": "31701214",
                          "candidateEducation": "Software Engineering"
                        }
                        """)
                .when()
                .put("/api/candidates/{id}")
                .then()
                .statusCode(200)
                .body("candidateName", equalTo("William Danø"));

    }

    @Test
    @Order(4)
    void getAllCandidates(){

        when()
                .get("/api/candidates")
                .then()
                .statusCode(200)
                .body("$", not(empty()));

    }

    @Test
    @Order(5)
    void deleteCandidate(){

        given()
                .pathParam("id", candidateId)
                .when()
                .delete("/api/candidates/{id}")
                .then()
                .statusCode(204);

    }

}
