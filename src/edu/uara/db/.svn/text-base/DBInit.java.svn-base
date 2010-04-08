/*
* This file is part of the Factbook Generator.
* 
* The Factbook Generator is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* The Factbook Generator is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang
*/
package edu.uara.db;

/**
 * This class exposes methods to the scripting engine that allow for scripted
 * initialization of the database, should it be necessary.
 * @author jeff
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import edu.uara.db.profiles.ProfileManager;
import edu.uara.db.structure.TableData;

public class DBInit {    
    /**
     * This method uses the supplied table data to attempt to create a table on the database.
     * It uses the profile to check for the table exists error code and is silent if this happens;
     * since we use this method to "verify the table structure."
     * @param data - The table data.
     * @return True on success, false otherwise.
     */
    public static boolean createTable(TableData data) throws SQLException {
        try {
            Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(data.getStatement());
            conn.commit();
        }
        //basically we only throw an error if it's not a table exists error.
        catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode != ProfileManager.CURRENT_PROFILE.getErrorCode("TABLE_EXISTS"))
                throw new SQLException(e);
        }
        catch (ClassNotFoundException e) {
            System.err.println("***ERROR: Connection class was not found.");
        }
        catch (NullPointerException e) {
            System.err.println("***ERROR: Null exception in the init script. Likely a profile is missing.");
        }

        return true;
    }
    
    public static void init() throws SQLException {
        //create Files metadata table
        TableData td = new TableData();
        td.setName("Files");
        td.addLongStringColumn("File_Name");
        td.addStringColumn("File_Year");
        td.addIntColumn("File_ID");
        td.addLongStringColumn("File_Status");
        DBInit.createTable(td);
        
        //create AIS table
        td = new TableData();
        td.setName("AIS");
        td.addIntColumn("File_ID");
        td.addIntColumn("Year");
        td.addIntColumn("SSN");
        td.addIntColumn("Gender");
        td.addIntColumn("Race");
        td.addIntColumn("Geo_Origin");
        td.addIntColumn("Acceptance");
        td.addIntColumn("Enrollment");
        td.addIntColumn("Student_Level");
        td.addIntColumn("Major_Code");
        td.addIntColumn("Transfer_FICE");
        td.addIntColumn("Institution_FICE");
        td.addIntColumn("Residence_App");
        td.addIntColumn("Residence_Enrollment");
        DBInit.createTable(td);

        //create DIS table
        td = new TableData();
        td.setName("DIS");
        td.addIntColumn("File_ID");
        td.addIntColumn("Year");
        td.addLongStringColumn("Student_ID");
        td.addIntColumn("Institution_ID");
        td.addLongStringColumn("Name");
        td.addDateColumn("Birthday");
        td.addStringColumn("Residency");
        td.addStringColumn("Institutional_Admit_Type");
        td.addStringColumn("Original_Admit_Type");
        td.addIntColumn("Original_Admit_Year");
        td.addIntColumn("Oringinal_Admit_Term");
        td.addStringColumn("Readmit_Type");
        td.addIntColumn("Readmit_Year");
        td.addIntColumn("Readmit_Term");
        //Privious School
        td.addStringColumn("Institution_Type");
        td.addStringColumn("School_Code");
        td.addIntColumn("Grade_Scheme");
        td.addFloatColumn("GPA");
        td.addDateColumn("End_Date");
        //Test Score
        td.addIntColumn("Highest_Sat_Verbal");
        td.addIntColumn("Highest_Sat_Math");
        td.addIntColumn("Highest_GREV");
        td.addIntColumn("Highest_GREQ");
        td.addIntColumn("Highest_GREA");
        //Cummulative Data
        td.addIntColumn("Cummulative_Term");
        td.addFloatColumn("Cummulative_Earn_Hours");
        td.addFloatColumn("Cummulative_GPA");
        td.addFloatColumn("Total_Hours_Transfered");
        //MHEC_DIS_Data
        td.addIntColumn("MHEC_Month_Awarded");
        td.addIntColumn("MHEC_Year_Awarded");
        //td.addIntColumn("MHEC_Student_ID");
        td.addIntColumn("MHEC_Gender");
        td.addIntColumn("MHEC_Racial_Category");
        //MHEC_Degrees_Awarded
        td.addIntColumn("MHEC_Degree_1");
        td.addIntColumn("MHEC_Program_Code_1");
        td.addIntColumn("MHEC_Degree_2");
        td.addIntColumn("MHEC_Program_Code_2");
        td.addIntColumn("MHEC_Degree_3");
        td.addIntColumn("MHEC_Program_Code_3");
        td.addIntColumn("MHEC_Degree_4");
        td.addIntColumn("MHEC_Program_Code_4");
        td.addIntColumn("MHEC_Degree_5");
        td.addIntColumn("MHEC_Program_Code_5");
        //Degree_Data
        // Degree One
        td.addStringColumn("Degree_1_Code_Awarded");
        td.addStringColumn("Degree_1_Major1");
        td.addStringColumn("Degree_1_Major2");
        td.addStringColumn("Degree_1_Minor");
        td.addStringColumn("Degree_1_Certificate");
        td.addStringColumn("Degree_1_College_Code");
        td.addDateColumn("Degree_1_Date");
        td.addFloatColumn("Degree_1_GPA");
        // Degree Two
        td.addStringColumn("Degree_2_Code_Awarded");
        td.addStringColumn("Degree_2_Major1");
        td.addStringColumn("Degree_2_Major2");
        td.addStringColumn("Degree_2_Minor");
        td.addStringColumn("Degree_2_Certificate");
        td.addStringColumn("Degree_2_College_Code");
        td.addDateColumn("Degree_2_Date");
        td.addFloatColumn("Degree_2_GPA");
        // Degree Three
        td.addStringColumn("Degree_3_Code_Awarded");
        td.addStringColumn("Degree_3_Major1");
        td.addStringColumn("Degree_3_Major2");
        td.addStringColumn("Degree_3_Minor");
        td.addStringColumn("Degree_3_Certificate");
        td.addStringColumn("Degree_3_College_Code");
        td.addDateColumn("Degree_3_Date");
        td.addFloatColumn("Degree_3_GPA");
        //
        td.addStringColumn("Primary_Concentration");
        td.addStringColumn("Primary_Track");
        td.addStringColumn("Academic_Catalog");
        td.addIntColumn("Country_Of_Origin");
        DBInit.createTable(td);

        //create EDS table
        TableData eds = new TableData();
        eds.setName("EDS");
        eds.addIntColumn("File_ID");
        eds.addIntColumn("Year");
        eds.addIntColumn("FICE_Code");
        eds.addStringColumn("Campus_Code");
        eds.addLongStringColumn("Employee_ID");
        eds.addIntColumn("Gender");
        eds.addIntColumn("Race");
        eds.addIntColumn("Birth_Year");
        eds.addIntColumn("Employment_Status");
        eds.addDateColumn("Init_Hire_Date");
        eds.addDateColumn("Position_Hire_Date");
        eds.addIntColumn("Occupation_Code");
        eds.addIntColumn("Academic_Rank");
        eds.addIntColumn("Tenure_Status");
        eds.addIntColumn("Prog_Assignment");
        eds.addIntColumn("Faculty_Prog_Assignment");
        eds.addIntColumn("Contract");
        eds.addIntColumn("Salary");
        eds.addIntColumn("Highest_Degree");
        eds.addIntColumn("Promotion");
        eds.addIntColumn("Appointment_Status");
        eds.addIntColumn("Citizenship");
        DBInit.createTable(eds);

        //create EIS table
        TableData eis = new TableData();
        eis.setName("EIS");
        eis.addIntColumn("File_ID");
        eis.addIntColumn("Session_Code");
        eis.addIntColumn("Year");
        eis.addIntColumn("FICE_Code");
        eis.addStringColumn("Campus_Code");
        eis.addStringColumn("Student_Number");
        eis.addIntColumn("Gender");
        eis.addIntColumn("Race");
        eis.addIntColumn("Birth_Year");
        eis.addIntColumn("Geo_Origin");
        eis.addIntColumn("Tuition_Status");
        eis.addIntColumn("Program_Code");
        eis.addIntColumn("Degree_Sought");
        eis.addIntColumn("First_Time_Status");
        eis.addIntColumn("Credit_Hours");
        eis.addIntColumn("Attendance_Category");
        eis.addIntColumn("Student_Level");
        eis.addStringColumn("Career");
        eis.addIntColumn("SAT_Verbal");
        eis.addIntColumn("SAT_Math");
        eis.addIntColumn("RC_Activity");
        eis.addIntColumn("RC_Institution");
        DBInit.createTable(eis);

        //create POP table
        td = new TableData();
        td.setName("POP");
        td.addIntColumn("File_ID");
        td.addLongStringColumn("Student_ID");
        td.addIntColumn("Institution_ID");
        td.addIntColumn("Year");
        td.addIntColumn("Term");
        td.addDateColumn("Date_Prepared");
        td.addDateColumn("Birthdate");
        td.addIntColumn("Gender");
        td.addStringColumn("Marital_Status");
        td.addIntColumn("Ethnic_Origin");
        td.addStringColumn("Native_Tongue");
        td.addStringColumn("Religion");
        td.addStringColumn("Citizenship");
        td.addIntColumn("Geo_Origin");
        td.addIntColumn("Residency_Term");
        td.addStringColumn("Handicap_Type");
        td.addStringColumn("Visa_Type");
        td.addStringColumn("Foreign_Student");
        td.addStringColumn("Co_op_Student");
        td.addStringColumn("Veteran_Code");
        td.addIntColumn("Veteran_Benefit");
        td.addStringColumn("GI_Certificate_Type_Term");
        td.addStringColumn("NCAA_Athletic_Code");
        td.addIntColumn("Zip_Permanent");
        // MHEC Data
        td.addIntColumn("Program_Code");
        td.addIntColumn("Degree_Sought");
        td.addIntColumn("First_Time_Status");
        td.addFloatColumn("Credit_Hour_Load");
        td.addIntColumn("Current_Status");
        td.addIntColumn("Student_Level");
        td.addIntColumn("Original_Enrolment_Year");
        td.addIntColumn("Original_Enrolment_Term");
        td.addStringColumn("Original_Enrolment_Type");
        td.addIntColumn("Last_Readmit_Year");
        td.addIntColumn("Last_Readmit_Term");
        td.addStringColumn("Last_Readmit_Type");
        //last School Attended  
        td.addStringColumn("Last_Institution_Admit_Type");
        td.addStringColumn("Last_Institution_Type");
        td.addStringColumn("Last_School_Code");
        td.addIntColumn("Last_Grade_Scheme");
        td.addFloatColumn("Last_GPA");
        td.addStringColumn("Last_Degree_Sought");
        td.addStringColumn("Last_Degree_Received");
        td.addDateColumn("Last_End_Date");
        //Test Score
        td.addIntColumn("Highest_Sat_Verbal");
        td.addIntColumn("Highest_Sat_Math");
        td.addIntColumn("Highest_Sat_Reading");
        td.addIntColumn("Highest_Sat_Vocabulary");
        td.addIntColumn("Highest_TSWE");
        td.addIntColumn("Highest_GREV");
        td.addIntColumn("Highest_GREQ");
        td.addIntColumn("Highest_GREA");
        td.addIntColumn("Highest_LSAT");
        td.addIntColumn("Highest_GMAT");
        td.addIntColumn("Highest_TOEFL");
        td.addIntColumn("Highest_NTEC");
        td.addIntColumn("Highest_NTEG");
        td.addIntColumn("Highest_NTEP");
        td.addIntColumn("Highest_NJPRE_Reading");
        td.addIntColumn("Highest_NJPRE_Sentences");
        td.addIntColumn("Highest_NJPRE_Essay");
        td.addIntColumn("Highest_NJPRE_Comp");
        td.addIntColumn("Highest_NJPRE_Algebra");
        td.addIntColumn("Highest_NJPST_Reading");
        td.addIntColumn("Highest_NJPST_Sentences");
        td.addIntColumn("Highest_NJPST_Essay");
        td.addIntColumn("Highest_NJPST_Comp");
        td.addIntColumn("Highest_NJPST_Algebra");
        //td.addIntColumn("MHEC_Student_ID");
        td.addStringColumn("Remedial");
        // previous term cummulative data
        td.addIntColumn("Previous_Year");
        td.addIntColumn("Previous_Term");
        td.addFloatColumn("Previous_Cummulative_Attempted_Hours");
        td.addFloatColumn("Previous_Cummulative_Earned_Hours");
        td.addFloatColumn("Previous_Cummulative_Earned_Quality_Hours");
        td.addFloatColumn("Previous_Cummulative_Earned_Quality_Points");
        td.addFloatColumn("Previous_Cummulative_GPA");
        //Current Term Data
        td.addStringColumn("Current_Term_Full_Part");
        td.addStringColumn("Current_Term_Career");
        td.addStringColumn("Current_Term_College");
        td.addStringColumn("Current_Term_Classification");
        td.addStringColumn("Current_Term_Degree_Primary");
        td.addStringColumn("Current_Term_Major1_Primary");
        td.addStringColumn("Current_Term_Major2_Primary");
        td.addStringColumn("Current_Term_Primary_Concentration");
        td.addStringColumn("Current_Term_Secondary_Concentration");
        td.addStringColumn("Current_Term_Minor_Primary");
        td.addStringColumn("Current_Term_Certificate");
        td.addStringColumn("Current_Term_Special_Program");
        td.addStringColumn("Current_Term_Registration_Type");
        td.addStringColumn("Current_Term_Room_Type");
        td.addStringColumn("Current_Term_Dorm_Code");
        //Current Term Post Data
        td.addFloatColumn("Current_Term_Attempted_Hours");
        td.addFloatColumn("Current_Term_Earned_Hours");
        td.addFloatColumn("Current_Term_Earned_Quality");
        td.addFloatColumn("Current_Term_Quality_Points");
        td.addFloatColumn("Current_Term_GPA");
        td.addStringColumn("Current_Term_Dean_List");
        td.addStringColumn("Current_Term_Academic_Action");
        td.addStringColumn("Current_Term_Withdraw_Code");
        td.addDateColumn("Current_Term_Withdraw_Date");
        td.addStringColumn("Current_Term_Graduation_Code");
        td.addStringColumn("Current_Post_Grades");
        // Classification Cummulative data
        td.addIntColumn("Cummulative_Year");
        td.addIntColumn("Cummulative_Term");
        td.addFloatColumn("Cummulative_Attempted_Hours");
        td.addFloatColumn("Cummulative_Earned_Hours");
        td.addFloatColumn("Cummulative_Earned_Quality_Hours");
        td.addFloatColumn("Cummulative_Quality_Points");
        td.addFloatColumn("Cummulative_GPA");
        td.addFloatColumn("Total_Hours_Transfered");
        td.addFloatColumn("Total_Hours_Toward_Graduation");
        //Financial Aid Data
        td.addIntColumn("Award_Year");
        td.addStringColumn("Award_Period");
        td.addStringColumn("Applied_For_Need_Aid");
        td.addIntColumn("Total_Budget");
        td.addIntColumn("Total_Need");
        td.addIntColumn("Total_Family_Contribution");
        td.addIntColumn("Federal_Income_Student");
        td.addIntColumn("Federal_Income_Parent");
        td.addStringColumn("Dependency_Status");
        td.addIntColumn("Total_Aid_Awarded_Amount");
        // Amount_By_Award_Class
        td.addIntColumn("Grant_Amount");
        td.addIntColumn("Loan_Amount");
        td.addIntColumn("Job_Amount");
        // Amount_By_Source_Of_Aid
        td.addIntColumn("Outside_Award_Private_Amount");
        td.addIntColumn("Institution_Aid_Amount");
        td.addIntColumn("Student_Aid_State_Amount");
        td.addIntColumn("Student_Aid_Federal_Amount");
        td.addIntColumn("Source_Unknown_Amount");
        // Amount_By_Ims_Category
        td.addIntColumn("Campus_Based_Aid_Amount");
        td.addIntColumn("Minority_Aid_Amount");
        td.addIntColumn("Athletic_Aid_Amount");
        td.addIntColumn("Tuition_Waiver_Amount");
        td.addIntColumn("Pell_Amount");
        td.addIntColumn("GSL_Amount");
        td.addIntColumn("Other_Aid_Amount");
        td.addStringColumn("EMPLD");
        td.addStringColumn("Fall_Sport_Code");
        td.addStringColumn("Winter_Sport_Code");
        td.addStringColumn("Spring_Sport_Code");
        td.addStringColumn("Sem_AA_GI_Cert_Term");
        td.addStringColumn("Sem_AA_GI_Cert_Type");
        td.addStringColumn("Sem_Current_Primary_Track");
        DBInit.createTable(td);
    }
}