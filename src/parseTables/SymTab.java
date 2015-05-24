package parseTables;

import java.util.*;

public class SymTab {

    private HashMap<String, Object> table;

    public SymTab() {
        table = new HashMap<String, Object>();
    }

    public Set<String> keySet() {
        return table.keySet();
    }

    public void add(String name, Object o) {
        table.put(name, o);
    }

    public Object get(String name) {
        return table.get(name);
    }

    public String toString() {
        String s = "";
        Set<String> keys = table.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext()) {
            String k = i.next();
            s = k + ":" + table.get(k) + "\n";
        }
        return s;
    }

    public void setValue(String name, double value) {
        Object o = table.get(name);
        if (o.getClass() == Double.class)
            table.put(name, new Double(value));
    }

    public double getValue(String name) {
        Object o = table.get(name);
        if (o.getClass() == Double.class)
            return ((Double) o).doubleValue();
        else
            return 0;
    }

}
