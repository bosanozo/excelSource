package sample1;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.platform.commons.util.StringUtils;

import lombok.Data;

/**
 * input and expexted data for test
 */
@Data
public class TestData {
    /** test no */
    private int no;
    /** test description */
    private String description;
    /** http method */
    private String method;
    /** api path */
    private String path;
    /** input json */
    private String input;
    /** expected http status code */
    private int status;
    /** expected json array count */
    private Optional<Integer> count;
    /** expected json */
    private String expected;
    /** rusult name use in after input or path */
    private String resultName;
    /** skip test */
    private boolean skip;

    /**
     * create instance
     * 
     * @param map
     * @throws IOException
     */
    public TestData(Map<String, Cell> map) throws IOException {
        this.no = getIntValue(map.get("no"));
        this.description = getStringValue(map.get("description"));
        this.method = getStringValue(map.get("method"));
        this.path = getStringValue(map.get("path"));
        this.input = getStringValue(map.get("input"));
        this.status = getIntValue(map.get("status"));
        Optional.ofNullable(map.get("count")).ifPresent(cell -> this.count = Optional.ofNullable((int)cell.getNumericCellValue()));
        this.expected = getStringValue(map.get("expected"));
        this.resultName = getStringValue(map.get("resultname"));
        this.skip = getBooleanValue(map.get("skip"));
    }

    /** return test name. */
    public String toString() {
        return StringUtils.isNotBlank(this.description) ?
            this.description : this.method + " " + this.path;
    }

    private String getStringValue(Cell cell) {
        return cell != null ? cell.getStringCellValue() : null;
    }

    private int getIntValue(Cell cell) {
        return cell != null ? (int)cell.getNumericCellValue() : 0;
    }

    private boolean getBooleanValue(Cell cell) {
        return cell != null ? cell.getBooleanCellValue() : false;
    }
}
