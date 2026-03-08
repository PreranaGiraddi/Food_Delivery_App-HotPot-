package com.wipro.hotpot.dto;

public class RestaurantDTO {

    private Long id;
    private String name;
    private String location;
    private String contactNumber;
    private String description;
    private String imageUrl;
    private boolean isActive;
    private Long ownerId;
    private String ownerName;

    
    public Long getId()                { return id; }
    public String getName()            { return name; }
    public String getLocation()        { return location; }
    public String getContactNumber()   { return contactNumber; }
    public String getDescription()     { return description; }
    public String getImageUrl()        { return imageUrl; }
    public boolean isActive()          { return isActive; }
    public Long getOwnerId()           { return ownerId; }
    public String getOwnerName()       { return ownerName; }

    
    public void setId(Long id)                          { this.id = id; }
    public void setName(String name)                    { this.name = name; }
    public void setLocation(String location)            { this.location = location; }
    public void setContactNumber(String c)              { this.contactNumber = c; }
    public void setDescription(String description)      { this.description = description; }
    public void setImageUrl(String imageUrl)            { this.imageUrl = imageUrl; }
    public void setActive(boolean active)               { this.isActive = active; }
    public void setOwnerId(Long ownerId)                { this.ownerId = ownerId; }
    public void setOwnerName(String ownerName)          { this.ownerName = ownerName; }
}