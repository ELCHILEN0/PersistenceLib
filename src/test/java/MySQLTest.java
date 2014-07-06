import static com.novus.persistence.queries.expression.Expressions.equal;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import com.novus.persistence.databases.Database;
import com.novus.persistence.databases.mysql.MySQLDatabase;

public class MySQLTest {

	@Test
	public void test() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl("jdbc:mysql://localhost/test_db");
		source.setUsername("root");
		source.setPassword("root");

		assertTrue(source.isAccessToUnderlyingConnectionAllowed());
		try (Connection con = source.getConnection()) {
			assertNotNull(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Database database = null;
		try {
			database = new MySQLDatabase(source);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		try (Connection con = database.getConnection()) {
			assertNotNull(con);

			database.createStructure(con, Human.class);
			Human h = database.select(Human.class).where(equal(Human.NAME, "Jnani Weibel")).findOne(con);

			if (h == null) {
				h = new Human("Jnani Weibel", 17, "Trinidad");
			}

			h.setAge(h.getAge() + 1);

			database.save(con, h);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
