package exportexcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



class CellStyleText {

    private CellStyle styleTextCell;          // текстовые ячейки

    protected CellStyle getStyleText() {

        CellStyle style;
        style = this.styleTextCell;
        return style;

    }

    // создание и настройка стилей
    protected void createCellStyle(XSSFWorkbook dataBook) {

        // текстовые ячейки
        this.styleTextCell = dataBook.createCellStyle();
        // настройка стиля
        this.styleTextCell.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleTextCell.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание

    }




}
