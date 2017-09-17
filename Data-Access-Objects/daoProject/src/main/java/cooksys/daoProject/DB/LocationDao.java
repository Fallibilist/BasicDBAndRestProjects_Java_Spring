package cooksys.daoProject.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cooksys.daoProject.entity.Location;

public class LocationDao {
	private Statement statement;
	
	public LocationDao(Connection connection) {
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Location get(Long id) {
		// gets the location with the given id from the db
		try {
			String query = "SELECT * FROM \"Location\" WHERE \"Location\".location_id=" + id;
			ResultSet resultSet = statement.executeQuery(query);
		
			resultSet.next();
			return new Location(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean save(Location location) {
		// stores based on whether an id exists
		try {
			if(location.getId() == null) {
				// Insert
				String query = "INSERT INTO \"Location\" (city, state, country) VALUES (\'" + 
						location.getCity() + "\', \'" + 
						location.getState() + "\', \'" + 
						location.getCountry() + "\');";
				statement.executeUpdate(query);
			} else {
				// Update
				statement.executeUpdate("UPDATE \"Location\" SET city=\'" + location.getCity() + 
						"\', state=\'" + location.getState() + 
						"\', country=\'" + location.getCountry() +
						"\' WHERE \"Location\".location_id=" + location.getId());
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
