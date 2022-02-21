package day04;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.List;

public class ActorsMain {
    public static void main(String[] args) {
        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("actors");
            dataSource.setPassword("actors");

            Flyway flyway=Flyway.configure().dataSource(dataSource).load();
            flyway.migrate();

            ActorsRepository employeesDao = new ActorsRepository(dataSource);
            employeesDao.saveActor("John Doe");

            List<String> names=employeesDao.listActorsNames();
            System.out.println(names);

            String name= employeesDao.findActorNameById(4L);
            System.out.println(name);
        } catch (SQLException se) {
            throw new IllegalArgumentException("Connection Error", se);
        }
    }
}
