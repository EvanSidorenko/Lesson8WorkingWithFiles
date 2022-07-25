package workingWithFiles;

import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsExactText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class Homework {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/zipHomework/zipFiles.zip"));
        ZipInputStream is =  new ZipInputStream(Objects.requireNonNull(classLoader.getResourceAsStream("files/zipHomework/zipFiles.zip")));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {

            if (entry.getName().equals("csv_example.csv")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    CSVReader csvReader = new CSVReader(
                            new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                    List<String[]> content = csvReader.readAll();

                    org.assertj.core.api.Assertions.assertThat(content).contains(
                            new String[]{"Neo", "Matrix1", "Hero"},
                            new String[]{"Smith", "Agent", "Villain"},
                            new String[]{"Trinity", "Trin", "Lover"});

                }

            }

            if (entry.getName().equals("pdf/pdf_example.pdf")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);

                    Assertions.assertEquals(2, pdf.numberOfPages);
                    assertThat(pdf, new ContainsExactText("Simple PDF File"));
                }
            }

            if (entry.getName().equals("xlsx/xls_example.xls")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);

                    String value = xls.excel.getSheetAt(0).getRow(2).getCell(6).getStringCellValue();
                    org.assertj.core.api.Assertions.assertThat(value).contains("Great Britain");
                }

            }
        }
        System.out.println("Все тесты работают.");
    }
}

