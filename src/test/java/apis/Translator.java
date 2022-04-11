package apis;

import java.io.IOException;
import java.util.List;

public interface Translator {

    List<String> translate(String text) throws IOException;

}
