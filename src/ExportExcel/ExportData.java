package exportexcel;

import enums.UnitInput;
import enums.UnitOutput;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import solvers.SolverUnitConverter;
import java.util.ArrayList;

public class ExportData {

    public void exportData(XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<double[][]> listData, ArrayList<int[]> listInternalOffsets,
                           int globalVerticalOffset, int localVerticalOffset, double outputAltitudeInc, int rowCount, int[] rowEndIndex,
                           UnitOutput[] unitOutput, SolverUnitConverter unitConverter, UnitInput[] unitInput) {

        XSSFRow rowData;                                                                                                // строка
        Cell cell;                                                                                                      // ячейка
        CellStyleNumber cellStyleNumber = new CellStyleNumber();                                                        // стиль ячейки
        UnitMatching unitMatching = new UnitMatching();                                                                 // сопоставитель номера столбца и единицы измерения

        // вспомогательные переменные
        int rowDataArray = 0;                                                                                         // счетчик строк в массивах данных
        int rowSheetInc = 0;                                                                                            // счетчик строк в листе excel
        int offset = 0;                                                                                                 // смещение блоков относительно друг друга
        double currentValue;                                                                                            // вспомогательная переменная для конвертации единиц измерения
        UnitOutput unit;                                                                                                // хранит тип единицы измерения
        CellStyle style;                                                                                                // хранит стиль ячейки

        // создание стилей ячеек
        cellStyleNumber.createCellStyle(dataBook, unitOutput);

        if (unitInput[0] == UnitInput.Kilometer) {
            outputAltitudeInc = outputAltitudeInc * 1000.0;
        }

        // до тех пор, пока текущее значение высоты во вспомогательном столбце меньше чем максимальное значение в этом же столбце, выполняется цикл
        while (listData.get(0)[rowDataArray][listInternalOffsets.get(0)[2]] <= listData.get(0)[listData.get(0).length-1][listInternalOffsets.get(0)[2]]) {

            //System.out.println(listData.get(0)[rowDataArray][listInternalOffsets.get(0)[2]] +" "+ listData.get(0)[listData.get(0).length-1][listInternalOffsets.get(0)[2]]);

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

                    // задаем временной переменной текущее значение
                    currentValue = listData.get(listDataIndex)[rowDataArray][column];

                    // конвертация и запись в ячейку
                    // возврат единиц измерения и стилей из сопоставителей
                    unit = unitMatching.getUnit(listDataIndex, column, listInternalOffsets, unitOutput);
                    style = cellStyleNumber.getUnitStyle(unit);

                    // экспорт. отрицательные высоты и параметры атмосферы экспортируются
                    // блок высот. "вспомогательный" столбец экспортироваться не должен.
                    // высоты выводятся следующим образом: футы всегда выводятся как есть, т.к. их не во что конвертировать;
                    // метры выводятся в зависимости от настроек пользователя - либо как есть, либо конвертируются в километры
                    if ((listDataIndex == 0) && (column != listInternalOffsets.get(0)[2])) {

                        cell = rowData.createCell(column + offset);
                        cell.setCellValue(Precision.round(currentValue, unit.getUnitPrecision()));  // присвоение ячейке округленного значения
                        cell.setCellStyle(style);                                                                                           // присвоение ячейки ей соответствующего стиля
                    }

                    // блок параметров атмосферы. здесь и далее в индексе строки введено смещение в лево -1 т.к. в блоке высот есть лишний столбец, который учитывается при рассчете offset
                    if (listDataIndex == 1) {

                        //rowData.createCell(column + offset).setCellValue(outputUnitConvert.getUnitOutput(unit, currentValue)); - старая версия
                        cell = rowData.createCell(column + offset - 1);
                        cell.setCellValue(Precision.round(unitConverter.getSIToUnitOutput(unit, currentValue), unit.getUnitPrecision()));  // присвоение ячейке округленного значения
                        cell.setCellStyle(style);                                                                                           // присвоение ячейки ей соответствующего стиля
                    }


                    // а вот отрицательные скорости не экспортируются.
                    if ((listDataIndex >= 2) & (currentValue >= 0.0)){

                        cell = rowData.createCell(column + offset - 1);
                        cell.setCellValue(Precision.round(unitConverter.getSIToUnitOutput(unit, currentValue), unit.getUnitPrecision()));
                        cell.setCellStyle(style);
                    }
                    // вместо них создается пустая ячейка
                    if ((listDataIndex >= 2) & (currentValue <= 0.0)){

                        rowData.createCell(column + offset - 1).setCellValue("");
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

            // по достижении максимального значения цикл должен быть прерван чтобы не добавить лишние инкременты и не вылететь с ошибкой
            if (listData.get(0)[rowDataArray][listInternalOffsets.get(0)[2]] == listData.get(0)[listData.get(0).length-1][listInternalOffsets.get(0)[2]]) {
                break;
            }

            rowDataArray = rowDataArray + (int) outputAltitudeInc;                                                        // установка значения следующей высоты для вывода
            rowSheetInc++;                                                                                              // установка индекса следующей строки
        }
    }
}
