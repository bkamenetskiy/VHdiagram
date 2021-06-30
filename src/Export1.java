import  java.io.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFChart;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Export1 {

    // сокровищница
    private ArrayList <int[]> serviceList;                                                                              // хранилище общих настроек
    private ArrayList <int[]> outputList;                                                                               // хранилище настроек вывода
    private ArrayList <float [][]> dataList;                                                                            // хранилище данных

    // создание книги
    private XSSFWorkbook dataFile = new XSSFWorkbook();
    // создание листа
    private XSSFSheet sheet = this.dataFile.createSheet("Result");

    // экспорт данных
    public void exportData (float maxAltitude, int outputAltitudeInc) throws IOException {

        int initAlt = 0;                                                                                                // счетчик текущей высоты = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc
        int rowInc = 0;                                                                                                 // счетчик строк


        // сие сотонинство работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая записана в последней строке массива с данными (this.serviceList.get(0)[0] - координата строки), в метрах  (this.serviceList.get(1)[0] - координата столбца с метрами)
        while (initAlt <= (int) this.dataList.get(0)[this.serviceList.get(1)[0]][this.serviceList.get(0)[0]]){

            // создание строки (тип short), с номером rowInc
            XSSFRow rowData = this.sheet.createRow((short)rowInc);

            // заполнение строки смыслом
            for (int columnIndex = 0; columnIndex <= dataList.get(0).length - 1; columnIndex++) {                       // перебор столбцов блока высот
                rowData.createCell(columnIndex + 0).setCellValue(this.dataList.get(0)[columnIndex][initAlt]);                         // создание ячейки с координатами rowInc (номер строки) и columnIndex (номер элемента строки), запись в нее элемента массива с координатами minAltitude и columnIndex
            }
            initAlt = initAlt + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка координаты следующей строки



        }




        // запись в файл
        writeFile("d:\\Data.xlsx", this.dataFile);
        System.out.println("Your excel file has been generated!");




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
