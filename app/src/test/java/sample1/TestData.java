package sample1;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import lombok.Data;

@Data
public class TestData {
    private int no;
    private String method;
    private String path;
    private String input;
    private int status;
    private int count;
    private String expected;
    private String resultName;
    private boolean skip;

    public TestData(Map<String, Cell> map) {
        this.no = getIntValue(map, "No");
        this.method = getStringValue(map, "Method");
        this.path = getStringValue(map, "Path");
        this.input = getStringValue(map, "Input");
        this.status = getIntValue(map, "Status");
        this.count = getIntValue(map, "Count");
        this.expected = getStringValue(map, "Expected");
        this.resultName = getStringValue(map, "ResultName");
        this.skip = getBooleanValue(map, "Skip");
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
