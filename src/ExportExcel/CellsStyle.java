package ExportExcel;

import org.apache.poi.ss.usermodel.*;

class CellsStyle {

    private Cell cell;
    private CellStyle cellStyle;
    private DataFormat formatCell;


    // настройки выравнивания для уже созданной ячейки - column
    protected void setCellAlignment(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign) {

        // ячейка. column - индекс ячейки
        this.cell = row.getCell(column);
        // стиль ячейки
        this.cellStyle = wb.createCellStyle();

        // настройки стиля. настройка выравниваний
        this.cellStyle.setAlignment(halign);
        this.cellStyle.setVerticalAlignment(valign);

        // присвоение стиля ячейке
        this.cell.setCellStyle(this.cellStyle);

    }

    // настройки формата значения в уже созданной ячейке
    protected void setCellFormat(Workbook wb, Row row, int column, String format) {

        // ячейка. column - индекс ячейки
        this.cell = row.getCell(column);
        // стиль ячейки
        this.cellStyle = wb.createCellStyle();
        // формат ячейки
        this.formatCell = wb.createDataFormat();

        // настройки стиля. настройка формата
        this.cellStyle.setDataFormat(this.formatCell.getFormat(format));

        // присвоение стиля ячейке
        this.cell.setCellStyle(this.cellStyle);

    }

    // создание ячейки с заданным выравниванием и настройками разрядности для числа
    protected void createCell(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign, float cellValue, String format) {

        // ячейка
        this.cell = row.createCell(column);
        // присваивание ячейки значения
        this.cell.setCellValue(cellValue);
        // стиль ячейки
        this.cellStyle = wb.createCellStyle();
        // формат ячейки
        this.formatCell = wb.createDataFormat();

        // настройки стиля. настройка выравниваний
        this.cellStyle.setAlignment(halign);
        this.cellStyle.setVerticalAlignment(valign);
        this.cellStyle.setDataFormat(this.formatCell.getFormat(format));

        // присвоение стиля ячейке
        this.cell.setCellStyle(this.cellStyle);
    }

    // создание ячейки с заданным выравниванием (для строки)
    protected void createCell(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign, float cellValue) {

        // ячейка
        this.cell = row.createCell(column);
        // присваивание ячейки значения
        this.cell.setCellValue(cellValue);
        // стиль ячейки
        this.cellStyle = wb.createCellStyle();
        // формат ячейки
        this.formatCell = wb.createDataFormat();

        // настройки стиля. настройка выравниваний
        this.cellStyle.setAlignment(halign);
        this.cellStyle.setVerticalAlignment(valign);

        // присвоение стиля ячейке
        this.cell.setCellStyle(this.cellStyle);
    }


}
