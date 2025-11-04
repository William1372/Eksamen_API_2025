package dat.routes;


import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final CandidateRoute candidateRoute = new CandidateRoute();
    private final SkillRoute skillRoute = new SkillRoute();
    private final ReportRoute reportRoute = new ReportRoute();

    public EndpointGroup getRoutes() {
        return () -> {

            path("/candidates", candidateRoute.getRoutes());
            path("/skills", skillRoute.getRoutes());
            path("/reports", reportRoute.getRoutes());

        };

    }

}