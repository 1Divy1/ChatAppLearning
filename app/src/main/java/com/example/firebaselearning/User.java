package com.example.firebaselearning;

public class User {

    private String userName;
    private String email;
    private String profilePicture;

    public User() {}

    public User(String userName, String email, String profilePicture) {
        this.userName = userName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName() {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail() {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture() {
        this.profilePicture = profilePicture;
    }
}
