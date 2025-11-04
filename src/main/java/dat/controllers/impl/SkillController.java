package dat.controllers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.SkillDAO;
import dat.dtos.SkillDTO;
import dat.external.SkillStatsFetcher;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class SkillController implements IController<SkillDTO, Integer> {

    private final SkillDAO dao;

    public SkillController(){

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = SkillDAO.getInstance(emf);

    }

    @Override
    public void read(Context ctx){

        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        SkillDTO skillDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(skillDTO, SkillDTO.class);

    }

    @Override
    public void readAll(Context ctx){

        List<SkillDTO> skillDTO = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(skillDTO, SkillDTO.class);

    }

    @Override
    public void create(Context ctx){

        SkillDTO jsonRequest = ctx.bodyAsClass(SkillDTO.class);
        SkillDTO skillDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(skillDTO, SkillDTO.class);

    }

    @Override
    public void update(Context ctx){

        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        SkillDTO skillDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(skillDTO, SkillDTO.class);

    }

    @Override
    public void delete(Context ctx){

        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        ctx.res().setStatus(204);

    }

    @Override
    public boolean validatePrimaryKey(Integer integer){

        return dao.validatePrimaryKey(integer);

    }

    @Override
    public SkillDTO validateEntity(Context ctx){

        return ctx.bodyValidator(SkillDTO.class)
                .check(s -> s.getSkillName() != null && !s.getSkillName().isEmpty(), "Skill name must be set")
                .check(s -> s.getSkillDescription() != null && !s.getSkillDescription().isEmpty(),"Skill description must be set")
                .get();

    }

    /*

    public void populate(Context ctx){

        dao.populate();
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Skill database has been populated\" }");

    }

     */

    public void fetchFromExternalApi(Context ctx){

        try{

            List<SkillDTO> skills = dao.readAll();

            List<String> slugs = skills.stream()
                    .map(SkillDTO::getSlug)
                    .toList();

            Map<String, JsonNode> statsMap = SkillStatsFetcher.fetchSkillStats(slugs);

            for (SkillDTO skillDTO : skills){

                JsonNode node = statsMap.get(skillDTO.getSlug());

                if (node != null){

                    skillDTO.setPopularityScore(node.get("popularityScore").asInt());
                    skillDTO.setAverageSalary(node.get("averageSalary").asInt());

                    dao.update(skillDTO.getId(), skillDTO);

                }

            }

            ctx.status(200).json(skills);

        }catch(Exception e){

            e.printStackTrace();
            ctx.status(500).json(Map.of("error","Failed to fetch skill stats from external API"));

        }

    }

}
