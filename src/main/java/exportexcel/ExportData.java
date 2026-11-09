package exportexcel;

import enums.UnitInput;
import enums.UnitOutput;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import solvers.SolverUnitConverter;
import java.util.ArrayList;

public class ExportData {

    /**
     * Записывает расчётные данные в выбранных единицах и запоминает последние строки рядов скоростей.
     * Номера столбцов, единицы и стили подготавливаются перед записью строк.
     */
    public void exportData(XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<double[][]> listData, ArrayList<int[]> listInternalOffsets,
                           int globalVerticalOffset, int localVerticalOffset, double outputAltitudeInc, int rowCount, int[] rowEndIndex,
                           UnitOutput[] unitOutput, SolverUnitConverter unitConverter, UnitInput[] unitInput) {

        XSSFRow rowData;                                                                                                // строка
        Cell cell;                                                                                                      // ячейка
        CellStyleNumber cellStyleNumber = new CellStyleNumber();                                                        // стиль ячейки
        UnitMatching unitMatching = new UnitMatching();                                                                 // сопоставитель номера столбца и единицы измерения

        // вспомогательные переменные
        int rowDataArray = 0;                                                                                           // счетчик строк в массивах данных
        int rowSheetInc = 0;                                                                                            // счетчик строк в листе excel
        // создание стилей ячеек
        cellStyleNumber.createCellStyle(dataBook, unitOutput);

        // Параметры столбцов хранятся по индексу блока и номеру столбца внутри него.
        int blockCount = listData.size(); // Количество блоков расчётных данных.
        int[][] sheetColumns = new int[blockCount][]; // Номера столбцов на листе Excel.
        UnitOutput[][] columnUnits = new UnitOutput[blockCount][]; // Единицы измерения для экспорта.
        CellStyle[][] columnStyles = new CellStyle[blockCount][]; // Стили ячеек каждого столбца.
        int blockOffset = 0; // Номер первого столбца текущего блока на листе Excel.
        // Для каждого блока выделяем массивы под параметры его столбцов.
        for (int block = 0; block < blockCount; block++) {
            int columnCount = listData.get(block)[0].length;
            sheetColumns[block] = new int[columnCount];
            columnUnits[block] = new UnitOutput[columnCount];
            columnStyles[block] = new CellStyle[columnCount];
            // Для каждого столбца определяем номер на листе, единицу и стиль для всех строк.
            for (int column = 0; column < columnCount; column++) {
                UnitOutput unit = unitMatching.getUnit(block, column, listInternalOffsets, unitOutput);
                sheetColumns[block][column] = blockOffset + column;
                columnUnits[block][column] = unit;
                columnStyles[block][column] = cellStyleNumber.getUnitStyle(unit);
            }
            // В блоке высот вспомогательный столбец пропускается при экспорте.
            blockOffset += block == 0 ? columnCount - 1 : columnCount;
        }

        if (unitInput[0] == UnitInput.Kilometer) {
            outputAltitudeInc = outputAltitudeInc * 1000.0;
        }

        // до тех пор, пока текущее значение высоты во вспомогательном столбце меньше чем максимальное значение в этом же столбце, выполняется цикл
        while (listData.get(0)[rowDataArray][listInternalOffsets.get(0)[2]] <= listData.get(0)[listData.get(0).length-1][listInternalOffsets.get(0)[2]]) {

            //System.out.println(listData.get(0)[rowDataArray][listInternalOffsets.get(0)[2]] +" "+ listData.get(0)[listData.get(0).length-1][listInternalOffsets.get(0)[2]]);

            // создание строки
            rowData = sheet.createRow((short) rowSheetInc + localVerticalOffset + globalVerticalOffset);

            // Берём текущую строку каждого блока и используем готовые параметры её столбцов.
            for (int listDataIndex = 0; listDataIndex < blockCount; listDataIndex++) {
                double[] values = listData.get(listDataIndex)[rowDataArray];

                for (int column = 0; column < sheetColumns[listDataIndex].length; column++) {
                    double currentValue = values[column];
                    int sheetColumn = sheetColumns[listDataIndex][column];
                    UnitOutput unit = columnUnits[listDataIndex][column];
                    CellStyle style = columnStyles[listDataIndex][column];

                    // экспорт. отрицательные высоты и параметры атмосферы экспортируются
                    // блок высот. "вспомогательный" столбец экспортироваться не должен.
                    // высоты выводятся следующим образом: футы всегда выводятся как есть, т.к. их не во что конвертировать;
                    // метры выводятся в зависимости от настроек пользователя - либо как есть, либо конвертируются в километры
                    if ((listDataIndex == 0) && (column != listInternalOffsets.get(0)[2])) {

                        if (column == listInternalOffsets.get(0)[0]) {
                            currentValue = unitConverter.getSIToUnitOutput(unit, currentValue);
                        }
                        cell = rowData.createCell(sheetColumn);
                        cell.setCellValue(Precision.round(currentValue, unit.getUnitPrecision()));  // присвоение ячейке округленного значения
                        cell.setCellStyle(style);                                                                                           // присвоение ячейки ей соответствующего стиля
                    }

                    // Блок параметров атмосферы.
                    if (listDataIndex == 1) {

                        cell = rowData.createCell(sheetColumn);
                        cell.setCellValue(Precision.round(unitConverter.getSIToUnitOutput(unit, currentValue), unit.getUnitPrecision()));  // присвоение ячейке округленного значения
                        cell.setCellStyle(style);                                                                                           // присвоение ячейки ей соответствующего стиля
                    }


                    // а вот отрицательные скорости не экспортируются.
                    if ((listDataIndex >= 2) & (currentValue >= 0.0)){

                        cell = rowData.createCell(sheetColumn);
                        cell.setCellValue(Precision.round(unitConverter.getSIToUnitOutput(unit, currentValue), unit.getUnitPrecision()));
                        cell.setCellStyle(style);
                    }
                    // вместо них создается пустая ячейка
                    if ((listDataIndex >= 2) & (currentValue < 0.0)){

                        rowData.createCell(sheetColumn).setCellValue("");
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
