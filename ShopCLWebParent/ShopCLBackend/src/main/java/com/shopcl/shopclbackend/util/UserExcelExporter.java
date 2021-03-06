package com.shopcl.shopclbackend.util;

import com.shopcl.common.entity.User;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public UserExcelExporter() {

    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Usuários");
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);
        createCell(row, 0, "Usuário ID", cellStyle);
        createCell(row, 1, "Email", cellStyle);
        createCell(row, 2, "Nome", cellStyle);
        createCell(row, 3, "Sobrenome", cellStyle);
        createCell(row, 4, "Funções", cellStyle);
        createCell(row, 5, "Status", cellStyle);
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, XSSFCellStyle cellStyle) {

        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle);
    }

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx");
        writeHeaderLine();
        writeDataLines(listUsers);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<User> listUsers) {
        int rowIndex = 1;
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);
        for (User user : listUsers) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getFirstName(), cellStyle);
            createCell(row, columnIndex++, user.getLastName(), cellStyle);
            createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, columnIndex++, user.isEnabled(), cellStyle);
        }
    }
}
