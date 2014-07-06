import com.novus.persistence.annotations.Column;
import com.novus.persistence.annotations.Id;
import com.novus.persistence.annotations.Table;


@Table(name = "humans")
public class Human {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String AGE = "age";
	public static final String HOMETOWN = "hometown";

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = NAME)
	private String name;

	@Column(name = AGE)
	private Integer age;

	@Column(name = HOMETOWN)
	private String hometown;

	public Human() {}

	public Human(String name, int age, String hometown) {
		this.name = name;
		this.age = age;
		this.hometown = hometown;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getHometown() {
		return hometown;
	}
	
	public String toString() {
		return String.format("[%s, %s, %s, %s]", id, name, age, hometown);
	}
}
