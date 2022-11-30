import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class Student implements Jsonable {
    private String nev;
    private String neptunkod;
    private String szak;

    public Student(String nev, String neptunkod, String szak) {
        this.nev = nev;
        this.neptunkod = neptunkod;
        this.szak = szak;
    }

    @Override
    public String toJson() {
        final StringWriter writer = new StringWriter();

        try {
            this.toJson(writer);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return writer.toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("nev", this.nev);
        json.put("neptunkod", this.neptunkod);
        json.put("szak", this.szak);
        json.toJson(writer);
    }
}
