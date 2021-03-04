package sample1;

import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.poi.ss.usermodel.Cell;

import lombok.Data;

@Data
public class TestData {
    private int no;
    private String description;
    private String method;
    private String path;
    private JsonElement input;
    private int status;
    private Optional<Integer> count;
    private JsonElement expected;
    private String resultName;
    private boolean skip;

    public TestData(Map<String, Cell> map) {
        this.no = getIntValue(map.get("no"));
        this.description = getStringValue(map.get("description"));
        this.method = getStringValue(map.get("method"));
        this.path = getStringValue(map.get("path"));
        this.input = getJsonValue(map.get("input"));
        this.status = getIntValue(map.get("status"));
        Optional.ofNullable(map.get("count")).ifPresent(cell -> this.count = Optional.ofNullable((int)cell.getNumericCellValue()));
        this.expected = getJsonValue(map.get("expected"));
        this.resultName = getStringValue(map.get("resultname"));
        this.skip = getBooleanValue(map.get("skip"));
    }

    public String toString() {
        return this.method + " " + this.path;
    }

    private String getStringValue(Cell cell) {
        return cell != null ? cell.getStringCellValue() : null;
    }

    private JsonElement getJsonValue(Cell cell) {
        return cell != null ? JsonParser.parseString(cell.getStringCellValue()) : null;
    }

    private int getIntValue(Cell cell) {
        return cell != null ? (int)cell.getNumericCellValue() : 0;
    }

    private boolean getBooleanValue(Cell cell) {
        return cell != null ? cell.getBooleanCellValue() : false;
    }
}
