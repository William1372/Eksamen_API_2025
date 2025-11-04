package dat.config;

import dat.entities.Candidate;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;

public class Populate {

    public static void main(String[] args){

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (EntityManager em = emf.createEntityManager()){

            em.getTransaction().begin();

            // Skills
            Skill java = new Skill("Java", SkillCategory.PROG_LANG, "Backend programming language", "java", null,null);
            Skill python = new Skill("Python", SkillCategory.PROG_LANG, "Data analysis and ML language", "python",null,null);
            Skill postgresql = new Skill("PostgreSQL", SkillCategory.DB, "Relational SQL database", "postgresql",null,null);
            Skill docker = new Skill("Docker", SkillCategory.DEVOPS, "Containerization tool", "docker",null,null);
            Skill react = new Skill("React", SkillCategory.FRONTEND, "Frontend JS library", "react",null,null);
            Skill junit = new Skill("JUnit", SkillCategory.TESTING, "Java unit testing framework", "junit",null,null);

            em.persist(java);
            em.persist(python);
            em.persist(postgresql);
            em.persist(docker);
            em.persist(react);
            em.persist(junit);

            // Candidates
            Candidate alice = new Candidate("William Øster Danø", "31520223", "Computer Science", new HashSet<>());
            Candidate bob = new Candidate("Benedicte V.", "42451215", "Software Engineering", new HashSet<>());
            Candidate clara = new Candidate("Jonathan Øster Danø", "31701416", "Information Technology", new HashSet<>());

            // Relations
            alice.addSkill(java);
            alice.addSkill(junit);

            bob.addSkill(python);
            bob.addSkill(postgresql);
            bob.addSkill(docker);

            clara.addSkill(react);
            clara.addSkill(java);
            clara.addSkill(postgresql);

            // Persist
            em.persist(alice);
            em.persist(bob);
            em.persist(clara);

            em.getTransaction().commit();
            System.out.println("Database populated successfully!");

        }catch (Exception e){

            e.printStackTrace();

        }

    }

}
