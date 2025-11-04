package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.CandidateDTO;
import dat.entities.Candidate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CandidateDAO implements IDAO<CandidateDTO, Integer> {

    private static CandidateDAO instance;
    private static EntityManagerFactory emf;

    public static CandidateDAO getInstance(EntityManagerFactory _emf){

        if (instance == null){
            emf = _emf;
            instance = new CandidateDAO();
        }
        return instance;
    }

    @Override
    public CandidateDTO read(Integer integer){

        try(EntityManager em = emf.createEntityManager()){

            Candidate candidate = em.find(Candidate.class, integer);
            return candidate != null ? new CandidateDTO(candidate) : null;

        }

    }

    @Override
    public List<CandidateDTO> readAll(){

        try(EntityManager em = emf.createEntityManager()){

            TypedQuery<CandidateDTO> query = em.createQuery("SELECT new dat.dtos.CandidateDTO(c) FROM Candidate c", CandidateDTO.class);
            return query.getResultList();

        }

    }

    @Override
    public CandidateDTO create(CandidateDTO candidateDTO){

        try(EntityManager em = emf.createEntityManager()){

            em.getTransaction().begin();
            Candidate candidate = new Candidate(candidateDTO);
            em.persist(candidate);
            em.getTransaction().commit();
            return new CandidateDTO(candidate);

        }

    }

    @Override
    public CandidateDTO update(Integer integer, CandidateDTO candidateDTO){

        try(EntityManager em = emf.createEntityManager()){

            em.getTransaction().begin();
            Candidate c = em.find(Candidate.class, integer);
            c.setCandidateName(candidateDTO.getCandidateName());
            c.setCandidatePhoneNumber(candidateDTO.getCandidatePhoneNumber());
            c.setCandidateEducation(candidateDTO.getCandidateEducation());
            Candidate mergedCandidate = em.merge(c);
            return new CandidateDTO(mergedCandidate);

        }

    }

    @Override
    public void delete(Integer integer){

        try(EntityManager em = emf.createEntityManager()){

            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, integer);
            if (candidate != null){

                em.remove(candidate);

            }
            em.getTransaction().commit();
        }

    }

    @Override
    public boolean validatePrimaryKey(Integer integer){

        try (EntityManager em = emf.createEntityManager()){

            Candidate candidate = em.find(Candidate.class, integer);
            return candidate != null;

        }

    }

}
