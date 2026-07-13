package travel.agency.view;

import net.sf.jasperreports.engine.*;
import travel.agency.util.DBConnection;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;

public class ReportForm {

    public static void showBookingReport() {
        try {
            InputStream reportStream = ReportForm.class.getResourceAsStream("/travel/agency/reportfiles/BookingSummaryReport.jrxml");

            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, "Report template not found inside application.", "Report Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Connection conn = DBConnection.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);

            String outputPath = System.getProperty("user.home") + "\\BookingSummaryReport.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            File pdfFile = new File(outputPath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(null, "Report generated at: " + pdfFile.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to generate report: " + e.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}