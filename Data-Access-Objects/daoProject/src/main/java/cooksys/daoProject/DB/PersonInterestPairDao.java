package cooksys.daoProject.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import cooksys.daoProject.entity.Interest;

public class PersonInterestPairDao {
	private Statement statement;
	
	public PersonInterestPairDao(Connection connection) {
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkIfPairExists(Long personId, Long interestId) {
		try {
			String query = "SELECT * FROM \"PersonInterestPairs\" WHERE person_id=" + 
						personId + " AND interest_id=" + interestId;
			ResultSet resultSet = statement.executeQuery(query);
		
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean addInterestToPerson(Long personId, Long interestId) {
		// Adds an interest/person pair entry
		try {
			// Insert
			if(!checkIfPairExists(personId, interestId)) {
				String query = "INSERT INTO \"PersonInterestPairs\" (person_id, interest_id) VALUES (" + 
						personId + ", " + 
						interestId + ");";
				statement.executeUpdate(query);		
				return true;		
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Set<Interest> getPersonsInterests(Long id) {
		// gets interests from the PersonInterestPairs table
		try {
			Set<Interest> interests = new HashSet<Interest>();
			
			String query = "SELECT interest.interest_id, interest.title "
					+ "FROM \"PersonInterestPairs\" AS pip JOIN \"Interest\" AS interest "
					+ "ON pip.interest_id=interest.interest_id WHERE person_id=" + id;
			ResultSet resultSet = statement.executeQuery(query);
		
			while(resultSet.next()) {
				interests.add(new Interest(resultSet.getLong(1), resultSet.getString(2)));
			}
			return interests;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setPersonsInterests(Long personId, Set<Interest> interests) {
		// sets interests of the person in the PersonInterestPairs table
		interests.forEach(interest -> {
			addInterestToPerson(personId, interest.getId());
		});
	}
}
