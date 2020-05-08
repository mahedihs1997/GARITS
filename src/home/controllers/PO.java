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

public class PO {

    private Connection conn = null;
    PreparedStatement pstmt = null;

    public  void Main() throws Exception {
        // Creating a PdfDocument object
        String dest = "PartsOrder.pdf";
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument object
        PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document object
        Document doc = new Document(pdf);

        //Title for the document
        Paragraph reportTitle = new Paragraph("Parts Order");
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
        Paragraph date = new Paragraph (dtf.format(localDate)); // Get current date the invoice is generated
        date.setPaddingBottom(10f);

        //PartsTable creation
        float[] columnWidths = {80,80,80,80,80};
        Table PartsTable = new Table(columnWidths);
        PartsTable.setFontSize(10f);
        //Call function to add rows in parts table
        dbGetPartsUsed(PartsTable);
        dbGetTotalPrice(PartsTable);

        Paragraph Partsupplier = new Paragraph("Supplier: Fjord Supplies");
            Partsupplier.add("\n");
            Partsupplier.add("Address: 10 Largeunits, Trade Estate,Reading,RG10 4P");
            Partsupplier.add("\n");
            Partsupplier.add("\n");
            Partsupplier.add("Tel: 01895 453 857");
            Partsupplier.add("\n");
            Partsupplier.add("Fax: 01895 453 857");
            Partsupplier.setPaddingBottom(12f);

        Paragraph signature = new Paragraph("Signed: ");
        signature.setPaddingTop(10);

        // Adding Title to document 'Invoice'
        doc.add(reportTitle);
        doc.add(Details);
        doc.add(date);
        doc.add(Partsupplier);

        // Adding Table to document
        createColumns(doc);
        doc.add(PartsTable);

        //Things after table
        doc.add(signature);


        // Closing the document
        doc.close();
        Desktop.getDesktop().open(new File("PartsOrder.pdf"));
        System.out.println("Table created successfully..");
    }
    public void createColumns(Document doc)  {
        float[] columnWidths = {80, 80, 80, 80, 80};
        Table table = new Table(columnWidths);
        table.setFontSize(10f);
        //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
        //13 columns each row
        table.addCell(new Cell().add("Order Number")); // Col 1
        table.addCell(new Cell().add("Part Name")); // Col 2
        table.addCell(new Cell().add("Description")); // Col 3
        table.addCell(new Cell().add("Quantity")); // Col 4
        table.addCell(new Cell().add("Price")); // Col 5
        table.setPaddingBottom(5);
        doc.add(table);
    }
    public void dbGetPartsUsed(Table PartsTable) {

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT PartCode,PartName,Description, Threshold, (Threshold*PartPrice) AS Price FROM Part WHERE Quantity < Part.Threshold;");
            while (rs.next()) {

                //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
                //Row 1
                PartsTable.addCell(new Cell().add(rs.getString("PartCode"))); // Col 1
                PartsTable.addCell(new Cell().add(rs.getString("PartName"))); // Col 2
                PartsTable.addCell(new Cell().add(rs.getString("Description"))); // Col 3
                PartsTable.addCell(new Cell().add(rs.getString("Threshold"))); // Col 4
                PartsTable.addCell(new Cell().add(rs.getString("Price"))); // Col 4
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void dbGetTotalPrice(Table PartsTable) {

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(Price) FROM (SELECT PartCode,PartName,Description, Threshold, (Threshold*PartPrice) AS Price FROM Part WHERE Quantity < Part.Threshold);");
            while (rs.next()) {

                //Adds a row of parts Max of 5 columns. // Each row has max of 5 columns. 6th addCell makes a new row
                //Row 1
                PartsTable.addCell(new Cell().add("")); // Col 1
                PartsTable.addCell(new Cell().add("")); // Col 2
                PartsTable.addCell(new Cell().add("")); // Col 3
                PartsTable.addCell(new Cell().add("Total:")); // Col 4
                PartsTable.addCell(new Cell().add(rs.getString("SUM(Price)"))); // Col 4
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


