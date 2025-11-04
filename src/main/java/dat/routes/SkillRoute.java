package dat.routes;

import dat.config.Populate;
import dat.controllers.impl.SkillController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SkillRoute {

    private final SkillController skillController = new SkillController();

    protected EndpointGroup getRoutes(){

        return() -> {

            get("/populate",ctx -> {

                Populate.main(null);
                ctx.status(200).result("Database has been populated!");

            }, Role.ADMIN);

            get("/fetch", skillController::fetchFromExternalApi, Role.USER, Role.ADMIN);

            get("/", skillController::readAll, Role.USER, Role.ADMIN);
            get("/{id}", skillController::read, Role.USER, Role.ADMIN);

            post("/", skillController::create, Role.ADMIN);
            put("/{id}", skillController::update, Role.ADMIN);
            delete("/{id}", skillController::delete, Role.ADMIN);

        };

    }

}
