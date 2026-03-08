package com.wipro.hotpot.dto;

public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String address;
    private String gender;
    private String role;   

    public RegisterRequest() {}

    public String getName()            { return name; }
    public String getEmail()           { return email; }
    public String getPassword()        { return password; }
    public String getContactNumber()   { return contactNumber; }
    public String getAddress()         { return address; }
    public String getGender()          { return gender; }
    public String getRole()            { return role; }

    public void setName(String name)                   { this.name = name; }
    public void setEmail(String email)                 { this.email = email; }
    public void setPassword(String password)           { this.password = password; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setAddress(String address)             { this.address = address; }
    public void setGender(String gender)               { this.gender = gender; }
    public void setRole(String role)                   { this.role = role; }
}