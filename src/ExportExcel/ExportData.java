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
        int rowAltitude = 0;                                                                                            // счетчик строк в массивах данных
        int rowSheet = 0;                                                                                               // счетчик строк в листе excel

        float conversionAltitude;                                                                                       // вспомогательная переменная для конвертации высот
        float conversionVelocity;                                                                                       // вспомогательная переменная для конвертации скоростей

        //
        while (rowAltitude <= rowCount) {

            XSSFRow rowData = sheet.createRow(rowSheet + localVerticalOffset + globalVerticalOffset);             // создание строки, с номером rowSheet с глобальным смещением вниз на высоту шапки таблицы localVerticalOffset

            // блок высот
            for (int column = 0; column <= listData.get(0)[0].length - 1; column++) {                                   // перебор столбцов блока высот от нулевого до последнего

                conversionAltitude = listData.get(0)[rowAltitude] [listInternalOffsets.get(0)[0]];                          // установка текущей высоты в метрах

                // экспорт высоты в метрах
                if (column == listInternalOffsets.get(0)[0]) {

                    switch (unitAltitude) {
                        case 1:
                            conversionAltitude = unitConverter.getLengthKilometer(conversionAltitude);                  // конвертация из метров в километры
                            rowData.createCell(column).setCellValue(conversionAltitude);                                // создание ячейки в строке с номером rowData и номером столбца column, запись в нее элемента Altitude
                            break;
                        default:
                            rowData.createCell(column).setCellValue(conversionAltitude);                                // по умолчанию заполняется метрами
                            break;
                    }
                }

                // экспорт высоты в футах
                if (column == listInternalOffsets.get(0)[1]) {

                    conversionAltitude = unitConverter.getLengthFoot(conversionAltitude);                                 // конвертация из метров в футы
                    rowData.createCell(column).setCellValue(conversionAltitude);

                }
            }

            // блок атмосферы. экспортируется как есть, без конвертации
            for (int column = 0; column <= listData.get(1)[0].length - 1; column++) {

                rowData.createCell(column + listData.get(0)[0].length).setCellValue(listData.get(1)[rowAltitude][column]);

                // установка параметров выравнивания ячейки
                cellStyle.setCellAlignment(dataBook, rowData, column + listData.get(0)[0].length, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                // установка формата значения в ячейке
                cellStyle.setCellFormat(dataBook, rowData, column + listData.get(0)[0].length, "0.000");


            }

            // блок скорости. перебор массивов скоростей. 2 - место, с которого начинаются блоки скорости в dataList
            for (int i = 2; i < listData.size(); i++) {

                // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
                int offset = listData.get(0)[0].length + listData.get(1)[0].length + listData.get(i)[0].length * (i - 2);

                // экспорт данных из блока i
                for (int column = 0; column <= listData.get(i)[0].length - 1; column++) {

                    // вспомогательная переменная. хранит посчитанное текущеее значение взятое из блока скорости
                    conversionVelocity = listData.get(i)[rowAltitude][column];

                    // конвертация Vcas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[0]) & (conversionVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка маха на отрицательные значения
                    if ((column == listInternalOffsets.get(2)[1]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + column).setCellValue(conversionVelocity);

                        // Ma граничен сверху Mc, при этом, если Ma превышает предельное значение, он обращается в -1.
                        // Для корректного построения графика необходимо определить последнюю строку, в котрой Ma больше 0. И это только один из нескольких возможных частных случаев
                        // по этому в rowEndIndex пишутся индексы последних строк каждого из блоков скоростей
                        switch (i) {
                            case 2:
                                rowEndIndex[0] = rowSheet;
                                break;
                            case 3:
                                rowEndIndex[1] = rowSheet;
                                break;
                            case 4:
                                rowEndIndex[2] = rowSheet;
                                break;
                            case 5:
                                rowEndIndex[3] = rowSheet;
                                break;
                        }
                    }

                    // конвертация Vtas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[2]) & (conversionVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // конвертация Veas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[3]) & (conversionVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка скоростного напора на отрицательные значения
                    if ((column == listInternalOffsets.get(2)[4]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + column).setCellValue(conversionVelocity);

                    }
                }
            }

            rowAltitude = rowAltitude + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowSheet++;                                                                                                   // установка индекса следующей строки
        }

    }



}
