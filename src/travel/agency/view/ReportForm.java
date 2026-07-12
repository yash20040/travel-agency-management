package travel.agency.view;

import net.sf.jasperreports.engine.*;
import travel.agency.util.DBConnection;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;

public class ReportForm {

    public static void showBookingReport() {
        try {
            String reportPath = "reports/BookingSummaryReport.jrxml";
            File reportFile = new File(reportPath);
            System.out.println("Looking for report at: " + reportFile.getAbsolutePath());
            System.out.println("File exists? " + reportFile.exists());

            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            System.out.println("Report compiled successfully.");

            Connection conn = DBConnection.getConnection();
            System.out.println("DB connection obtained: " + (conn != null));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
            System.out.println("Report filled successfully.");

            String outputPath = "reports/BookingSummaryReport.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            System.out.println("PDF exported to: " + new File(outputPath).getAbsolutePath());

            File pdfFile = new File(outputPath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
                System.out.println("Desktop.open() called.");
            } else {
                JOptionPane.showMessageDialog(null, "Report generated at: " + pdfFile.getAbsolutePath());
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to generate report: " + e.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}