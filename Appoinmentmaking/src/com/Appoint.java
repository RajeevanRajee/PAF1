package com;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Appoint {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/appointment", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public String getPatientDetails() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error dtabase don't have any data.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\"1\"><tr><th>Appointment ID</th><th>Doctor ID</th><th>DoctorName</th><th>PatientID</th><th>PatientName</th><th>HospitalName</th><th>Date</th></tr>";
			String query = "select * from patientdetails";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String PID = rs.getString("PID");
				String appointmentid = rs.getString("appointmentid");
				String doctorid = rs.getString("doctorid");
				String doctorName = rs.getString("doctorName");
				String patientid = rs.getString("patientid");
				String patientName = rs.getString("patientName");
				String hospitalName = rs.getString("hospitalName");
				String date = rs.getString("date");

				// Add into the html table

				output += "<tr><td><input id='hidItemIDUpdate' name='hidItemIDUpdate' type='hidden' value='" + PID + "'>" + appointmentid + "</td>";
			
				output += "<td>" + doctorid + "</td>";
				output += "<td>" + doctorName + "</td>";
				output += "<td>" + patientid + "</td>";
				output += "<td>" + patientName + "</td>";
				output += "<td>" + hospitalName + "</td>";
				output += "<td>" + date + "</td>";

				// buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td><td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-pid='"+PID+"'></td></tr>";
				
			}
			con.close();
			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the Patient.";
			System.err.println(e.getMessage());
		}
		return output;
	}
	

	public String addPatientDetails(String appointmentid, String doctorid, String doctorName, String patientid, String patientName,
			String hospitalName, String date) {
		
		String output = "";
		try{
			Connection con = connect();
			
			if (con == null){
					return "Error while connecting to the database for inserting."; 
				}
	
			// create a prepared statement
			String query = " insert into patientdetails (`appointmentid`,`doctorid`,`doctorName`,`patientid`,`patientName`,`hospitalName`,`date`)" + " values (?,?,?, ?, ?, ?,?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			
			preparedStmt.setString(1, appointmentid);
			preparedStmt.setString(2, doctorid);
			preparedStmt.setString(3, doctorName);
			preparedStmt.setString(4, patientid);
			preparedStmt.setString(5, patientName);
			preparedStmt.setString(6, hospitalName);
			preparedStmt.setString(7, date);
	
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newItems = getPatientDetails();
			output = "{\"status\":\"success\", \"data\": \"" +newItems + "\"}";
			
			
		}catch (Exception e){
			output = "{\"status\":\"error\", \"data\":\"Error while inserting thepatient details.\"}";
					System.err.println(e.getMessage());
			}
		return output;
	}

	
	
public String updatepatient(String PID,String appointmentid, String doctorid, String doctorName, String patientid, String patientName,
		String hospitalName, String date){
		
		String output = "";
		try{
			Connection con =connect();
			if (con == null){
				return "Error while connecting to the database for updating.";
			}
		// create a prepared statement
		String query = "UPDATE patientdetails SET appointmentid=?, doctorid=?, doctorName=?, patientid=?, patientName=?, hospitalName=?,date=? WHERE PID=?";
		PreparedStatement preparedStmt = con.prepareStatement(query);
		// binding values
		
		preparedStmt.setString(1, appointmentid);
		preparedStmt.setString(2, doctorid);
		preparedStmt.setString(3, doctorName);
		preparedStmt.setString(4, patientid);
		preparedStmt.setString(5, patientName);
		preparedStmt.setString(6, hospitalName);
		preparedStmt.setString(7, date);

		
		preparedStmt.setInt(8, Integer.parseInt(PID));
		// execute the statement
		preparedStmt.execute();
		con.close();
		String newItems = getPatientDetails();
		output = "{\"status\":\"success\", \"data\": \"" +newItems + "\"}";
	}catch (Exception e){
		output = "{\"status\":\"error\", \"data\":\"Error while updating the payment.\"}";
				System.err.println(e.getMessage());

	
	}
		return output;

	}

	public String deletepatient(String PID) {
		String output = "";
		try {
				Connection con = connect();
				if (con == null) {
					return "Error while connecting to the database for deleting.";
				}
			// create a prepared statement
			String query = "delete from patientdetails where PID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(PID));
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newItems = getPatientDetails();
			output = "{\"status\":\"success\", \"data\": \"" +newItems + "\"}";
			
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while deleting the payment.\"}";
					System.err.println(e.getMessage());
		}
		return output;
	}

}
