package com.kontrol;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.kontrol.websocket.DownloadSocket;
import com.kontrol.websocket.FileDownload;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Path("/report")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public String hello() {
        byte[] binaryFile = generateReport();

        FileDownload fileDownload = new FileDownload();
        fileDownload.binaryData = binaryFile;
        fileDownload.fileName = "Payment Report.pdf";
        DownloadSocket.send(fileDownload);

        return "Mudi Lukman";
    }

    private static byte[] generateReport() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        try(Document document = new Document(pdfDocument)) {
            ImageData imageData = ImageDataFactory.create(GreetingResource.class.getResource("/logo.png"));
            Image image = new Image(imageData);
            float[] scale = getImageScale(image.getImageWidth(), image.getImageHeight());
            image.setWidth(scale[0]);
            image.setHeight(scale[1]);
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(image);

            Paragraph orgName = new Paragraph("Federal University of Technology, Minna");
            orgName.setTextAlignment(TextAlignment.CENTER);
            orgName.setBold();
            document.add(orgName);

            String header = "Payment Report";
            if (true) {
                header += " for 2014/2015 Admission Session";
            }
            if (true) {
                header += " between January 01, 2022 to " + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }
            Paragraph reportName = new Paragraph(header);
            reportName.setTextAlignment(TextAlignment.CENTER);
            document.add(reportName);

            float columnWidths[] = new float[]{50f, 200f, 250f, 250f, 100f, 100f, 100f};
            Table table = new Table(columnWidths);

            table.addCell(new Cell().add(new Paragraph("S/N").setBold().setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph("Applicant ID").setBold().setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph("Applicant Name").setBold().setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph("Course").setBold().setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph("Transaction Date").setBold().setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph("Status").setBold().setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph("Amount (N)").setBold().setFontSize(8)));

            for (int i = 1; i <= 100; i++) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(i)).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph("2021-INFOTECH-002-00002").setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph("Mudi Lukman").setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph("Information Technology").setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(LocalDate.now().toString()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph("SUCCESS").setFontSize(8)));
                double amount = 53_000;
                table.addCell(new Cell().add(new Paragraph(String.format("%,.2f", amount)).setFontSize(8)));
            }

            document.add(table);
        }
        return outputStream.toByteArray();
    }

    private static float[] getImageScale(float width, float height) {
        float[] scale = new float[] {50f, 50f};
        if (width > height) {
            //80 by 40
            scale[0] = 80f;
            scale[1] = 40f;
        } else if (width < height) {
            //50 by 80
            scale[1] = 80f;
        }
        return scale;
    }
}