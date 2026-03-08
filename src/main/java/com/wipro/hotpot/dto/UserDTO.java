package com.wipro.hotpot.dto;

import com.wipro.hotpot.entity.User;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String gender;
    private String role;
    private boolean active;

    public UserDTO() {}

    // Static factory — converts User entity → UserDTO (hides password)
    public static UserDTO from(User u) {
        UserDTO dto = new UserDTO();
        dto.id            = u.getId();
        dto.name          = u.getName();
        dto.email         = u.getEmail();
        dto.contactNumber = u.getContactNumber();
        dto.address       = u.getAddress();
        dto.gender        = u.getGender();
        dto.role          = "ROLE_" + u.getRole().name();
        dto.active        = u.isActive();
        return dto;
    }

    public Long getId()            { return id; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getContactNumber(){ return contactNumber; }
    public String getAddress()     { return address; }
    public String getGender()      { return gender; }
    public String getRole()        { return role; }
    public boolean isActive()      { return active; }

    public void setId(Long id)                       { this.id = id; }
    public void setName(String name)                 { this.name = name; }
    public void setEmail(String email)               { this.email = email; }
    public void setContactNumber(String c)           { this.contactNumber = c; }
    public void setAddress(String address)           { this.address = address; }
    public void setGender(String gender)             { this.gender = gender; }
    public void setRole(String role)                 { this.role = role; }
    public void setActive(boolean active)            { this.active = active; }
}