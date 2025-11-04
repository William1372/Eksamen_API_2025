package dat.routes;

import dat.controllers.impl.CandidateController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoute {

    private final CandidateController candidateController = new CandidateController();

    protected EndpointGroup getRoutes(){

        return() -> {

            get("/populate", candidateController::populate, Role.ADMIN);

            get("/", candidateController::readAll, Role.USER, Role.ADMIN);
            get("/{id}", candidateController::read, Role.USER, Role.ADMIN);

            post("/", candidateController::create, Role.ADMIN);
            put("/{id}", candidateController::update, Role.ADMIN);
            delete("/{id}", candidateController::delete, Role.ADMIN);

        };

    }

}
