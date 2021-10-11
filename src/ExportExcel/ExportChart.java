package exportexcel;

import enums.UnitOutput;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.ArrayList;

public class ExportChart {

    public void Chart (XSSFSheet sheet, ArrayList<int[]> listInternalOffsets, int globalVerticalOffset,
                       int localVerticalOffset, int[] rowEndIndex, ArrayList<double[][]> listData, UnitOutput[] unitOutput) {

        // типовые размеры лабуды на которой все рисуется
        // 0 - длина лабуды; 1 - высота лабуды; 2, 3 - dx и dy соответственно - абсолютное смещение лабуды относительно 0 (индекс);
        // 4 - горизонтальный отступ между графиками (их начальными точками).
        int [] size = new int[] {13, 25, 0, 0, 14};

        // создание лабуды на которой ресуется график для Маха
        XSSFDrawing drawingMach = sheet.createDrawingPatriarch();                                                           // создает Patriarch - лабуду на которогй все рисуется
        XSSFClientAnchor anchorMach = drawingMach.createAnchor(0, 0, 0, 0, size[2], size[3], size[2]+size[0], size[3]+size[1]); // создание прямоугольника по координатам двух точек - верхней левой и правой нижней
        XSSFChart chartMach = drawingMach.createChart(anchorMach);                                                                  // добавление в лабуду графика
        // создание лабуды на которой ресуется график для Vtas
        XSSFDrawing drawingVtas = sheet.createDrawingPatriarch();                                                           // создает Patriarch - лабуду на которогй все рисуется
        XSSFClientAnchor anchorVtas = drawingVtas.createAnchor(0, 0, 0, 0, size[2] + size[4], size[3], size[2]+size[0] + size[4], size[3]+size[1]); // создание прямоугольника по координатам двух точек - верхней левой и правой нижней
        XSSFChart chartVtas = drawingVtas.createChart(anchorVtas);                                                                  // добавление в лабуду графика

        // создание легенды для Маха
        XDDFChartLegend legendMach = chartMach.getOrAddLegend();
        legendMach.setPosition(LegendPosition.RIGHT);
        // создание легенды для Vtas
        XDDFChartLegend legendVtas = chartVtas.getOrAddLegend();
        legendVtas.setPosition(LegendPosition.RIGHT);

        // настройка осей для Маха
        XDDFCategoryAxis bottomAxisMach = chartMach.createCategoryAxis(AxisPosition.BOTTOM);                            // положение горизонтальной оси
        bottomAxisMach.setTitle("M");                                                                                   // название горизонтальной оси
        bottomAxisMach.setCrosses(AxisCrosses.AUTO_ZERO);                                                               // "0" - автоматическое
        XDDFValueAxis leftAxisMach = chartMach.createValueAxis(AxisPosition.LEFT);                                      // положение вертикальной оси
        leftAxisMach.setTitle("H, " + unitOutput[0].getUnitName());                                                     // название вертикальной оси
        leftAxisMach.setCrosses(AxisCrosses.AUTO_ZERO);                                                                 // "0" - автоматическое
        // настройка осей для Vtas
        XDDFCategoryAxis bottomAxisVtas = chartVtas.createCategoryAxis(AxisPosition.BOTTOM);                            // положение горизонтальной оси
        bottomAxisVtas.setTitle("Vtas, " + unitOutput[2].getUnitName());                                                // название горизонтальной оси
        bottomAxisVtas.setCrosses(AxisCrosses.AUTO_ZERO);                                                               // "0" - автоматическое
        XDDFValueAxis leftAxisVtas = chartVtas.createValueAxis(AxisPosition.LEFT);                                      // положение вертикальной оси
        leftAxisVtas.setTitle("H, " + unitOutput[0].getUnitName());                                                     // название вертикальной оси
        leftAxisVtas.setCrosses(AxisCrosses.AUTO_ZERO);                                                                 // "0" - автоматическое

        // максимальное и минимальное значение
        //leftAxisMach.setMaximum(12.2);
        //leftAxisMach.setMinimum(-0.3);

        // индекс первой строки
        int firstRow = globalVerticalOffset + localVerticalOffset;
        int offset = 0;

        // вертикальная ось
        XDDFDataSource <Double>[] dataSourceMach = new XDDFDataSource[listData.size()];
        XDDFDataSource <Double>[] dataSourceVtas = new XDDFDataSource[listData.size()];
        // горизонтальная ось
        XDDFNumericalDataSource <Double>[] numericalDataSourceMach = new XDDFNumericalDataSource[listData.size()];
        XDDFNumericalDataSource <Double>[] numericalDataSourceVtas = new XDDFNumericalDataSource[listData.size()];
        // набор данных
        XDDFLineChartData dataMach = (XDDFLineChartData)chartMach.createData(ChartTypes.LINE, bottomAxisMach, leftAxisMach);
        XDDFLineChartData dataVtas = (XDDFLineChartData)chartVtas.createData(ChartTypes.LINE, bottomAxisVtas, leftAxisVtas);
        // линия на графике
        XDDFLineChartData.Series seriesMach;
        XDDFLineChartData.Series seriesVtas;

        // выбор данных
        for (int listDataIndex = 0; listDataIndex <= listData.size() - 1; listDataIndex++) {

            if (listDataIndex == 0) {
                offset = 0;
            }
            else {
                offset = listData.get(listDataIndex - 1)[0].length + offset;
            }

            if (listDataIndex >= 2) {

                // вертикальная ось число Маха
                dataSourceMach[listDataIndex] = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, firstRow + rowEndIndex[listDataIndex - 2],
                        offset - 1 + listInternalOffsets.get(2)[1], offset - 1 + listInternalOffsets.get(2)[1]));
                // вертикальная ось Vtas
                dataSourceVtas[listDataIndex] = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, firstRow + rowEndIndex[listDataIndex - 2],
                        offset - 1 + listInternalOffsets.get(2)[2], offset - 1 + listInternalOffsets.get(2)[2]));

                // горизонтальная ось число Маха
                numericalDataSourceMach[listDataIndex] = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, firstRow + rowEndIndex[listDataIndex - 2],
                        listInternalOffsets.get(0)[0], listInternalOffsets.get(0)[0]));
                // горизонтальная ось число Vtas
                numericalDataSourceVtas[listDataIndex] = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, firstRow + rowEndIndex[listDataIndex - 2],
                        listInternalOffsets.get(0)[0], listInternalOffsets.get(0)[0]));

                // набор данных для числа Маха
                seriesMach = (XDDFLineChartData.Series)dataMach.addSeries(dataSourceMach[listDataIndex], numericalDataSourceMach[listDataIndex]);
                seriesMach.setTitle(getTitleMach(listDataIndex), null);
                seriesMach.setSmooth(true);
                seriesMach.setMarkerSize((short)6);
                seriesMach.setMarkerStyle(MarkerStyle.TRIANGLE);

                // набор данных для Vtas
                seriesVtas = (XDDFLineChartData.Series)dataVtas.addSeries(dataSourceVtas[listDataIndex], numericalDataSourceVtas[listDataIndex]);
                seriesVtas.setTitle(getTitleVtas(listDataIndex), null);
                seriesVtas.setSmooth(true);
                seriesVtas.setMarkerSize((short)6);
                seriesVtas.setMarkerStyle(MarkerStyle.TRIANGLE);
            }
        }

        chartMach.plot(dataMach);
        chartVtas.plot(dataVtas);

    }

    private String getTitleMach(int name) {

        switch (name) {
            case 2:
                return "Md";
            case 3:
                return "Mc";
            case 4:
                return "Ma";
            case 5:
                return "Ms";
            default:
                return "Неизвестный тип";
        }
    }

    private String getTitleVtas(int name) {

        switch (name) {
            case 2:
                return "Vd";
            case 3:
                return "Vc";
            case 4:
                return "Va";
            case 5:
                return "Vs";
            default:
                return "Неизвестный тип";
        }
    }





}
