package exportexcel;

import enums.UnitOutput;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.ArrayList;

public class ExportChart {

    /**
     * Строит графики высоты от числа Маха и истинной скорости по данным листа Excel.
     */
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

        XDDFScatterChartData dataMach = createScatterData(chartMach, "M", "0.0", unitOutput[0]);
        XDDFScatterChartData dataVtas = createScatterData(chartVtas, "Vtas, " + unitOutput[2].getUnitName(),
                "0", unitOutput[0]);

        int firstRow = globalVerticalOffset + localVerticalOffset;
        int altitudeColumn = listInternalOffsets.get(0)[0];
        int[] velocityOffsets = listInternalOffsets.get(2);
        // Первый блок скоростей начинается после высоты и атмосферы. Вспомогательная высота пропускается.
        int velocityColumn = listData.get(0)[0].length + listData.get(1)[0].length - 1;

        for (int listDataIndex = 2; listDataIndex < listData.size(); listDataIndex++) {
            int lastRow = firstRow + rowEndIndex[listDataIndex - 2];
            int machColumn = velocityColumn + velocityOffsets[1];
            int vtasColumn = velocityColumn + velocityOffsets[2];

            XDDFNumericalDataSource<Double> altitudes = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(firstRow, lastRow, altitudeColumn, altitudeColumn));
            XDDFNumericalDataSource<Double> mach = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(firstRow, lastRow, machColumn, machColumn));
            XDDFNumericalDataSource<Double> vtas = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(firstRow, lastRow, vtasColumn, vtasColumn));

            addSmoothSeries(dataMach, mach, altitudes, getTitleMach(listDataIndex));
            addSmoothSeries(dataVtas, vtas, altitudes, getTitleVtas(listDataIndex));
            velocityColumn += listData.get(listDataIndex)[0].length;
        }

        chartMach.plot(dataMach);
        chartVtas.plot(dataVtas);

    }

    /**
     * Настраивает область построения, числовые оси, сетку и стиль точечной диаграммы.
     * Задаёт формат подписей осей независимо от формата ячеек.
     */
    private XDDFScatterChartData createScatterData(XSSFChart chart, String xTitle, String xNumberFormat, UnitOutput altitudeUnit) {
        configureChartLayout(chart);
        chart.displayBlanksAs(DisplayBlanks.GAP);

        XDDFValueAxis xAxis = chart.createValueAxis(AxisPosition.BOTTOM);
        xAxis.setTitle(xTitle);
        xAxis.setNumberFormat(xNumberFormat);
        styleAxis(xAxis);

        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle("H, " + altitudeUnit.getUnitName());
        yAxis.setNumberFormat("0");
        styleAxis(yAxis);

        for (var axis : chart.getCTChart().getPlotArea().getValAxList()) {
            axis.getNumFmt().setSourceLinked(false);
        }

        XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, xAxis, yAxis);
        data.setStyle(ScatterStyle.SMOOTH);
        return data;
    }

    /**
     * Настраивает прямоугольное полотно, область построения и легенду у правого края.
     */
    private void configureChartLayout(XSSFChart chart) {
        chart.getCTChartSpace().addNewRoundedCorners().setVal(false);

        XDDFManualLayout plotLayout = chart.getOrAddManualLayout();
        plotLayout.setTarget(LayoutTarget.INNER);
        plotLayout.setXMode(LayoutMode.EDGE);
        plotLayout.setYMode(LayoutMode.EDGE);
        plotLayout.setWidthMode(LayoutMode.FACTOR);
        plotLayout.setHeightMode(LayoutMode.FACTOR);
        plotLayout.setX(0.11);
        plotLayout.setY(0.04);
        plotLayout.setWidthRatio(0.87);
        plotLayout.setHeightRatio(0.87);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);
        legend.setOverlay(true);
        XDDFManualLayout legendLayout = legend.getOrAddManualLayout();
        legendLayout.setXMode(LayoutMode.EDGE);
        legendLayout.setYMode(LayoutMode.EDGE);
        legendLayout.setX(0.905);
        legendLayout.setY(0.60);
    }

    /**
     * Оформляет линию оси толщиной 2 пт и добавляет основные линии сетки толщиной 0,5 пт.
     */
    private void styleAxis(XDDFValueAxis axis) {
        XDDFColor axisColor = XDDFColor.from(new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80});
        XDDFLineProperties axisLine = new XDDFLineProperties(new XDDFSolidFillProperties(axisColor));
        axisLine.setWidth(2.0);
        axis.getOrAddShapeProperties().setLineProperties(axisLine);

        XDDFColor gridColor = XDDFColor.from(new byte[]{(byte) 0xA5, (byte) 0xA5, (byte) 0xA5});
        XDDFLineProperties gridLine = new XDDFLineProperties(new XDDFSolidFillProperties(gridColor));
        gridLine.setWidth(0.5);
        axis.getOrAddMajorGridProperties().setLineProperties(gridLine);
    }

    /**
     * Добавляет ряд с высотой по оси Y и сглаженной линией без маркеров.
     */
    private void addSmoothSeries(XDDFScatterChartData data, XDDFNumericalDataSource<Double> xValues,
                                 XDDFNumericalDataSource<Double> altitudes, String title) {
        XDDFScatterChartData.Series series = (XDDFScatterChartData.Series) data.addSeries(xValues, altitudes);
        series.setTitle(title, null);
        series.setSmooth(true);
        series.setMarkerStyle(MarkerStyle.NONE);
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
