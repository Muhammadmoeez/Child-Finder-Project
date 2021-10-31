package com.example.childfinder;

import com.google.firebase.auth.FirebaseAuth;

public class ChildModel {

     private String ChildName;
     private String ChildEmail;
     private String ChildPassword;
     private String ChildConfirmPassword;
     private String ChildNumber;
     private String Role;
     private String ParentID;
     private String ChildID;
     private double ChildLongitude;
     private double ChildLatitude;


     public ChildModel() {
     }

     public ChildModel(String childName, String childEmail, String childPassword, String childConfirmPassword, String childNumber, String role, String parentID, String childID, double childLongitude, double childLatitude) {
          ChildName = childName;
          ChildEmail = childEmail;
          ChildPassword = childPassword;
          ChildConfirmPassword = childConfirmPassword;
          ChildNumber = childNumber;
          Role = role;
          ParentID = parentID;
          ChildID = childID;
          ChildLongitude = childLongitude;
          ChildLatitude = childLatitude;
     }

     public String getChildName() {
          return ChildName;
     }

     public void setChildName(String childName) {
          ChildName = childName;
     }

     public String getChildEmail() {
          return ChildEmail;
     }

     public void setChildEmail(String childEmail) {
          ChildEmail = childEmail;
     }

     public String getChildPassword() {
          return ChildPassword;
     }

     public void setChildPassword(String childPassword) {
          ChildPassword = childPassword;
     }

     public String getChildConfirmPassword() {
          return ChildConfirmPassword;
     }

     public void setChildConfirmPassword(String childConfirmPassword) {
          ChildConfirmPassword = childConfirmPassword;
     }

     public String getChildNumber() {
          return ChildNumber;
     }

     public void setChildNumber(String childNumber) {
          ChildNumber = childNumber;
     }

     public String getRole() {
          return Role;
     }

     public void setRole(String role) {
          Role = role;
     }

     public String getParentID() {
          return ParentID;
     }

     public void setParentID(String parentID) {
          ParentID = parentID;
     }

     public String getChildID() {
          return ChildID;
     }

     public void setChildID(String childID) {
          ChildID = childID;
     }

     public double getChildLongitude() {
          return ChildLongitude;
     }

     public void setChildLongitude(double childLongitude) {
          ChildLongitude = childLongitude;
     }

     public double getChildLatitude() {
          return ChildLatitude;
     }

     public void setChildLatitude(double childLatitude) {
          ChildLatitude = childLatitude;
     }
}
