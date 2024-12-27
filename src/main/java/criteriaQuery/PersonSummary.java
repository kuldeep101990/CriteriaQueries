package criteriaQuery;

import java.util.Date;

public class PersonSummary {

    private String name;
    private Date dob;

    public PersonSummary() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "PersonSummary{name='" + name + "', dob=" + dob + "}";
    }
}
