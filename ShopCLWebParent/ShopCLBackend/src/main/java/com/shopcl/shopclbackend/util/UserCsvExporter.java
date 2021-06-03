package com.shopcl.shopclbackend.util;

import com.shopcl.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");
        Writer writer = new OutputStreamWriter(response.getOutputStream(), "utf-8");
        writer.write('\uFEFF');
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Usuário ID", "Email", "Nome", "Sobrenome", "Funções", "Habilitado"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
        csvBeanWriter.writeHeader(csvHeader);
        for (User user : listUsers) {
            csvBeanWriter.write(user, fieldMapping);
        }
        csvBeanWriter.close();
    }
}
