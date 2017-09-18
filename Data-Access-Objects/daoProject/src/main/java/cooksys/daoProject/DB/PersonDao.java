package cooksys.daoProject.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import cooksys.daoProject.entity.Interest;
import cooksys.daoProject.entity.Location;
import cooksys.daoProject.entity.Person;

public class PersonDao {
	
	private Connection connection;
	private Statement statement;
	private LocationDao locationDao;
	private InterestDao interestDao;
	private PersonInterestPairDao personInterestPairDao;
	
	public PersonDao(Connection connection, LocationDao locationDao, InterestDao interestDao, PersonInterestPairDao personInterestPairDao) {
		this.connection = connection;
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.locationDao = locationDao;
		this.interestDao = interestDao;
		this.personInterestPairDao = personInterestPairDao;
	}

	public Set<Person> getAll() {
		try {
			Set<Person> allPeople = new HashSet<Person>();
			
			String query = "SELECT * FROM \"Person\"";
			ResultSet resultSet = statement.executeQuery(query);
		
			while(resultSet.next()) {
				allPeople.add(new Person(resultSet.getLong(1), 
						resultSet.getString(2), 
						resultSet.getString(3), 
						resultSet.getInt(4), 
						locationDao.get(resultSet.getLong(5)), 
						personInterestPairDao.getPersonsInterests(resultSet.getLong(1))));
			}
			return allPeople;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Person get(Long id) {
		// gets the person with the given id from the db
		try {
			String query = "SELECT * FROM \"Person\" WHERE \"Person\".person_id=" + id;
			ResultSet resultSet = statement.executeQuery(query);
		
			resultSet.next();
			return new Person(resultSet.getLong(1), 
							resultSet.getString(2), 
							resultSet.getString(3), 
							resultSet.getInt(4), 
							locationDao.get(resultSet.getLong(5)), 
							personInterestPairDao.getPersonsInterests(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean save(Person person) {
		// stores based on whether an id exists
		try {
			if(person.getLocation().getId() == null) {
				locationDao.save(person.getLocation());
			} else {
				if(locationDao.get(person.getLocation().getId()) == null) {
					person.getLocation().setId(null);
					locationDao.save(person.getLocation());
					return false;
				}
			}
			
			person.getInterests().forEach(interest -> {
				if(interest.getId() == null) {
					interestDao.save(interest);
				} else {
					if(interestDao.get(interest.getId()) == null) {
						interest.setId(null);
						interestDao.save(interest);
					}
				}
			});
			
			if(person.getId() == null) {
				// Insert
				String query = "INSERT INTO \"Person\" (first_name, last_name, age, location_id) VALUES (\'" + 
						person.getFirst_name() + "\', \'" + 
						person.getLast_name() + "\', " + 
						person.getAge() + ", " + 
						person.getLocation().getId() + ");";
				PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.executeUpdate();
				
				Long personId = null;
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		                personId = generatedKeys.getLong(1);
		            }
		        }
				if(personId != null) {
					personInterestPairDao.setPersonsInterests(personId, person.getInterests());
					return true;
				}
			} else {
				// Update
				statement.executeUpdate("UPDATE \"Person\" SET first_name=\'" + person.getFirst_name() + 
						"\', last_name=\'" + person.getLast_name() + 
						"\', age=" + person.getAge() + 
						", location_id=" + person.getLocation().getId() + 
						" WHERE \"Person\".person_id=" + person.getId());
				personInterestPairDao.setPersonsInterests(person.getId(), person.getInterests());
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Set<Person> findInterestGroup(Interest interest, Location location) {
		// Executes our query: 
		try {
			Set<Person> interestGroup = new HashSet<Person>();
			
			String query = "SELECT \"Person\".person_id, \"Person\".first_name, \"Person\".last_name, \"Person\".age, \"Location\".location_id, \"Interest\".title " +
				"FROM (((\"Person\" JOIN \"Location\" ON \"Person\".location_id = \"Location\".location_id) " +
			    	"JOIN \"PersonInterestPairs\" ON \"PersonInterestPairs\".person_id = \"Person\".person_id)" +
			    	"JOIN \"Interest\" ON \"Interest\".interest_id = \"PersonInterestPairs\".interest_id)" +
					"WHERE \"Interest\".title = \'" + interest.getTitle() + "\' AND \"Location\".city = \'" + location.getCity() + "\'";
			ResultSet resultSet = statement.executeQuery(query);
		
			while(resultSet.next()) {
				interestGroup.add(new Person(resultSet.getLong(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getInt(4),
						locationDao.get(resultSet.getLong(5)),
						personInterestPairDao.getPersonsInterests(resultSet.getLong(1))));
			}
			return interestGroup;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
//		// Specific Query
//		SELECT "Person".person_id, "Person".first_name, "Person".last_name, "Person".age, "Location".city, "Interest".title 
//			FROM ((("Person" JOIN "Location" ON "Person".location_id = "Location".location_id) 
//		    	JOIN "PersonInterestPairs" ON "PersonInterestPairs".person_id = "Person".person_id)
//		    	JOIN "Interest" ON "Interest".interest_id = "PersonInterestPairs".interest_id)
//				WHERE "Interest".title = 'Drinking' AND "Location".city = 'Fallbrook'
//				
//		// Generalized Query
//		SELECT DISTINCT "Person".person_id, first_name, last_name, age FROM 
//			(SELECT person_id FROM
//		    (SELECT interest_id FROM 
//		    "PersonInterestPairs" GROUP BY interest_id HAVING COUNT(interest_id) > 1) AS dupInterests
//		        JOIN "PersonInterestPairs" ON dupInterests.interest_id = "PersonInterestPairs".interest_id) AS resultSetOfMatched
//		        JOIN "Person" ON resultSetOfMatched.person_id = "Person".person_id
//		        JOIN "Location" ON "Person".location_id = "Location".location_id WHERE "Location".city = 'Fallbrook'
	}
}
