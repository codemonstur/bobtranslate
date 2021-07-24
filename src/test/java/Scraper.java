import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

import java.io.IOException;
import java.text.ParseException;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class Scraper {

    // Doesn't work.
    public static void main(String[] args) throws IOException, ParseException {

        ChromeDriverManager.getInstance(DriverManagerType.CHROME).browserVersion("76.0.3809.126").setup();
        Configuration.startMaximized = true;
        open("https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=nl");
        String[] strings = {"hello", "A simple sentence"};
        for (String data: strings) {
            $x("//textarea[@id='source']").clear();
            $x("//textarea[@id='source']").sendKeys(data);
            String translation = $x("//span[@class='tlid-translation translation']").getText();
        }
    }

}
