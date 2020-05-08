package home.controllers;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class JI {

    private Connection conn = null;
    PreparedStatement pstmt = null;

    String CustomerID, Name, Address, City, Country, Postcode;
    String VehicleRegNo, Make, Model;
    String Date, WorkDescription, ServiceType;

    int Jn, c = 0;

    double BeforeVAT = 0;
    double Total = 0;

    double PP2, LP2, SP2;

    String CustomerType;

    boolean hasDiscount = false;

    String DiscountTypeLabel = "";

    double PP, LP, SP;



    public void Main(String jobNumber) throws IOException {


        Jn = Integer.parseInt(jobNumber);

        dbJobInformation(jobNumber);

        dbCustomerInformation(CustomerID);

        dbVehicleInformation(VehicleRegNo);



        // Creating a PdfDocument object
        String dest = "testPDF.pdf";
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument object
        PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document object
        Document doc = new Document(pdf);

        //Title for the document
        Paragraph Title = new Paragraph("Job Invoice");
        Title.setPaddingTop(10f);
        Title.setPaddingBottom(20f);

        // Part for Customer and Company details
        Paragraph Details = new Paragraph(Name); // Add Customer name variable
        Details.add(new Tab());
        Details.addTabStops(new TabStop(450, TabAlignment.RIGHT));
        Details.add("Quick Fix fitters"); // Company name
        Details.add("\n");

        //Address
        Details.add(Address); // Customer Address
        Details.add(new Tab());
        Details.addTabStops(new TabStop(450,TabAlignment.RIGHT));
        Details.add("10 High st."); // Company Address

        //City
        Details.add(City); // Add variable Customer Address
        Details.add(new Tab());
        Details.addTabStops(new TabStop(450,TabAlignment.RIGHT));
        Details.add("Ashford"); // Company Address

        //Postcode
        Details.add(Postcode); // Add customer Postcode variable
        Details.add(new Tab());
        Details.addTabStops(new TabStop(450,TabAlignment.RIGHT));
        Details.add("CT16 8YY"); // Company postcode

        //Creation date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Paragraph date = new Paragraph (dtf.format(localDate)); // Get current date the Job invoice is generated
        date.setPaddingLeft(360f);

        //Dear customer text
        Paragraph dearCustomer = new Paragraph("Dear " + Name); // Insert Customer Name variable
        dearCustomer.setPaddingBottom(15);

        //Invoice Number
        Paragraph invoiceNumber = new Paragraph("Job Number:" + jobNumber ); // Add invoice number variable
        invoiceNumber.setTextAlignment(TextAlignment.CENTER);
        invoiceNumber.setBold();

        //Vehicle Reg number, make, model
        Paragraph vehicleRegNumber = new Paragraph("Vehicle Registration Number: " + VehicleRegNo); // Insert Vehicle reg number variable here
        Paragraph vehicleMake = new Paragraph("Make: " + Make); // Insert vehicle make variable here
        Paragraph vehicleModel = new Paragraph ("Model: "+ Model); // Insert vehicle model variable here
        vehicleRegNumber.setBold();
        vehicleMake.setBold();
        vehicleModel.setBold();

        //Description of work:
        Paragraph tasksDone = new Paragraph ("Description of work: " );
        Paragraph tasks = new Paragraph(WorkDescription);
        tasks.setPaddingBottom(15f);



        // Creating a PartsTable
        float [] pointColumnWidths = {100F, 100F, 80F,60F,100};
        Table PartsTable = new Table(pointColumnWidths); // For parts
        Table PartsPrice = new Table(pointColumnWidths); // For Total price parts
        Table LabourTable = new Table(pointColumnWidths); // For Labour
        Table LabourPrice = new Table(pointColumnWidths); // Labour Price
        Table ServicePrice = new Table(pointColumnWidths); // Service Price
        Table TotalTable = new Table(pointColumnWidths); // For Total
        Table VATTable = new Table(pointColumnWidths); // For VAT
        Table GrandTotalTable = new Table(pointColumnWidths); // For Grand Total
        PartsTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        PartsPrice.setHorizontalAlignment(HorizontalAlignment.CENTER);
        LabourTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        LabourPrice.setHorizontalAlignment(HorizontalAlignment.CENTER);
        ServicePrice.setHorizontalAlignment(HorizontalAlignment.CENTER);
        TotalTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        VATTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        GrandTotalTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //Adding cells to the PartsTable (Turn this into a function with data as parameters)
        //Adds a row of column name
        PartsTable.addCell(new Cell().add("Item"));
        PartsTable.addCell(new Cell().add("Part No."));
        PartsTable.addCell(new Cell().add("Unit Cost"));
        PartsTable.addCell(new Cell().add("Quantity"));
        PartsTable.addCell(new Cell().add(""));

        dbGetPartsUsed(jobNumber, PartsTable);

        dbGetTotalPriceParts(jobNumber, PartsPrice);

        PartsTable.setPaddingBottom(5);

        //Labour
        LabourTable.addCell(new Cell().add("Mechanic Name"));
        LabourTable.addCell(new Cell().add("Duration Minutes"));
        LabourTable.addCell(new Cell().add("Task")); // Would be hourly rate of mechanic
        LabourTable.addCell(new Cell().add("Hourly Rate")); // Duration of work done
        LabourTable.addCell(new Cell().add("")); // Hourly rate * work done
        LabourTable.setPaddingBottom(5);

        dbGetTasksDone(jobNumber, LabourTable);

        dbGetTotalPriceLabour(jobNumber, LabourPrice);

        LabourTable.setPaddingBottom(5);

        //Total
        ServicePrice.addCell(new Cell().add("")); // Col 1
        ServicePrice.addCell(new Cell().add("")); // Col 2
        ServicePrice.addCell(new Cell().add(ServiceType)); // Col 3
        ServicePrice.addCell(new Cell().add("")); // Col 4
        ServicePrice.addCell(new Cell().add(ServicePrice)); // Cost of parts and labour cost

        Double NP = PP2 + PP2*0.3;

        Double DP = NP + SP2 + LP2;

        //Total
        TotalTable.addCell(new Cell().add("")); // Col 1
        TotalTable.addCell(new Cell().add("")); // Col 2
        TotalTable.addCell(new Cell().add("Total")); // Col 3
        TotalTable.addCell(new Cell().add("")); // Col 4
        TotalTable.addCell(new Cell().add(DP + "")); // Cost of parts and labour cost


        Double VAT = DP*0.2;

        Double TD = BigDecimal.valueOf(VAT).setScale(3, RoundingMode.HALF_UP).doubleValue();

        //VAT
        VATTable.addCell(new Cell().add("")); // Col 1
        VATTable.addCell(new Cell().add("")); // Col 2
        VATTable.addCell(new Cell().add("VAT")); // Col 3
        VATTable.addCell(new Cell().add("")); // Col 4
        VATTable.addCell(new Cell().add(TD + "")); // 12.5% of total cost

        Double GT = DP + TD;

        //Grand Total
        GrandTotalTable.addCell(new Cell().add("")); // Col 1
        GrandTotalTable.addCell(new Cell().add("")); // Col 2
        GrandTotalTable.addCell(new Cell().add("Grand Total")); // Col 3
        GrandTotalTable.addCell(new Cell().add("")); // Col 4
        GrandTotalTable.addCell(new Cell().add(GT + "")); // Cost of VAT and Total


        Paragraph thankYou = new Paragraph("Thank you for your valued custom. We look forward to receiving your payment in due course");
        thankYou.setPaddingTop(10);
        thankYou.setPaddingBottom(10);

        Paragraph yoursSincerely = new Paragraph ("Yours sincerely, ");
        yoursSincerely.setPaddingBottom(6);

        Paragraph franchiseeName = new Paragraph ("G. Lancaster");

        // Adding Title to document 'Invoice'
        doc.add(Title);
        doc.add(Details);
        doc.add(date);
        doc.add(dearCustomer);
        doc.add(invoiceNumber);
        doc.add(vehicleRegNumber);
        doc.add(vehicleMake);
        doc.add(vehicleModel);
        doc.add(tasksDone);
        doc.add(tasks);

        // Adding Table to document
        doc.add(PartsTable);
        doc.add(PartsPrice);
        doc.add(LabourTable);
        doc.add(LabourPrice);
        doc.add(TotalTable);
        doc.add(VATTable);
        doc.add(GrandTotalTable);

        //Things after PartsTable
        doc.add(thankYou);
        doc.add(yoursSincerely);
        doc.add(franchiseeName);

        // Closing the document
        doc.close();
        Desktop.getDesktop().open(new File("testPDF.pdf"));

   }

    public void dbJobInformation(String s){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Job WHERE JobNumber = '" + s + "'");

            while (rs.next()){

                CustomerID = rs.getString("VehicleCustomercustomerID");
                VehicleRegNo = rs.getString("VehiclevehicleRegNo");
                Date = rs.getString("DateBooked");
                WorkDescription = rs.getString("TaskDescription");
                ServiceType = rs.getString("ServiceType");
                PP = rs.getDouble("ServicePrice");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbCustomerInformation(String c){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Customer WHERE customerID = '" + c + "'");

            while (rs.next()){

                Address = rs.getString("address");
                City = rs.getString("city");
                Name = rs.getString("name");
                Country = rs.getString("country");
                Postcode = rs.getString("postcode");

            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbVehicleInformation(String v){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Vehicle WHERE RegNo = '" + v + "'");

            while (rs.next()){

                Make = rs.getString("Make");
                Model = rs.getString("Model");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbGetPartsUsed(String j, Table PartsTable) {

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT DISTINCT PartName, PartPrice, PartQuantity, PartCode FROM Job JOIN PartUsed ON (PartUsed.JobJobNumber = '" + j +  "') JOIN Part ON (Part.PartCode = PartUsed.PartPartCode)");

            while (rs.next()) {

                //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
                //Row 1
                PartsTable.addCell(new Cell().add(rs.getString("PartName"))); // Col 1
                PartsTable.addCell(new Cell().add(rs.getString("PartCode"))); // Col 2
                PartsTable.addCell(new Cell().add(rs.getDouble("PartPrice")*1.3 + "")); // Col 3
                PartsTable.addCell(new Cell().add(rs.getString("PartQuantity"))); // Col 4
                PartsTable.addCell(new Cell().add("")); // Col 4
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dbGetTotalPriceParts(String j,Table PartsTable){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(PartPrice * PartQuantity) FROM (\n" +
                    "\n" +
                    "SELECT DISTINCT PartName, PartPrice, PartQuantity, PartCode \n" +
                    "FROM Job JOIN PartUsed ON (PartUsed.JobJobNumber = '"+ j +"') \n" +
                    "JOIN Part ON (Part.PartCode = PartUsed.PartPartCode)\n" +
                    ")\n");

            while (rs.next()) {

                //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
                //Row 1
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add("£ " + rs.getString("SUM(PartPrice * PartQuantity)")));
                PP = rs.getInt("SUM(PartPrice * PartQuantity)");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbGetTasksDone(String j, Table PartsTable){

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT DISTINCT TaskName, Duration, TaskMechanic, hourlyRate \n" +
                    "FROM Job JOIN Tasks ON (Tasks.JobJobNumber = '" + j + "') \n" +
                    "JOIN Staff ON (Staff.username = Tasks.TaskMechanic)");
            while (rs.next()) {

                //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
                //Row 1
                PartsTable.addCell(new Cell().add(rs.getString("TaskName"))); // Col 1
                PartsTable.addCell(new Cell().add(rs.getString("Duration"))); // Col 2
                PartsTable.addCell(new Cell().add(rs.getString("TaskMechanic"))); // Col 3
                PartsTable.addCell(new Cell().add(rs.getString("hourlyRate"))); // Col 4
                PartsTable.addCell(new Cell().add("")); // Col 4
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbGetTotalPriceLabour(String j,Table PartsTable){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(Duration * (hourlyRate/60)) FROM (\n" +
                    "\n" +
                    "SELECT DISTINCT TaskName, Duration, TaskMechanic, hourlyRate \n" +
                    "FROM Job JOIN Tasks ON (Tasks.JobJobNumber = '" + j + "') \n" +
                    "JOIN Staff ON (Staff.username = Tasks.TaskMechanic)\n" +
                    ")\n");

            while (rs.next()) {

                //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
                //Row 1
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add(""));
                PartsTable.addCell(new Cell().add("£ " + rs.getString("SUM(Duration * (hourlyRate/60))")));
                LP = rs.getInt("SUM(Duration * (hourlyRate/60))");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void calculateDetails(){
        dbJobInformation(Jn);
        dbGetTotalPriceParts(Jn);
        dbGetTotalPriceLabour(Jn);
        dbGetCurrentMonthSpending();

        Double PartMarkup = PP2 * 1.3;

        if (CustomerType.equals("Casual")){
            Double BVAT = PartMarkup + LP2 + SP2;
            BeforeVAT = BVAT;
            Total = BVAT * 1.2;
        }else if (CustomerType.equals("Account Holder")){
            if(DiscountTypeLabel.equals("No Discount")){
                Double BVAT = PartMarkup + LP2 + SP2;
                BeforeVAT = BVAT;
                Total = BVAT * 1.2;
            }else if (DiscountTypeLabel.equals("Fixed")){
                Double BVAT = PartMarkup + LP2 + SP2;
                int FixedDiscount = Integer.parseInt(Rate1Label);
                BeforeVAT = (BVAT * (100 - FixedDiscount)) / 100;
                Total = BeforeVAT * 1.2;
            }else if (DiscountTypeLabel.equals("Variable")){

                int PartsDiscount = Integer.parseInt(Rate1Label);
                int ServiceDiscount = Integer.parseInt(Rate2Label);

                Double newLP = (LP2 * (100 - ServiceDiscount)) / 100;
                Double newSP = (SP2 * (100 - ServiceDiscount)) / 100;
                Double newPP = (PartMarkup * (100 - PartsDiscount)) / 100;

                Double BVAT = newPP + newSP + newLP;

                BeforeVAT = BVAT;
                Total = BeforeVAT * 1.2;
            }else if (DiscountTypeLabel.equals("Flexible")){
                if(MonthlySpending < 1000){
                    Double BVAT = PartMarkup + LP2 + SP2;
                    int BasicDiscount = Integer.parseInt(Rate1Label);
                    BeforeVAT = (BVAT * (100 - BasicDiscount)) / 100;
                    Total = BeforeVAT * 1.2;
                }else if (MonthlySpending > 1000 && MonthlySpending < 5000){
                    Double BVAT = PartMarkup + LP2 + SP2;
                    int StandardDiscount = Integer.parseInt(Rate2Label);
                    BeforeVAT = (BVAT * (100 - StandardDiscount)) / 100;
                    Total = BeforeVAT * 1.2;
                }else if (MonthlySpending > 5000){
                    Double BVAT = PartMarkup + LP2 + SP2;
                    int PremiumDiscount = Integer.parseInt(Rate3Label);
                    BeforeVAT = (BVAT * (100 - PremiumDiscount)) / 100;
                    Total = BeforeVAT * 1.2;
                }

            }

        }

    }

    public void dbJobInformation(int s){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Job WHERE JobNumber = '" + s + "'");

            while (rs.next()){
                PP = rs.getDouble("ServicePrice");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbGetTotalPriceParts(int j){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(PartPrice * PartQuantity) FROM (\n" +
                    "\n" +
                    "SELECT DISTINCT PartName, PartPrice, PartQuantity, PartCode \n" +
                    "FROM Job JOIN PartUsed ON (PartUsed.JobJobNumber = '"+ j +"') \n" +
                    "JOIN Part ON (Part.PartCode = PartUsed.PartPartCode)\n" +
                    ")\n");

            while (rs.next()) {
                PP = rs.getInt("SUM(PartPrice * PartQuantity)");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbGetTotalPriceLabour(int j){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(Duration * (hourlyRate/60)) FROM (\n" +
                    "\n" +
                    "SELECT DISTINCT TaskName, Duration, TaskMechanic, hourlyRate \n" +
                    "FROM Job JOIN Tasks ON (Tasks.JobJobNumber = '" + j + "') \n" +
                    "JOIN Staff ON (Staff.username = Tasks.TaskMechanic)\n" +
                    ")\n");

            while (rs.next()) {
                LP = rs.getInt("SUM(Duration * (hourlyRate/60))");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbGetCurrentMonthSpending(){
        conn = database.dbConnection.garitsConnection();

        int Month = LocalDateTime.now().getMonth().getValue();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(AmountPaid) FROM Payment WHERE PaymentMonth = '" + Month + "' AND PaymentCustomerID = '" + c + "'");

            while (rs.next()){
                MonthlySpending = rs.getDouble("SUM(AmountPaid)");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String Rate1Label,Rate2Label, Rate3Label;

    double MonthlySpending = 0;

    public void setJobNumber(){
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Job WHERE JobNumber = '" + Jn + "'");

            while (rs.next()){
                c = rs.getInt("VehicleCustomercustomerID");

            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDetails() {
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Customer WHERE customerID = '" + c + "'");

            while (rs.next()){
                Name = rs.getString("Name");
                CustomerType = rs.getString("AccountType");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDiscountType() {
        {
            try {
                conn = database.dbConnection.garitsConnection();

                ResultSet rs = conn.createStatement().executeQuery("select * from DiscountPlan WHERE CustomercustomerID = '" + c + "'");

                while (rs.next()) {

                    hasDiscount = true;

                    DiscountTypeLabel  = (rs.getString("Name"));

                    if (DiscountTypeLabel.equals("Fixed")){
                        Rate1Label = (rs.getString("FixedRate"));
                    }else if (DiscountTypeLabel.equals("Flexible")){
                        Rate1Label = (rs.getString("BasicRate"));
                        Rate2Label = (rs.getString("StandardRate"));
                        Rate3Label = (rs.getString("PremiumRate"));

                    }else if (DiscountTypeLabel.equals("Variable")){
                        Rate1Label = (rs.getString("PartsRate"));
                        Rate2Label = (rs.getString("ServiceRate"));
                    }
                }


                if (!hasDiscount){
                    DiscountTypeLabel = ("No Discount");
                }


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void changeJobStatus(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Job SET JobStatus = 'Paid' WHERE JobNumber = ? ";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Jn);

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Job is now paid!");

                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("");

                alert.showAndWait();
            }

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }


}
