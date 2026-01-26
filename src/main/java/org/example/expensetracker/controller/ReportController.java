package org.example.expensetracker.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    public ReportController(ReportService reportService, UserRepository userRepository) {
        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showReport(
            Authentication authentication,
            Model model,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate toDate
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        Map<String, Object> report = new HashMap<>();

        // ONLY load data when user clicks Apply Filter
        if (fromDate != null && toDate != null) {
            report = reportService.getDateRangeReport(user, fromDate, toDate);
        } else {
            report.put("expenses", List.of());
            report.put("totalExpense", 0.0);
        }

        model.addAttribute("report", report);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        // ✅ REQUIRED for topbar templates (${user.name})
        model.addAttribute("user", user);

        return "report";
    }

    @GetMapping("/pdf")
    public void exportReportPdf(
            Authentication authentication,
            HttpServletResponse response,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate fromDate,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate toDate
    ) throws Exception {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return;
        }

        Map<String, Object> report =
                reportService.getDateRangeReport(user, fromDate, toDate);

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=expense_report_" + fromDate + "_to_" + toDate + ".pdf"
        );

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Expense Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("From: " + fromDate));
        document.add(new Paragraph("To: " + toDate));
        document.add(new Paragraph(" "));

        // Table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 3, 5, 3});

        addTableHeader(table);
        addTableData(table, (List<?>) report.get("expenses"));

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(
                "Total Expense: ₹ " + report.get("totalExpense"),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        ));

        document.close();
    }

    private void addTableHeader(PdfPTable table) {
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        Stream.of("Date", "Category", "Description", "Amount")
                .forEach(col -> {
                    PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(cell);
                });
    }

    private void addTableData(PdfPTable table, List<?> expenses) {
        for (Object obj : expenses) {
            Expense exp = (Expense) obj;

            table.addCell(exp.getDate().toString());
            table.addCell(exp.getCategory().getName());
            table.addCell(exp.getDescription());
            table.addCell("₹ " + exp.getAmount());
        }
    }
}
