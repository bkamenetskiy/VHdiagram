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
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType;

import java.util.ArrayList;
import java.util.Arrays;

public class ExportChart {
    private static final double PLOT_TOP = 0.04;
    private static final double PLOT_HEIGHT = 0.92;

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

        int firstRow = globalVerticalOffset + localVerticalOffset;
        int altitudeColumn = listInternalOffsets.get(0)[0];
        int lastAltitudeRow = firstRow + Arrays.stream(rowEndIndex).max().orElse(0);
        double minAltitude = sheet.getRow(firstRow).getCell(altitudeColumn).getNumericCellValue();
        double maxAltitude = sheet.getRow(lastAltitudeRow).getCell(altitudeColumn).getNumericCellValue();
        double xTitleY = getHorizontalAxisTitleY(minAltitude, maxAltitude);
        XDDFScatterChartData dataMach = createScatterData(chartMach, "M", "0.0", unitOutput[0], 0.93, xTitleY);
        XDDFScatterChartData dataVtas = createScatterData(chartVtas, "Vtas, " + unitOutput[2].getUnitName(),
                "0", unitOutput[0], 0.86, xTitleY);

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
    private XDDFScatterChartData createScatterData(XSSFChart chart, String xTitle, String xNumberFormat,
                                                  UnitOutput altitudeUnit, double xTitleX, double xTitleY) {
        configureChartLayout(chart, xTitleY);
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
            if (axis.getAxId().getVal() == xAxis.getId()) {
                styleAxisTitle(axis.getTitle(), xTitleX, xTitleY);
            } else {
                styleAxisTitle(axis.getTitle(), 0.07, 0.07);
            }
        }

        XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, xAxis, yAxis);
        data.setStyle(ScatterStyle.SMOOTH);
        return data;
    }

    /**
     * Настраивает прямоугольное полотно, область построения и легенду у правого края.
     */
    private void configureChartLayout(XSSFChart chart, double xTitleY) {
        chart.getCTChartSpace().addNewRoundedCorners().setVal(false);

        XDDFManualLayout plotLayout = chart.getOrAddManualLayout();
        plotLayout.setTarget(LayoutTarget.INNER);
        plotLayout.setXMode(LayoutMode.EDGE);
        plotLayout.setYMode(LayoutMode.EDGE);
        plotLayout.setWidthMode(LayoutMode.FACTOR);
        plotLayout.setHeightMode(LayoutMode.FACTOR);
        plotLayout.setX(0.06);
        plotLayout.setY(PLOT_TOP);
        plotLayout.setWidthRatio(0.92);
        plotLayout.setHeightRatio(PLOT_HEIGHT);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);
        legend.setOverlay(true);
        XDDFManualLayout legendLayout = legend.getOrAddManualLayout();
        legendLayout.setXMode(LayoutMode.EDGE);
        legendLayout.setYMode(LayoutMode.EDGE);
        legendLayout.setX(0.905);
        // При пересечении с подписью оси располагаем легенду выше неё.
        legendLayout.setY(xTitleY >= 0.54 && xTitleY < 0.805 ? xTitleY - 0.21 : 0.60);
    }

    /**
     * Размещает горизонтальную подпись шрифтом 12 пт внутри полотна, у конца оси.
     */
    private void styleAxisTitle(CTTitle axisTitle, double x, double y) {
        XDDFTitle title = new XDDFTitle(null, axisTitle);
        title.setOverlay(true);
        var body = title.getBody();
        body.getXmlObject().getBodyPr().setVert(STTextVerticalType.HORZ);
        body.getXmlObject().getBodyPr().setRot(0);
        var titleFont = body.getParagraph(0).getDefaultRunProperties();
        titleFont.setFontSize(12.0);
        titleFont.setBold(true);

        XDDFManualLayout layout = new XDDFManualLayout(axisTitle.getLayout());
        layout.setXMode(LayoutMode.EDGE);
        layout.setYMode(LayoutMode.EDGE);
        layout.setX(x);
        layout.setY(y);
    }

    /**
     * Оценивает положение пересечения горизонтальной оси с нулём высоты по округлённому диапазону.
     * Excel по-прежнему подбирает границы осей автоматически.
     */
    private double getHorizontalAxisTitleY(double minAltitude, double maxAltitude) {
        if (minAltitude >= 0) {
            return PLOT_TOP + PLOT_HEIGHT - 0.06;
        }
        if (maxAltitude <= 0) {
            return PLOT_TOP + 0.02;
        }

        double approximateStep = (maxAltitude - minAltitude) / 8;
        double decade = Math.pow(10, Math.floor(Math.log10(approximateStep)));
        double step = 10 * decade;
        for (double multiplier : new double[]{1, 2, 2.5, 5}) {
            if (multiplier * decade >= approximateStep) {
                step = multiplier * decade;
                break;
            }
        }
        double lowerTick = Math.floor(minAltitude / step) * step;
        double upperTick = Math.ceil(maxAltitude / step) * step;
        double crossing = PLOT_TOP + PLOT_HEIGHT * upperTick / (upperTick - lowerTick);
        return crossing < PLOT_TOP + 0.06 ? crossing + 0.02 : crossing - 0.06;
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
