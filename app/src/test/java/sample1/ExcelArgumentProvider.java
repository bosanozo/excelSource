package sample1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

class ExcelArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ExcelSource> {
    private String fileName;
    private String sheetName;

    @Override
    public void accept(ExcelSource t) {
        this.fileName = t.file();
        this.sheetName = t.sheet();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        // return Stream.of("foo", "bar").map(Arguments::of);
        Workbook workbook = WorkbookFactory.create(new File(this.fileName));
        Sheet sheet = workbook.getSheet(this.sheetName);

        List<TestData> list = new ArrayList<TestData>();
        int lastRowNum = sheet.getLastRowNum();

        if (lastRowNum > 1) {
            Row headerRow = sheet.getRow(0);
            int lastCellNum = headerRow.getLastCellNum();
            List<String> headerNames = new ArrayList<String>();
            for (int i = 0; i < lastCellNum; i++) {
                headerNames.add(headerRow.getCell(i).getStringCellValue());
            }
            
            for (int i = 1; i < lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) break;
                Map<String, Cell> map = new HashMap<String, Cell>();
                for (int j = 0; j < lastCellNum; j++) {
                    map.put(headerNames.get(j), row.getCell(j));
                }
                TestData data = new TestData(map);
                list.add(data);
            }
        }
        return list.stream().map(Arguments::of);
    }    
}