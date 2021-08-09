package ExportExcel;
import models.ModelUnits;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExportData {


    public void exportData(XSSFSheet sheet, ArrayList<float[][]> listData, ArrayList <int[]> listInternalOffsets,
                           int unitAltitude, int unitVelocity, int globalVerticalOffset,
                           int localVerticalOffset, int outputAltitudeInc, ModelUnits unitConverter, int rowCount, int[] rowEndIndex, XSSFWorkbook dataBook) {

        CellsStyle cellStyle = new CellsStyle();

        // экспорт данных
        int rowAltitudeInc = 0;                                                                                         // счетчик строк в массивах данных
        int rowSheetInc = 0;                                                                                            // счетчик строк в листе excel

        float currentValueAltitude;                                                                                     // вспомогательная переменная для конвертации высот
        float currentValueVelocity;                                                                                     // вспомогательная переменная для конвертации скоростей

        //
        while (rowAltitudeInc <= rowCount) {

            XSSFRow rowData = sheet.createRow(rowSheetInc + localVerticalOffset + globalVerticalOffset);        // создание строки, с номером rowSheetInc с глобальным смещением вниз на высоту шапки таблицы localVerticalOffset

            // блок высот
            for (int column = 0; column <= listData.get(0)[0].length - 1; column++) {                                   // перебор столбцов блока высот от нулевого до последнего

                currentValueAltitude = listData.get(0)[rowAltitudeInc] [listInternalOffsets.get(0)[0]];                 // установка текущей высоты в метрах

                // экспорт высоты в метрах
                if (column == listInternalOffsets.get(0)[0]) {

                    switch (unitAltitude) {
                        case 1:                                                                                         // конвертация из метров в километры
                            currentValueAltitude = unitConverter.getLengthKilometer(currentValueAltitude);
                            cellStyle.createCell(dataBook, rowData, column, HorizontalAlignment.CENTER,
                                                    VerticalAlignment.CENTER, currentValueAltitude, "0.000");
                            break;
                        default:                                                                                        // по умолчанию заполняется метрами
                            cellStyle.createCell(dataBook, rowData, column, HorizontalAlignment.CENTER,
                                                    VerticalAlignment.CENTER, currentValueAltitude, "0.0");
                            break;
                    }
                }

                // экспорт высоты в футах
                if (column == listInternalOffsets.get(0)[1]) {

                    currentValueAltitude = unitConverter.getLengthFoot(currentValueAltitude);                           // конвертация из метров в футы
                    cellStyle.createCell(dataBook, rowData, column, HorizontalAlignment.CENTER,
                                            VerticalAlignment.CENTER, currentValueAltitude, "0.0");

                }
            }

            // блок атмосферы. экспортируется как есть, без конвертации
            for (int column = 0; column <= listData.get(1)[0].length - 1; column++) {

                // экспорт плотности
                if (column == listInternalOffsets.get(1)[0]) {

                    cellStyle.createCell(dataBook, rowData, column + listData.get(0)[0].length, HorizontalAlignment.CENTER,
                                            VerticalAlignment.CENTER, listData.get(1)[rowAltitudeInc][column], "0.0000");
                }

                // экспорт атмосферного давления
                if (column == listInternalOffsets.get(1)[1]) {

                    cellStyle.createCell(dataBook, rowData, column + listData.get(0)[0].length, HorizontalAlignment.CENTER,
                                            VerticalAlignment.CENTER, listData.get(1)[rowAltitudeInc][column], "0.0");
                }

                // экспорт температуры
                if (column == listInternalOffsets.get(1)[2]) {

                    cellStyle.createCell(dataBook, rowData, column + listData.get(0)[0].length, HorizontalAlignment.CENTER,
                                            VerticalAlignment.CENTER, listData.get(1)[rowAltitudeInc][column], "0.0");
                }

                // экспорт скорости звука
                if (column == listInternalOffsets.get(1)[3]) {

                    cellStyle.createCell(dataBook, rowData, column + listData.get(0)[0].length, HorizontalAlignment.CENTER,
                                            VerticalAlignment.CENTER, listData.get(1)[rowAltitudeInc][column], "0.00");
                }
                // старый вариант
                //rowData.createCell(column + listData.get(0)[0].length).setCellValue(listData.get(1)[rowAltitudeInc][column]);
            }

            // блок скорости. перебор массивов скоростей. 2 - место, с которого начинаются скорости в dataList
            for (int i = 2; i < listData.size(); i++) {

                // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
                int offset = listData.get(0)[0].length + listData.get(1)[0].length + listData.get(i)[0].length * (i - 2);

                // экспорт данных из блока i
                for (int column = 0; column <= listData.get(i)[0].length - 1; column++) {

                    // вспомогательная переменная. хранит значение взятое из блока скорости до конвертации
                    currentValueVelocity = listData.get(i)[rowAltitudeInc][column];

                    // конвертация Vcas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[0]) & (currentValueVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:                                                                                     // метры. не конвертируется
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, currentValueVelocity, "0.0000");
                                break;
                            case 1:                                                                                     // конвертация м/с в км/ч
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, unitConverter.getVelocityKm(currentValueVelocity), "0.0");
                                break;
                            case 2:                                                                                     // конвертация м/с в узлы
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, unitConverter.getVelocityKt(currentValueVelocity), "0.000");
                                break;
                        }
                    }

                    // проверка маха на отрицательные значения
                    if ((column == listInternalOffsets.get(2)[1]) & (currentValueVelocity >= 0.0f)) {

                        cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                VerticalAlignment.CENTER, currentValueVelocity, "0.000");

                        // Ma граничен сверху Mc, при этом, если Ma превышает предельное значение, он обращается в -1. И это только один из нескольких возможных частных случаев, возможных при рассчетах
                        // Для корректного построения графика необходимо определить последнюю строку, в котрой Ma больше 0.
                        // по этому в rowEndIndex пишутся индексы последних строк каждого из блоков скоростей
                        switch (i) {
                            case 2:
                                rowEndIndex[0] = rowSheetInc;
                                break;
                            case 3:
                                rowEndIndex[1] = rowSheetInc;
                                break;
                            case 4:
                                rowEndIndex[2] = rowSheetInc;
                                break;
                            case 5:
                                rowEndIndex[3] = rowSheetInc;
                                break;
                        }
                    }

                    // конвертация Vtas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[2]) & (currentValueVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:                                                                                     // метры. не конвертируется
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, currentValueVelocity, "0.0000");
                                break;
                            case 1:                                                                                     // конвертация м/с в км/ч
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, unitConverter.getVelocityKm(currentValueVelocity), "0.0");
                                break;
                            case 2:                                                                                     // конвертация м/с в узлы
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, unitConverter.getVelocityKt(currentValueVelocity), "0.000");
                                break;
                        }
                    }

                    // конвертация Veas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[3]) & (currentValueVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:                                                                                     // метры. не конвертируется
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, currentValueVelocity, "0.0000");
                                break;
                            case 1:                                                                                     // конвертация м/с в км/ч
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, unitConverter.getVelocityKm(currentValueVelocity), "0.0");
                                break;
                            case 2:                                                                                     // конвертация м/с в узлы
                                cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                        VerticalAlignment.CENTER, unitConverter.getVelocityKt(currentValueVelocity), "0.000");
                                break;
                        }
                    }

                    // проверка скоростного напора на отрицательные значения
                    if ((column == listInternalOffsets.get(2)[4]) & (currentValueVelocity >= 0.0f)) {

                        cellStyle.createCell(dataBook, rowData, offset + column, HorizontalAlignment.CENTER,
                                                VerticalAlignment.CENTER, unitConverter.getVelocityKt(currentValueVelocity), "0.0");

                    }
                }
            }

            rowAltitudeInc = rowAltitudeInc + outputAltitudeInc;                                                              // установка значения следующей высоты для вывода
            rowSheetInc++;                                                                                                 // установка индекса следующей строки
        }

    }



}
