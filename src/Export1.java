import  java.io.*;

import models.ModelUnits;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Export1 {

    // сокровищница
    private ArrayList <int[]> listSettings;                                                                              // хранилище общих настроек
    private ArrayList <float [][]> listData;                                                                            // хранилище данных
    private ArrayList <int[]> listInternalOffsets;



    // создание конвертера единиц измерения
    private ModelUnits unitConvert = new ModelUnits();
    // создание книги
    private XSSFWorkbook dataFile = new XSSFWorkbook();
    // создание листа
    private XSSFSheet sheet = this.dataFile.createSheet("Result");


    private int unitAltitude = 1; // 1 - километры; любой другой ключ - метры
    private int unitVelocity = 0; // 0 - м/с; 1 - км/ч; 2 - knot


    public void exportData(int outputAltitudeInc) throws IOException {

        // экспорт данных
        int initAlt = 0; // счетчик текущей высоты (в метрах) = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc
        int rowInc = 0; // счетчик строк
        int deltaHeight = 2; // смещение таблицы с данными по вертикали = высота шапки таблицы

        float conversionAltitude;                                                                                       // вспомогательная переменная для конвертации высот
        float conversionVelocity;                                                                                       // вспомогательная переменная для конвертации скоростей

        // экспорт работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая хранится в последней строке [serviceList.get(0)[0]], в метрах [this.serviceList.get(1)[0]]) массива с данными this.listData.get(0)
        while (initAlt <= (int) this.listData.get(0)[this.listSettings.get(0)[0]] [this.listInternalOffsets.get(0)[0]]) {

            XSSFRow rowData = this.sheet.createRow(rowInc + deltaHeight);                                       // создание строки, с номером rowInc с глобальным смещением вниз на высоту шапки таблицы deltaHeight

            // блок высот
            for (int column = 0; column <= this.listData.get(0)[0].length - 1; column++) {                              // перебор столбцов блока высот от нулевого до последнего

                conversionAltitude = this.listData.get(0)[initAlt] [this.listInternalOffsets.get(0)[0]];                // установка текущей высоты в метрах

                // экспорт высоты в метрах
                if (column == this.listInternalOffsets.get(0)[0]) {

                    switch (this.unitAltitude) {
                        case 1:
                            conversionAltitude = unitConvert.getLengthKilometer(conversionAltitude);                    // конвертация из метров в километры
                            rowData.createCell(column).setCellValue(conversionAltitude);        // создание ячейки в строке с номером rowData и номером столбца column, запись в нее элемента Altitude
                            break;
                        default:
                            rowData.createCell(column).setCellValue(conversionAltitude);        // по умолчанию заполняется метрами
                            break;
                    }
                }

                // экспорт высоты в футах
                if (column == this.listInternalOffsets.get(0)[1]) {

                    conversionAltitude = unitConvert.getLengthFoot(conversionAltitude);                                 // конвертация из метров в футы
                    rowData.createCell(column).setCellValue(conversionAltitude);

                }
            }

            // блок атмосферы. экспортируется как есть, без конвертации
            for (int column = 0; column <= this.listData.get(1)[0].length - 1; column++) {

                rowData.createCell(column + this.listData.get(0)[0].length).setCellValue(this.listData.get(1)[initAlt][column]);

            }

            // блок скорости. перебор массивов скоростей. 2 - место, с которого начинаются блоки скорости в dataList
            for (int i = 2; i < this.listData.size(); i++) {

                // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
                int offset = this.listData.get(0)[0].length + this.listData.get(1)[0].length + this.listData.get(i)[0].length * (i - 2);

                // экспорт данных из блока i
                for (int column = 0; column <= this.listData.get(i)[0].length - 1; column++) {

                    // вспомогательная переменная. хранит посчитанное текущеее значение взятое из блока скорости
                    conversionVelocity = this.listData.get(i)[initAlt][column];

                    // конвертация Vcas и отсечение отрицательных значений
                    if ((column == this.listInternalOffsets.get(2)[0]) & (conversionVelocity >= 0.0f)) {

                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка маха на отрицательные значения
                    if ((column == this.listInternalOffsets.get(2)[1]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + column).setCellValue(conversionVelocity);

                    }

                    // конвертация Vtas и отсечение отрицательных значений
                    if ((column == this.listInternalOffsets.get(2)[2]) & (conversionVelocity >= 0.0f)) {

                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // конвертация Veas и отсечение отрицательных значений
                    if ((column == this.listInternalOffsets.get(2)[3]) & (conversionVelocity >= 0.0f)) {

                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка скоростного напора на отрицательные значения
                    if ((column == this.listInternalOffsets.get(2)[4]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + column).setCellValue(conversionVelocity);

                    }
                }
            }

            initAlt = initAlt + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка координаты следующей строки
        }


        exportHeading();

        writeFile(this.dataFile);
        System.out.println("Your excel file has been generated!");


        float abc =  this.listData.get(0)[10][0];
        abc = unitConvert.getLengthKilometer(abc);
        System.out.println(abc);


    }



    public void exportHeading (){

        String[] headingTypeAtmParam = new String[] {"Плотность", "Атм. давление", "Температура", "Скорость звука"};
        String[] headingTypeVelocity = new String[] {"Vcas", "M", "Vtas", "Veas", "q"};

        String[] headingUnitVelocity = new String[] {"м/с", "км/ч", "knot"};
        String[] headingUnitLength = new String[] {"м", "км", "фут"};
        String[] headingUnitPressure = new String[] {"Па", "кг/см^2"};
        String[] headingUnitTemp = new String[] {"K", "C", "F"};

        XSSFRow rowHeading;                                                                                             // создание строки, с номером row с глобальным смещением вниз на высоту шапки таблицы deltaHeight
        this.sheet.createRow(0);
        this.sheet.createRow(1);

        // шапка блока высот
        // название
        rowHeading = this.sheet.getRow(0);
        rowHeading.createCell(0).setCellValue("Высота");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));

        for (int column = 0; column <= this.listData.get(0)[0].length - 1; column++) {

            // высота в метрах
            if (column == this.listInternalOffsets.get(0)[0]) {

                // единицы измерения
                switch (this.unitAltitude) {
                    case 1:
                        rowHeading = this.sheet.getRow(1);
                        rowHeading.createCell(column).setCellValue(headingUnitLength[1]);
                        break;
                    default:
                        rowHeading = this.sheet.getRow(1);
                        rowHeading.createCell(column).setCellValue(headingUnitLength[0]);
                        break;
                }
            }

            // высота в футах
            if (column == this.listInternalOffsets.get(0)[1]) {

                // единицы измерения
                rowHeading = this.sheet.getRow(1);
                rowHeading.createCell(column).setCellValue(headingUnitLength[2]);

            }
        }




        // шапка блока параметров атмосферы
        for (int column = 0; column <= this.listData.get(1)[0].length - 1; column++) {

            // заголовок плотности
            if (column == this.listInternalOffsets.get(1)[0]) {

                // название
                rowHeading = this.sheet.getRow(0);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingTypeAtmParam[0]);

                // единицы измерения
                rowHeading = this.sheet.getRow(1);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue("кг/м^3");

            }

            // заголовок атмосферного давления
            if (column == this.listInternalOffsets.get(1)[1]) {

                // название
                rowHeading = this.sheet.getRow(0);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingTypeAtmParam[1]);

                // единицы измерения
                rowHeading = this.sheet.getRow(1);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingUnitPressure[0]);

            }

            // заголовок температуры
            if (column == this.listInternalOffsets.get(1)[2]) {

                // название
                rowHeading = this.sheet.getRow(0);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingTypeAtmParam[2]);

                // единицы измерения
                rowHeading = this.sheet.getRow(1);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingUnitTemp[0]);

            }

            // заголовок скорости звука
            if (column == this.listInternalOffsets.get(1)[3]) {

                // название
                rowHeading = this.sheet.getRow(0);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingTypeAtmParam[3]);

                // единицы измерения
                rowHeading = this.sheet.getRow(1);
                rowHeading.createCell(column + this.listData.get(0)[0].length).setCellValue(headingUnitVelocity[0]);

            }
        }

            // шапка блока скоростей










       // }





    }



    // запись файла на диск
    private void writeFile (XSSFWorkbook file) throws IOException {
        FileOutputStream out = new FileOutputStream("d:\\Data.xlsx");
        file.write(out);
        out.close();
        file.close();
    }


    public void setListSettings(ArrayList<int[]> listSettings) {
        this.listSettings = listSettings;
    }

    public void setListData(ArrayList<float[][]> listData) {
        this.listData = listData;
    }

    public void setListInternalOffsets(ArrayList<int[]> listInternalOffsets) {
        this.listInternalOffsets = listInternalOffsets;
    }
}
