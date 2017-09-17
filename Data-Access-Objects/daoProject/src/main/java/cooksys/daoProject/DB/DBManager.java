package cooksys.daoProject.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import cooksys.daoProject.entity.Interest;
import cooksys.daoProject.entity.Location;
import cooksys.daoProject.entity.Person;

public class DBManager {
	private Connection connection;
	
	private LocationDao locationDao;
	private InterestDao interestDao;
	private PersonDao personDao;
	private PersonInterestPairDao personInterestPairDao;
	
	static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "bondstone";
	
	public DBManager(){
		try {
			getConnection();
			populateDaosFromDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private synchronized Connection getConnection() {
		if(connection != null) {
			return connection;
		}
		else {
			try {
				Class.forName("org.postgresql.Driver");
				
				// Gets connection to db server
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
				return connection;
			} catch(SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			return connection;
		}
	}
	
	private void populateDaosFromDB() {
		locationDao = new LocationDao(connection);
		interestDao = new InterestDao(connection);
		personInterestPairDao = new PersonInterestPairDao(connection);
		personDao = new PersonDao(connection, locationDao, personInterestPairDao);
	}
	
	public synchronized void closeConnection() {
		if(connection != null) {
			try {
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				connection = null;
			}
		}
	}
	
	public void runTests() {
		// Tests go here
		Set<Person> allPeople = personDao.getAll();
		
		//List All People
		System.out.println("All People: \n");
		
		if(allPeople != null) {
			allPeople.forEach(person -> System.out.println(person.toString()));
		}
		
		// Increments the age of a person
		Person testPersonAging = personDao.get(new Long(2));
		testPersonAging.setAge(testPersonAging.getAge() + 1);
		
		personDao.save(testPersonAging);
		
		// Add a new person
		// personDao.save(new Person("Michael", "Boren", 28, locationDao.get(new Long(1)), null));
		
		// Test our interest/location query
		System.out.println("Interest/Location Query Test:\nPeople living in Memphis and interested in Drinking: ");
		Set<Person> interestGroupPeople = personDao.findInterestGroup(interestDao.get(new Long(1)), locationDao.get(new Long(1)));
		if(interestGroupPeople != null) {
			interestGroupPeople.forEach(person -> System.out.println(person.toString()));
		}
		
		// Get Location test
		System.out.println("Location Test: " + locationDao.get(new Long(1)).toString());
		
		// Set Location test
		//locationDao.save(new Location("San Diego", "California", "USA"));
		//locationDao.save(new Location(new Long(4), "San Francisco", "California", "USA"));
		

		// Get Interest test
		System.out.println("Interest Test: " + interestDao.get(new Long(1)).toString());
		
		// Set Interest test
		//interestDao.save(new Interest("Exercise"));
		//interestDao.save(new Interest(new Long(6), "Fitness"));
	}
}