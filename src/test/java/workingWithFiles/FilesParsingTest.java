package workingWithFiles;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static com.codeborne.selenide.Selectors.byText;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parsePdfTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = Selenide.$(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Marc Philipp");
        assertThat(pdf.numberOfPages).isEqualTo(166);
    }

    @Test
    void parseXlsTest() throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("files/xls/prajs_ot_0807.xls");
        XLS xls = new XLS(stream);

        String stringCellValue = xls.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue();
        assertThat(stringCellValue).contains("Сахалинская обл, Южно-Сахалинск г");
    }

    @Test
    void parseCsvTest() throws Exception {

        try (InputStream is = classLoader.getResourceAsStream("files/csv/machine-readable-business-employment-data-mar-2022-quarter.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> list = reader.readAll();
            assertThat(list.get(0)).contains("Series_reference",
                    "Period",
                    "Data_value",
                    "Suppressed",
                    "STATUS",
                    "UNITS",
                    "Magnitude",
                    "Subject",
                    "Group",
                    "Series_title_1",
                    "Series_title_2",
                    "Series_title_3",
                    "Series_title_4",
                    "Series_title_5");
        }
    }

    @Test
    void parseZipTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("files/zip/Lesson8ReadingFiles.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
            assertThat(entry.getName()).isEqualTo("junit-user-guide-5.8.2.pdf, ");
            }
        }
    }
}



