package workingWithFiles;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.$;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesDownload {

    @Test
    void downloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File fileDownload = Selenide.$("#raw-url").download();
        try (InputStream is = new FileInputStream(fileDownload)) {
            assertThat(new String(is.readAllBytes(), UTF_8)).contains("This repository is the home of the next generation of JUnit");
        }
        String readString= Files.readString(fileDownload.toPath(), UTF_8);
    }
    @Test
    void uploadSelenideTest() {
        Selenide.open("https://the-internet.herokuapp.com/upload");
       $("input[type='file']")
        // .uploadFile(new File("C:\\Users\\sidor\\IdeaProjects\\Lesson8WorkingWithFiles\\src\\test\\resources\\files\\file.txt")); //bad practice
        .uploadFromClasspath("files/txt/file.txt");
       $("#file-submit").click();
       $("div.example").shouldHave(Condition.text("File Uploaded!"));
       $("#uploaded-files").shouldHave(Condition.text("file.txt"));
    }


}
