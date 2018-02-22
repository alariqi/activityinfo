package chdc.frontend.client.table;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * Placeholder for unimplemented columns
 */
public class TodoValueProvider implements ValueProvider<Integer, String> {

    private String path;

    public TodoValueProvider(String path) {
        this.path = path;
    }

    @Override
    public String getValue(Integer object) {
        return "#TODO";
    }

    @Override
    public void setValue(Integer object, String value) {
    }

    @Override
    public String getPath() {
        return path;
    }
}
