package dat.controllers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.controllers.IController;
import dat.daos.impl.CandidateDAO;
import dat.dtos.CandidateDTO;
import dat.dtos.SkillDTO;
import dat.entities.Candidate;
import dat.external.SkillStatsFetcher;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;

public class CandidateController implements IController<CandidateDTO, Integer> {

    private final CandidateDAO dao;

    public CandidateController(){

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = CandidateDAO.getInstance(emf);

    }

    @Override
    public void read(Context ctx){

        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        CandidateDTO candidateDTO = dao.read(id);

        List<String> slugs = candidateDTO.getSkills().stream()
                        .map(SkillDTO::getSlug)
                                .toList();

        Map<String, JsonNode> skillStats = SkillStatsFetcher.fetchSkillStats(slugs);

        candidateDTO.getSkills().forEach(skill -> {
            JsonNode node = skillStats.get(skill.getSlug());
            if (node != null) {
                skill.setPopularityScore(node.get("popularityScore").asInt());
                skill.setAverageSalary(node.get("averageSalary").asInt());
            }
        });

        ctx.res().setStatus(200);
        ctx.json(candidateDTO, CandidateDTO.class);

    }

    @Override
    public void readAll(Context ctx) {
        List<CandidateDTO> candidateList = dao.readAll();

        List<String> allSlugs = candidateList.stream()
                .flatMap(c -> c.getSkills().stream())
                .map(SkillDTO::getSlug)
                .distinct()
                .toList();

        Map<String, JsonNode> skillStats = SkillStatsFetcher.fetchSkillStats(allSlugs);

        candidateList.forEach(candidate ->
                candidate.getSkills().forEach(skill -> {
                    JsonNode node = skillStats.get(skill.getSlug());
                    if (node != null) {
                        skill.setPopularityScore(node.get("popularityScore").asInt());
                        skill.setAverageSalary(node.get("averageSalary").asInt());
                    }
                })
        );

        ctx.status(200).json(candidateList);
    }

    @Override
    public void create(Context ctx){

        CandidateDTO jsonRequest = ctx.bodyAsClass(CandidateDTO.class);
        CandidateDTO candidateDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(candidateDTO, CandidateDTO.class);

    }

    @Override
    public void update(Context ctx){

        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        CandidateDTO candidateDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(candidateDTO, Candidate.class);

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
    public CandidateDTO validateEntity(Context ctx){

        return ctx.bodyValidator(CandidateDTO.class)
                .check(c -> c.getCandidateName() != null && !c.getCandidateName().isEmpty(), "Candidate name must be set")
                .check(c -> c.getCandidatePhoneNumber() != null && !c.getCandidatePhoneNumber().isEmpty(), "Candidate phone number must be set")
                .check(c -> c.getCandidateEducation() != null && !c.getCandidateEducation().isEmpty(), "Candidate educational background must be set")
                .get();

    }


    public void populate(Context ctx){

        Populate.main(null);
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Candidate database has been populated\" }");

    }

}
