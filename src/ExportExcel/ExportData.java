package exportexcel;

import enums.UnitOutput;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import solvers.SolverUnitOutput;
import java.util.ArrayList;

public class ExportData {

    public void exportData(XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<double[][]> listData, ArrayList<int[]> listInternalOffsets,
                           int globalVerticalOffset, int localVerticalOffset, int outputAltitudeInc, int rowCount, int[] rowEndIndex, UnitOutput[] unitOutput) {

        XSSFRow rowData;                                                                                                // строка
        Cell cell;                                                                                                      // ячейка
        CellStyleNumber cellStyleNumber = new CellStyleNumber();                                                        // стиль ячейки
        UnitMatching unitMatching = new UnitMatching();                                                                 // сопоставитель номера столбца и единицы измерения
        SolverUnitOutput outputUnitConvert = new SolverUnitOutput();                                                    // конвертер единиц измерения

        // вспомогательные переменные
        int rowAltitudeInc = 0;                                                                                         // счетчик строк в массивах данных
        int rowSheetInc = 0;                                                                                            // счетчик строк в листе excel
        int offset = 0;                                                                                                 // смещение блоков относительно друг друга
        double currentValue;                                                                                            // вспомогательная переменная для конвертации единиц измерения
        UnitOutput unit;                                                                                                // хранит тип единицы измерения
        CellStyle style;                                                                                                // хранит стиль ячейки

        // создание стилей ячеек
        cellStyleNumber.createCellStyle(dataBook, unitOutput);

        while (rowAltitudeInc <= rowCount) {

            // создание строки
            rowData = sheet.createRow((short) rowSheetInc + localVerticalOffset + globalVerticalOffset);

            // перебираем хранилище данных
            for (int listDataIndex = 0; listDataIndex <= listData.size() - 1; listDataIndex++) {

                // рассчет смещений блоков
                if (listDataIndex == 0) {

                    offset = 0;
                }
                else {
                    offset = listData.get(listDataIndex - 1)[0].length + offset;
                }

                // в хранилище перебираем соответствующий массив данных
                for (int column = 0; column <= listData.get(listDataIndex)[0].length - 1; column++) {

                    // задаем временной переменной текущее значение высоты
                    // условие необходимо, что бы в футы не записались нули, т.к. во время расчета высот метры в имперские единицы не переводились, и столбец заполнялся 0
                    if (listDataIndex == 0) {

                        currentValue = listData.get(listDataIndex)[rowAltitudeInc][listInternalOffsets.get(0)[0]];
                    }
                    else {
                        currentValue = listData.get(listDataIndex)[rowAltitudeInc][column];
                    }

                    // конвертация и запись в ячейку
                    // возврат единиц измерения из сопоставителя
                    unit = unitMatching.getUnit(listDataIndex, column, listInternalOffsets, unitOutput);
                    style = cellStyleNumber.getUnitStyle(unit);

                    // экспорт. отрицательные высоты и параметры атмосферы экспортируются
                    if (listDataIndex < 2) {

                        //rowData.createCell(column + offset).setCellValue(outputUnitConvert.getUnitOutput(unit, currentValue)); - старая версия
                        cell = rowData.createCell((short) column + offset);
                        cell.setCellValue(Precision.round(outputUnitConvert.getUnitOutput(unit, currentValue), unit.getUnitPrecision()));  // присвоение ячейке округленного значения
                        cell.setCellStyle(style);                                                                                           // присвоение ячейки ей соответствующего стиля
                    }


                    // а вот отрицательные скорости - нет.
                    if ((listDataIndex >= 2) & (currentValue >= 0.0)){

                        cell = rowData.createCell((short) column + offset);
                        cell.setCellValue(Precision.round(outputUnitConvert.getUnitOutput(unit, currentValue), unit.getUnitPrecision()));
                        cell.setCellStyle(style);
                    }
                    // вместо них создается пустая ячейка
                    if ((listDataIndex >= 2) & (currentValue <= 0.0)){

                        rowData.createCell((short) column + offset).setCellValue("");
                    }

                    // Ma граничен сверху Mc, при этом, если Ma превышает предельное значение, он обращается в -1.
                    // И это только один из нескольких частных случаев, возможных при рассчетах.
                    // Для корректного построения графика необходимо определить последнюю строку, в котрой Ma больше 0.
                    // по этому в rowEndIndex пишутся индексы последних строк каждого из блоков скоростей
                    if ((currentValue > 0.0) & (listDataIndex >= 2)){

                        rowEndIndex[listDataIndex - 2] = rowSheetInc;
                    }
                }
            }
            rowAltitudeInc = rowAltitudeInc + outputAltitudeInc;                                                        // установка значения следующей высоты для вывода
            rowSheetInc++;                                                                                              // установка индекса следующей строки
        }
    }
}
