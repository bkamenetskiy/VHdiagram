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

public class ExcelExport {


    String[] dataTitle = new String[] {
            "Высота", "Плотность", "Давление", "Температура", "Скорость звука",
            "VdCAS", "   Md", "   VdTAS", "   VdEAS", "      qd",
            "       VcCAS", "      Mc", "   VcTAS", "   VcEAS", "     qc",
            "       VaCAS", "      Ma", "   VaTAS", "   VaEAS", "     qa",
            "       VsCAS", "      Ms", "   VsTAS", "   VsEAS", "     qs"
    };

    String[] dataUnit = new String[] {
            "м", "кг/м3", "Па", "К", "м/с",
            "м/с", "-", "м/с", "м/с", "Па",
            "м/с", "-", "м/с", "м/с", "Па",
            "м/с", "-", "м/с", "м/с", "Па"
    };

    // массив данных для экспорта
    private float[][] dataOutputArray;
    // создание книги
    XSSFWorkbook dataFile = new XSSFWorkbook();

    

    // запись в книгу
    public void writeData (float maxAltitude) throws IOException {

        // создание листа
        XSSFSheet sheet = this.dataFile.createSheet("data");

        // экспорт шапки таблицы
        int lengthDataTitle = dataTitle.length;
        XSSFRow rowTitle = sheet.createRow((short)0);
        for (int column = 0; column <= lengthDataTitle-1; column++) {
            rowTitle.createCell(column).setCellValue(dataTitle[column]);
        }

        // экспорт единиц измерения
        int lengthDataUnit = dataUnit.length;
        XSSFRow rowUnit = sheet.createRow((short)1);
        for (int column = 0; column <= lengthDataUnit-1; column++) {
            rowUnit.createCell(column).setCellValue(dataUnit[column]);
        }




        // экспорт данных
        int minAltitude = 0;                                                                                            // минимальная высота
        int incAltitude = 10;                                                                                           // шаг приращения по высоте
        int firstRow = 2;                                                                                               // индекс первой строки - сдвиг на шапку таблицы
        int Row = 2;                                                                                                    // индекс текущей строки


        while (minAltitude <= maxAltitude){

            XSSFRow rowData = sheet.createRow((short)Row);                                                              // создание строки (тип short, номер Row)
            for (int column = 0; column <= 25-1; column++) {                                                            // перебор столбцов
                rowData.createCell(column).setCellValue(this.dataOutputArray[minAltitude][column]);                     // создание ячейки с координатами Row (строка) и column (столбец), запись в нее элемента массива с координатами minAltitude и column
            }
            minAltitude = minAltitude + incAltitude;                                                                    // установка значения следующей высоты для вывода
            Row++;                                                                                                      // установка координаты следующей строки
        }

        // поиск -1 в Ma



        // параметры лабуды на которой рисуется график


        // типовые размеры лабуды на которой все рисуется
        // 0 - длина лабуды; 1 - высота лабуды; 2, 3 - dx и dy соответственно - абсолютное смещение лабуды относительно 0 (индекс);
        int [] size = new int[] {13, 25, 26, 0};


        // создание лабуды на которой ресуется график
        XSSFDrawing drawing = sheet.createDrawingPatriarch();                                                           // создает Patriarch - лабуду на которогй все рисуется
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0+size[2], 0+size[3], 0+size[2]+size[0], 0+size[3]+size[1]); // создание прямоугольника по координатам двух точек - верхней левой и правой нижней
        XSSFChart chart = drawing.createChart(anchor);                                                                  // добавление в лабуду графика

        // создание легенды
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        // настройка осей
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);                                    // положение горизонтальной оси
        bottomAxis.setTitle("M");                                                                                       // название горизонтальной оси
        bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);                                                                   // выбор начало "0" - автоматическое
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);                                              // положение вертикальной оси
        leftAxis.setTitle("H");                                                                                         // название вертикальной оси
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);                                                                     // выбор начало "0" - автоматическое

        // выбор данных
        XDDFDataSource<Double> Md = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, Row-1, 6, 6));
        XDDFDataSource<Double> Mc = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, Row-1, 11, 11));
        XDDFDataSource<Double> Ma = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, 1149, 16, 16));
        XDDFDataSource<Double> Ms = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, Row-1, 21, 21));
        XDDFNumericalDataSource<Double> Altitude = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, Row-1, 0, 0));
        XDDFNumericalDataSource<Double> Altitude2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, 1149, 0, 0));

        // настройка подписи данных
        XDDFLineChartData data = (XDDFLineChartData)chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series)data.addSeries(Md, Altitude);
        series1.setTitle("Md", (CellReference)null);
        series1.setSmooth(true);
        series1.setMarkerSize((short)6);
        series1.setMarkerStyle(MarkerStyle.TRIANGLE);
        XDDFLineChartData.Series series2 = (XDDFLineChartData.Series)data.addSeries(Mc, Altitude);
        series2.setTitle("Mc", (CellReference)null);
        series2.setSmooth(true);
        series2.setMarkerSize((short)6);
        series2.setMarkerStyle(MarkerStyle.TRIANGLE);
        XDDFLineChartData.Series series3 = (XDDFLineChartData.Series)data.addSeries(Ma, Altitude2);
        series3.setTitle("Ma", (CellReference)null);
        series3.setSmooth(true);
        series3.setMarkerSize((short)6);
        series3.setMarkerStyle(MarkerStyle.TRIANGLE);
        XDDFLineChartData.Series series4 = (XDDFLineChartData.Series)data.addSeries(Ms, Altitude);
        series4.setTitle("Ms", (CellReference)null);
        series4.setSmooth(true);
        series4.setMarkerSize((short)6);
        series4.setMarkerStyle(MarkerStyle.TRIANGLE);
        chart.plot(data);
//        solidLineSeries(data, 0, PresetColor.CHARTREUSE);
//        solidLineSeries(data, 1, PresetColor.TURQUOISE);




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

    public void setDataOutputArray(float[][] dataOutputArray) {
        this.dataOutputArray = dataOutputArray;
    }

    private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        org.apache.poi.xddf.usermodel.chart.XDDFChartData.Series series = (org.apache.poi.xddf.usermodel.chart.XDDFChartData.Series)data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }

        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }


}
