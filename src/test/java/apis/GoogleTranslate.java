package apis;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

import java.util.List;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public final class GoogleTranslate implements Translator {

    // Doesn't work.
    @Override  public List<String> translate(final String text) {
        ChromeDriverManager.getInstance(DriverManagerType.CHROME).browserVersion("76.0.3809.126").setup();
        // Configuration.startMaximized = true;
        open("https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=nl");
        String[] strings = {"hello", "A simple sentence"};
        for (String data: strings) {
            $x("//textarea[@id='source']").clear();
            $x("//textarea[@id='source']").sendKeys(data);
            String translation = $x("//span[@class='tlid-translation translation']").getText();
        }
        return null;
    }

}
