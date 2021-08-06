package ExportExcel;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;

public class ExportChart {




    public void Chart (XSSFSheet sheet, ArrayList<int[]> listInternalOffsets, int globalVerticalOffset, int localVerticalOffset, int[] rowEndIndex) {


        // типовые размеры лабуды на которой все рисуется
        // 0 - длина лабуды; 1 - высота лабуды; 2, 3 - dx и dy соответственно - абсолютное смещение лабуды относительно 0 (индекс);
        int [] size = new int[] {13, 25, 0, 0};


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
        bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);                                                                   // "0" - автоматическое
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);                                              // положение вертикальной оси
        leftAxis.setTitle("H");                                                                                         // название вертикальной оси
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);                                                                     // "0" - автоматическое

        // индекс первой строки
        int firstRow = globalVerticalOffset + localVerticalOffset;

        // выбор данных
        // значения горизонтальной оси
        XDDFDataSource<Double> Md = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, rowEndIndex[0], 7, 7));  // выбор столбца по двум точкам: 0, 1 - первая и последняя строка; 2, 3 - первый и последний столбец
        XDDFDataSource<Double> Mc = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, rowEndIndex[1], 12, 12));
        XDDFDataSource<Double> Ma = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, rowEndIndex[2], 17, 17));
        XDDFDataSource<Double> Ms = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, rowEndIndex[3], 22, 22));
        // значения вертикальной оси
        XDDFNumericalDataSource<Double> Altitude = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, rowEndIndex[0], 0, 0));
        XDDFNumericalDataSource<Double> AltitudeMa = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, rowEndIndex[2], 0, 0));

        // настройка подписи данных и выбор типа графика
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
        XDDFLineChartData.Series series3 = (XDDFLineChartData.Series)data.addSeries(Ma, AltitudeMa);
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








    }






}
