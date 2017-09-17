package cooksys.daoProject.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cooksys.daoProject.entity.Interest;

public class InterestDao {
	
	private Statement statement;
	
	public InterestDao(Connection connection) {
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Interest get(Long id) {
		// gets the interest with the given id from the db
		try {
			String query = "SELECT * FROM \"Interest\" WHERE \"Interest\".interest_id=" + id;
			ResultSet resultSet = statement.executeQuery(query);
		
			resultSet.next();
			return new Interest(resultSet.getLong(1), resultSet.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean save(Interest interest) {
		// stores based on whether an id exists
		try {
			if(interest.getId() == null) {
				// Insert
				String query = "INSERT INTO \"Interest\" (title) VALUES (\'" + 
						interest.getTitle() + "\');";
				statement.executeUpdate(query);
			} else {
				// Update
				statement.executeUpdate("UPDATE \"Interest\" SET title=\'" + interest.getTitle() + 
						"\' WHERE \"Interest\".interest_id=" + interest.getId());
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
