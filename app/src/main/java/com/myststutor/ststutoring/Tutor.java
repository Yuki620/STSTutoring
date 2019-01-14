package com.myststutor.ststutoring;

public class Tutor {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    private String name;
    private String school;
    private int grade;
    private String ageRange;
    private String availability;
    private String location;
    private String intro;
    private String contact;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private float price;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String email;
    private String uid;

    @Override
    public String toString() {
        return "Tutor{" +
                "name='" + name + '\'' +
                ", school='" + school + '\'' +
                ", grade=" + grade +
                ", ageRange='" + ageRange + '\'' +
                ", availability='" + availability + '\'' +
                ", location='" + location + '\'' +
                ", intro='" + intro + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
