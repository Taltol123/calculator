package org.example.calculator;

import java.util.*;

// Variable storage management
public class VariableStore {
    private final Map<String, Integer> variables = new HashMap<>();

    public void assign(String name, int value) {
        variables.put(name, value);
    }

    public int getValue(String name) {
        return variables.getOrDefault(name, 0);
    }

    public String getFormattedOutput() {
        if (variables.isEmpty()) {
            return "()";
        }

        List<String> sortedKeys = new ArrayList<>(variables.keySet());
        Collections.sort(sortedKeys);

        StringBuilder result = new StringBuilder("(");
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            result.append(key).append("=").append(variables.get(key));
            if (i < sortedKeys.size() - 1) {
                result.append(",");
            }
        }
        result.append(")");

        return result.toString();
    }

    public void clear() {
        variables.clear();
    }
}
