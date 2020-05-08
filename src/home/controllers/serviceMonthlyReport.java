package home.controllers;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class serviceMonthlyReport {

    private Connection conn = null;
    PreparedStatement pstmt = null;

    public void Main(int month, String serviceType) throws Exception {
        String st;
        int m;
        m = month;
        st = serviceType;
        // Creating a PdfDocument object
        String dest = "ServiceMonthlyReport.pdf";
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument object
        PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document object
        Document doc = new Document(pdf);

        //Title for the document
        Paragraph reportTitle = new Paragraph("Service Monthly Report");
        reportTitle.setPaddingTop(10f);
        reportTitle.setPaddingBottom(20f);

        // Part for Customer and Company details
        Paragraph Details = new Paragraph();
        Details.add("Quick Fix fitters");
        Details.add("\n");
        Details.add("10 High st."); // Company Address
        Details.add("\n");
        Details.add("Ashford"); // Company City
        Details.add("\n");
        Details.add("CT16 8YY"); // Company Postcode

        //Creation date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        Paragraph date = new Paragraph("Creation Date: "+ dtf.format(localDate)); // Get current date the invoice is generated
        date.setPaddingBottom(10f);

        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy");
        LocalDate localDate1 = LocalDate.now();
        date.setPaddingBottom(10f);

        //Monthly period
        Paragraph monthlyPeriod = new Paragraph("Monthly Period: " + m);
        monthlyPeriod.add("/"+dt.format(localDate1));

        //ServicesTable creation
        float[] columnWidths = {80, 80, 80, 80, 80, 80};
        Table servicesTable = new Table(columnWidths);
        servicesTable.setFontSize(10f);
        //Call function to add rows in parts table
        dbGetServices(m,st,servicesTable);


        // Adding Title to document '
        doc.add(reportTitle);
        doc.add(Details);
        doc.add(date);
        doc.add(monthlyPeriod);


        // Adding Table to document
        createColumns(doc);
        doc.add(servicesTable);

        // Closing the document
        doc.close();
        Desktop.getDesktop().open(new File("ServiceMonthlyReport.pdf"));
        System.out.println("Table created successfully..");
    }

    public void createColumns(Document doc) {
        float[] columnWidths = {80, 80, 80, 80, 80, 80};
        Table table = new Table(columnWidths);
        table.setFontSize(10f);
        //Adds a row of parts Max of 6 columns. // Each row has max of 6 columns. 7th addCell makes a new row
        //6 columns each row
        table.addCell(new Cell().add("Job Number")); // Col 1
        table.addCell(new Cell().add("Task Description")); // Col 2
        table.addCell(new Cell().add("Date Booked")); // Col 3
        table.addCell(new Cell().add("Vehicle Reg No")); // Col 4
        table.addCell(new Cell().add("Customer ID")); // Col 5
        table.addCell(new Cell().add("Service Type")); // Col 6
        table.setPaddingBottom(5);
        doc.add(table);
    }

    public void dbGetServices(int m,String st, Table serviceTable) {

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT JobNumber, TaskDescription, dateBooked, VehiclevehicleRegNo,VehicleCustomercustomerID, ServiceType FROM Job WHERE Month = '"+ m +"' AND ServiceType = '"+ st + "'");

            while (rs.next()) {
                //Adds a row of parts Max of 6 columns. // Each row has max of 6 columns. 7th addCell makes a new row
                //Row
                serviceTable.addCell(new Cell().add(rs.getString("JobNumber"))); // Col 1
                serviceTable.addCell(new Cell().add(rs.getString("TaskDescription"))); // Col 2
                serviceTable.addCell(new Cell().add(rs.getString("dateBooked"))); // Col 3
                serviceTable.addCell(new Cell().add(rs.getString("VehiclevehicleRegNo"))); // Col 4
                serviceTable.addCell(new Cell().add(rs.getString("VehicleCustomercustomerID"))); // Col 5
                serviceTable.addCell(new Cell().add(rs.getString("ServiceType"))); // Col 6
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
