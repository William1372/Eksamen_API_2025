package dat.routes;

import dat.controllers.impl.ReportController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ReportRoute {

    private final ReportController reportController = new ReportController();

    protected EndpointGroup getRoutes(){

        return() -> {


                get("/candidates/top-by-popularity", reportController::getTopCandidateByPopularity, Role.USER, Role.ADMIN);

        };

    };

}
