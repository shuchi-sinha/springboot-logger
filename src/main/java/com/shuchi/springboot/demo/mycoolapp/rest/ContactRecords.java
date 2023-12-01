package com.shuchi.springboot.demo.mycoolapp.rest;

public class ContactRecords {
    private String id;
    private String firstName;
    private String initial;
    private String lastName;
    @Override
    public String toString() {
        return "ContactRecords [id=" + id + ", firstName=" + firstName + ", initial=" + initial + ", lastName="
                + lastName + "]";
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getInitial() {
        return initial;
    }
    public void setInitial(String initial) {
        this.initial = initial;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
