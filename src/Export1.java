import  java.io.*;

import models.ModelUnits;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Export1 {

    // сокровищница
    private ArrayList <int[]> serviceList;                                                                              // хранилище общих настроек
    private ArrayList <int[]> outputList;                                                                               // хранилище настроек вывода
    private ArrayList <float [][]> dataList;                                                                            // хранилище данных

    // создание конвертера единиц измерения
    private ModelUnits unitConvert = new ModelUnits();
    // создание книги
    private XSSFWorkbook dataFile = new XSSFWorkbook();
    // создание листа
    private XSSFSheet sheet = this.dataFile.createSheet("Result");


    private int unitAltitude = 0; // 1 - километры; любой другой ключ - метры
    private int unitVelocity = 1; // 0 - м/с; 1 - км/ч; 2 - knot

    private String []  titleAtmParam = new String[] {"Плотность", "Давление", "Скорость звука", "Температура"};
    private String []  titleVelocity = new String[] {"Vcas", "M", "Vtas", "Veas", "q"};

    private String []  titleUnitVelocity = new String[] {"м/с", "км/ч", "knot"};
    private String []  titleUnitLength = new String[] {"м", "км", "фут"};




    public void exportData (float maxAltitude, int outputAltitudeInc) throws IOException {



        // экспорт данных
        int initAlt = 0; // счетчик текущей высоты (в метрах) = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc
        int rowInc = 0; // счетчик строк
        int deltaHeight = 2; // смещение таблицы с данными по вертикали = высота шапки таблицы
        int deltaWidth = 0; // смещение таблицы вправо

        float conversionAltitude;                                                                                       // вспомогательная переменная для конвертации высот
        float conversionVelocity;                                                                                       // вспомогательная переменная для конвертации скоростей



        // сие сотонинство работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая хранится в последней строке [serviceList.get(0)[0]], в метрах [this.serviceList.get(1)[0]]) массива с данными this.dataList.get(0)
        while (initAlt <= (int) this.dataList.get(0)[this.serviceList.get(0)[0]] [this.serviceList.get(1)[0]]){

            XSSFRow rowData = this.sheet.createRow((short) rowInc + deltaHeight);                               // создание строки (тип short), с номером rowInc с глобальным смещением вниз на высоту шапки таблицы deltaHeight

            // блок высот
            for (int column = 0; column <= this.dataList.get(0)[0].length - 1; column++) {                              // перебор столбцов блока высот от нулевого до последнего

                conversionAltitude = this.dataList.get(0)[initAlt][this.serviceList.get(1)[0]];                         // установка текущей высоты в метрах

                // экспорт высоты в метрах
                if (column == this.serviceList.get(1)[0]) {

                    switch (this.unitAltitude) {
                        case 1:
                            conversionAltitude = unitConvert.getLengthKilometer(conversionAltitude);                    // конвертация из метров в километры
                            rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);        // создание ячейки в строке с номером rowData и номером столбца column, запись в нее элемента Altitude
                            break;
                        default:
                            rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);
                            break;
                    }
                }

                // экспорт высоты в футах
                if (column == this.serviceList.get(1)[1]) {
                    conversionAltitude = unitConvert.getLengthFoot(conversionAltitude);                                 // конвертация из метров в футы
                    rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);
                }

            }

            // блок атмосферы. экспортируется как есть
            for (int column = 0; column <= this.dataList.get(1)[0].length - 1; column++) {

                rowData.createCell(column + deltaWidth + this.dataList.get(0)[0].length).setCellValue(this.dataList.get(1)[initAlt][column]);

            }

            // блок скорости. перебор массивов скоростей
            for (int i = 2; i < this.dataList.size(); i++) {

                // экспорт данных из блока i
                for (int column = 0; column <= this.dataList.get(i)[0].length - 1; column++) {

                    // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
                    int offset = deltaWidth + this.dataList.get(0)[0].length + this.dataList.get(1)[0].length + this.dataList.get(i)[0].length * (i - 2);
                    // вспомогательная переменная. хранит текущеее значение взятое из блока скорости
                    conversionVelocity = this.dataList.get(i)[initAlt][column];

                    // конвертация Vcas. отсечение отрицательных значений
                    if ((column == this.serviceList.get(1)[6]) & (conversionVelocity > 0.0f)) {
                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + this.serviceList.get(1)[6]).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + this.serviceList.get(1)[6]).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + this.serviceList.get(1)[6]).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка маха на -1
                    if ((column == this.serviceList.get(1)[7]) & (conversionVelocity > 0.0f)) {
                            rowData.createCell(offset + this.serviceList.get(1)[7]).setCellValue(conversionVelocity);
                    }

                    // конвертация Vtas. отсечение отрицательных значений
                    if ((column == this.serviceList.get(1)[8]) & (conversionVelocity > 0.0f)) {
                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + this.serviceList.get(1)[8]).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + this.serviceList.get(1)[8]).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + this.serviceList.get(1)[8]).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // конвертация Veas. отсечение отрицательных значений
                    if ((column == this.serviceList.get(1)[9]) & (conversionVelocity > 0.0f)) {
                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + this.serviceList.get(1)[9]).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + this.serviceList.get(1)[9]).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + this.serviceList.get(1)[9]).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка скоростного напора на -1
                    if ((column == this.serviceList.get(1)[10]) & (conversionVelocity > 0.0f)) {
                            rowData.createCell(offset + this.serviceList.get(1)[10]).setCellValue(conversionVelocity);
                    }
                }
            }

            initAlt = initAlt + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка координаты следующей строки
        }






        writeFile("d:\\Data.xlsx", this.dataFile);
        System.out.println("Your excel file has been generated!");


        float abc =  this.dataList.get(0)[10][0];
        abc = unitConvert.getLengthKilometer(abc);
        System.out.println(abc);


    }



    // запись файла на диск
    private void writeFile (String filename, XSSFWorkbook file) throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        file.write(out);
        out.close();
        file.close();
    }


    public void setServiceList(ArrayList<int[]> serviceList) {
        this.serviceList = serviceList;
    }

    public void setOutputList(ArrayList<int[]> outputList) {
        this.outputList = outputList;
    }

    public void setDataList(ArrayList<float[][]> dataList) {
        this.dataList = dataList;
    }
}
