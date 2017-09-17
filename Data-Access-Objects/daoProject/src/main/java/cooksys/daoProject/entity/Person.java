package cooksys.daoProject.entity;

import java.util.Set;

public class Person {
	private Long id;
	private String first_name;
	private String last_name;
	private Integer age;
	private Location location;
	private Set<Interest> interests;

	public Person () { }

	// Constructor for new People without an id yet
	public Person(String first_name, String last_name, Integer age, Location location, Set<Interest> interests) {
		this();
		this.first_name = first_name;
		this.last_name = last_name;
		this.age = age;
		this.location = location;
		this.interests = interests;
	}

	// Constructor for existing people with an id
	public Person(Long id, String first_name, String last_name, Integer age, Location location, Set<Interest> interests) {
		this(first_name, last_name, age, location, interests);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<Interest> getInterests() {
		return interests;
	}

	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}

	@Override
	public String toString() {
		StringBuffer interestDisplay = new StringBuffer("");
		
		interests.forEach(interest -> interestDisplay.append(interest.toString() + " "));
		
		return first_name + " " + last_name + "\nID: " + id + " Age: " + age
				+ "\nLocation: " + location.toString() + "\nInterests: " + interestDisplay + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
