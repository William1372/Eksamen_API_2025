package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.security.daos.ISecurityDAO;
import dat.security.daos.impl.SecurityDAO;
import jakarta.persistence.EntityManagerFactory;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        ISecurityDAO securityDAO = new SecurityDAO(emf);

        Populate.main(null);

        ApplicationConfig.startServer(7070);

    }

}