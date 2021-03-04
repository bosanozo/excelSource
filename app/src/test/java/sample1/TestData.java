package sample1;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import lombok.Data;

@Data
public class TestData {
    private int no;
    private String description;
    private String method;
    private String path;
    private String input;
    private int status;
    private int count;
    private String expected;
    private String resultName;
    private boolean skip;

    public TestData(Map<String, Cell> map) {
        this.no = getIntValue(map, "no");
        this.description = getStringValue(map, "description");
        this.method = getStringValue(map, "method");
        this.path = getStringValue(map, "path");
        this.input = getStringValue(map, "input");
        this.status = getIntValue(map, "status");
        this.count = getIntValue(map, "count");
        this.expected = getStringValue(map, "expected");
        this.resultName = getStringValue(map, "resultname");
        this.skip = getBooleanValue(map, "skip");
    }

    public String toString() {
        return this.method + " " + this.path;
    }

    private String getStringValue(Map<String, Cell> map, String key) {
        Cell cell = map.get(key);
        return cell != null ? cell.getStringCellValue() : null;
    }

    private int getIntValue(Map<String, Cell> map, String key) {
        Cell cell = map.get(key);
        return cell != null ? (int)cell.getNumericCellValue() : 0;
    }

    private boolean getBooleanValue(Map<String, Cell> map, String key) {
        Cell cell = map.get(key);
        return cell != null ? cell.getBooleanCellValue() : false;
    }
}
