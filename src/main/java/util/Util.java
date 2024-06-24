package util;

import entity.Tool;
import entity.ToolType;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Map<String, Tool> createToolInventory() {
        Map<String, Tool> tools = new HashMap<>();
        tools.put("CHNS", new Tool("CHNS", ToolType.CHAINSAW, "Stihl", 1.49, true, false, true));
        tools.put("LADW", new Tool("LADW", ToolType.LADDER, "Werner", 1.99, true, true, false));
        tools.put("JAKD", new Tool("JAKD", ToolType.JACKHAMMER, "DeWalt", 2.99, true, false, false));
        tools.put("JAKR", new Tool("JAKR", ToolType.JACKHAMMER, "Ridgid", 2.99, true, false, false));
        return tools;
    }
}
