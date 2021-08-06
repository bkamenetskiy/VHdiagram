package ExportExcel;

import org.apache.poi.ss.usermodel.*;

public class CellsStyle {

    // настройки выравнивания для уже созданной ячейки - column
    public void setCellAlignment(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign) {

        // объект типа ячейка. column - индекс ячейки
        Cell cell = row.getCell(column);
        // объект типа стиль ячейки
        CellStyle cellStyle = wb.createCellStyle();
        // объект типа формат ячейки
        DataFormat format = wb.createDataFormat();

        // настройки стиля. настройка выравниваний
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);

        // присвоение стиля ячейке
        cell.setCellStyle(cellStyle);

    }

    // настройки формата значения в уже созданной ячейки - column
    public void setCellFormat(Workbook wb, Row row, int column, String format) {

        // объект типа ячейка. column - индекс ячейки
        Cell cell = row.getCell(column);
        // объект типа стиль ячейки
        CellStyle cellStyle = wb.createCellStyle();
        // объект типа формат ячейки
        DataFormat formatCell = wb.createDataFormat();

        // настройки стиля. настройка формата
        cellStyle.setDataFormat(formatCell.getFormat(format));

        // присвоение стиля ячейке
        cell.setCellStyle(cellStyle);

    }


    // создание ячейки с заданным выравниванием и форматом
    public void createCell(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign, float cellValue, String format1) {

        // объект типа ячейка
        Cell cell = row.createCell(column);
        // присваивание ячейки значения
        cell.setCellValue(cellValue);

        // объект типа стиль ячейки
        CellStyle cellStyle = wb.createCellStyle();

        // объект типа формат ячейки
        DataFormat format = wb.createDataFormat();

        // настройки стиля. настройка выравниваний
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setDataFormat(format.getFormat(format1));

        // присвояние стиля ячейке
        cell.setCellStyle(cellStyle);

    }

}
