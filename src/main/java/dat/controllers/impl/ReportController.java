package dat.controllers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import dat.config.HibernateConfig;
import dat.daos.impl.CandidateDAO;
import dat.dtos.CandidateDTO;
import dat.dtos.SkillDTO;

import dat.external.SkillStatsFetcher;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ReportController {

    private final CandidateDAO dao;

    public ReportController(){

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = CandidateDAO.getInstance(emf);

    }

    public void getTopCandidateByPopularity(Context ctx){

        List<CandidateDTO> candidates = dao.readAll();

        List<String> allSlugs = candidates.stream()
                .flatMap(c -> c.getSkills().stream())
                .map(SkillDTO::getSlug)
                .distinct()
                .toList();

        Map<String, JsonNode> stats = SkillStatsFetcher.fetchSkillStats(allSlugs);

        candidates.forEach(candidate -> candidate.getSkills().forEach(skill -> {
            JsonNode node = stats.get(skill.getSlug());

            if (node != null) {

                skill.setPopularityScore(node.get("popularityScore").asInt());

            }

        }));

        CandidateDTO topCandidate = candidates.stream()
                .max(Comparator.comparingDouble(c -> c.getSkills().stream()
                        .mapToInt(s -> s.getPopularityScore() != null ? s.getPopularityScore() : 0)
                        .average()
                        .orElse(0)))
                .orElse(null);

        if (topCandidate != null){

            double avgPopularity = topCandidate.getSkills().stream()
                    .mapToInt(s -> s.getPopularityScore() != null ? s.getPopularityScore() : 0)
                    .average()
                    .orElse(0);

            ctx.status(200).json(Map.of(
                    "candidateId", topCandidate.getId(),
                    "candidateName", topCandidate.getCandidateName(),
                    "averagePopularity", avgPopularity
            ));

        }else{

            ctx.status(404).json(Map.of("message", "No candidates found"));

        }

    }

}
