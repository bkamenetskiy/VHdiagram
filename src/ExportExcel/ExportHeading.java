package exportexcel;

import enums.UnitOutput;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.util.ArrayList;

public class ExportHeading {

    public void exportHeading (XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<double[][]> listData, ArrayList<int[]> listInternalOffsets,
                               int globalVerticalOffset, UnitOutput[] unitOutput) {

        XSSFRow rowData;                                                                                                // строка
        Cell cell;                                                                                                      // ячейка
        CellStyleText cellStyleText = new CellStyleText();                                                              // стиль ячейки
        UnitMatching unitMatching = new UnitMatching();                                                                 // сопоставитель номера столбца и единицы измерения

        // вспомогательные переменные
        int rowName = 0 + globalVerticalOffset;                                                                         // номер строки с названием блока
        int rowType = 1 + globalVerticalOffset;                                                                         // номер строки с названием (типом) величины
        int rowUnit = 2 + globalVerticalOffset;                                                                         // номер строки с единицами измерения величины
        int[] rowIndex = new int[] {rowName, rowType, rowUnit};
        int offset = 0;                                                                                                 // смещение блоков относительно друг друга
        int length;                                                                                                     // для расчета длинн блоков (слияние ячеек)
        UnitOutput unit;                                                                                                // хранит тип единицы измерения

        // создание стилей
        cellStyleText.createCellStyle(dataBook);

        for (int row = 0; row <= rowIndex.length - 1; row++) {

            // создаем строку
            rowData = sheet.createRow(rowIndex[row]);

            // хозяйничаем в строке
            // перебираем блоки
            for (int listDataIndex = 0; listDataIndex <= listData.size() - 1; listDataIndex++) {

                // рассчитываем смещения
                int v = 0;
                if (listDataIndex == 0) {
                    offset = 0;
                    v = 0;
                }

                if (listDataIndex >= 1) {
                    offset = offset + listData.get(listDataIndex - 1)[0].length;
                    v = offset - 1 ;
                }

                length = listData.get(listDataIndex)[0].length;

                // заполняем первую строку названиями блоков
                if (row == 0) {


                    // создание ячейки и форматирование
                    if (listDataIndex == 0) {
                        cell = rowData.createCell(offset);
                    }
                    else {
                        cell = rowData.createCell(offset - 1);
                    }
                    cell.setCellStyle(cellStyleText.getStyleText());


                    // заголовок
                    cell.setCellValue(blockType(listDataIndex));

                    // слияние ячеек
                    // в блоке высоты надо, помимо ячеек, объединить первую и вторую строку
                    if (listDataIndex == 0) {
                        sheet.addMergedRegion(new CellRangeAddress(rowName, rowType, offset, offset + length - 1 - 1));
                    }
                    else {
                        sheet.addMergedRegion(new CellRangeAddress(rowName, rowName, offset - 1, offset - 1 + length - 1));
                    }
                }

                // заполняем вторую строку названием величин
                if (row == 1) {

                    // роемся по столбцам внутри блока
                    for (int column = 0; column <= listData.get(listDataIndex)[0].length - 1; column++) {

                        // создание ячейки, форматирование, задание значения
                        if (listDataIndex == 0) {
                            cell = rowData.createCell(offset + column);
                        }
                        else {
                            cell = rowData.createCell(offset - 1 + column);
                        }

                        cell.setCellStyle(cellStyleText.getStyleText());
                        cell.setCellValue(getType(listInternalOffsets, listDataIndex, column));
                    }
                }

                // заполняем третью строку единицами измерения
                if (row == 2) {

                    // роемся по столбцам внутри блока
                    for (int column = 0; column <= listData.get(listDataIndex)[0].length - 1; column++) {

                        // возврат единиц измерения

                        unit = unitMatching.getUnit(listDataIndex , column, listInternalOffsets, unitOutput);

                        // создание ячейки, форматирование, задание значения
                        if (listDataIndex == 0) {
                            cell = rowData.createCell(offset + column);
                        }
                        else {
                            cell = rowData.createCell(offset + column - 1);
                        }

                        cell.setCellStyle(cellStyleText.getStyleText());
                        cell.setCellValue(unit.getUnitName());
                    }
                }

            }
        }
    }

    // сопоставитель
    private String getType (ArrayList<int[]> listInternalOffsets, int listDataIndex, int column) {

        String name = "";

        switch (listDataIndex) {

            // блок высот
            case 0:

                break;

            // блок атмосферы
            case 1:

                if (column == listInternalOffsets.get(1)[0]) {
                    name = "Плотность";
                }

                if (column == listInternalOffsets.get(1)[1]) {
                    name = "Атм. давл.";
                }

                if (column == listInternalOffsets.get(1)[2]) {
                    name = "Температура";
                }

                if (column == listInternalOffsets.get(1)[3]) {
                    name = "Скорость звука";
                }
                break;

            // блоки скоростей
            default:

                if (column == listInternalOffsets.get(2)[0]) {
                    name = "Vcas";
                }

                if (column == listInternalOffsets.get(2)[1]) {
                    name = "M";
                }

                if (column == listInternalOffsets.get(2)[2]) {
                    name = "Vtas";
                }

                if (column == listInternalOffsets.get(2)[3]) {
                    name = "Veas";
                }

                if (column == listInternalOffsets.get(2)[4]) {
                    name = "q";
                }
                break;

        }
        return name;
    }

    private String blockType (int name) {

        switch (name) {
            case 0:
                return "Высота";
            case 1:
                return "Параметры атмосферы";
            case 2:
                return "Vd";
            case 3:
                return "Vc";
            case 4:
                return "Va";
            case 5:
                return "Vs";
            default:
                return "Неизвестный тип";
        }
    }




}
