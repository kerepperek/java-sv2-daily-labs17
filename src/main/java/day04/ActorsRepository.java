package day04;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name){
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (
                SQLException se) {
            throw new IllegalArgumentException("Connection Error",se);
        }

        try{

            PreparedStatement stmt=conn.prepareStatement("insert into actors(actor_name) values(?)");
            stmt.setString(1,name);
            stmt.executeUpdate();

        } catch (
                SQLException se) {
            throw new IllegalArgumentException("Cannot insert",se);
        }


    }

    public List<String> listActorsNames(){
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select actor_name from actors order by actor_name")
        ) {
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("actor_name");
                names.add(name);
            }
            return names;
        } catch (SQLException sqle) {
            throw new IllegalArgumentException("Cannot select actors", sqle);
        }
    }

    public String findActorNameById(Long id){
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select actor_name from actors where id = ?");
        ) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery();) {
               return  selectNameByPreparedStatement(ps);
            } catch (SQLException sqle) {
                throw new IllegalArgumentException("Cannot query", sqle);
            }
        } catch (SQLException sqle) {
            throw new IllegalArgumentException("Cannot query", sqle);
        }
    }

    private String selectNameByPreparedStatement(PreparedStatement ps){
        try (
                ResultSet rs = ps.executeQuery();
        ) {
            if (rs.next()) {
                String name = rs.getString("actor_name");
                return name;
            }
            throw new IllegalArgumentException("No result");
        } catch (SQLException sqle) {
            throw new IllegalArgumentException("Cannot query", sqle);
        }
    }
}
