package br.com.application.model;

import jakarta.persistence.*;


import java.util.Objects;

@Entity
@Table(name = "personal")
public class Personal {

    private static final long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "first_name", nullable = false, length = 80)
private String firstName;

@Column(name = "last_Name", nullable = false, length = 80)
private String lastName;

@Column(nullable = false, length = 100)
private String address;

@Column(nullable = false, length = 6)
private String gender;

    public Personal() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Personal personal = (Personal) o;
        return Objects.equals(getId(), personal.getId()) && Objects.equals(getFirstName(), personal.getFirstName()) && Objects.equals(getLastName(), personal.getLastName()) && Objects.equals(getAddress(), personal.getAddress()) && Objects.equals(getGender(), personal.getGender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getAddress(), getGender());
    }
}
