package exportexcel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExportHeading {

    private ArrayList <int[]> listInternalOffsets;

    public void exportHeading (XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<float[][]> listData, ArrayList<int[]> listInternalOffsets,
                               int[] outputUnit, int globalVerticalOffset) {

        CellsStyle cellStyle = new CellsStyle();
        this.listInternalOffsets = listInternalOffsets;

        // вспомогательные переменные
        int rowName = 0 + globalVerticalOffset;                                                                         // номер строки с названием блока
        int rowType = 1 + globalVerticalOffset;                                                                         // номер строки с названием (типом) величины
        int rowUnit = 2 + globalVerticalOffset;                                                                         // номер строки с единицами измерения величины

        XSSFRow rowHeading;                                                                                             // создание строк для заголовка таблицы
        sheet.createRow(rowName);
        sheet.createRow(rowType);
        sheet.createRow(rowUnit);

        // заголовок блока высот
        rowHeading = sheet.getRow(rowName);
        cellStyle.createCell(dataBook, rowHeading, 0, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Высота");
        sheet.addMergedRegion(new CellRangeAddress(rowName, rowType, 0, listData.get(0)[0].length - 1));

        // блок высот
        for (int column = 0; column <= listData.get(0)[0].length - 1; column++) {

            // перебор условий для определения единиц измерений
            for (int j = 0; j <= listInternalOffsets.get(0).length - 1; j++) {

                if (column == listInternalOffsets.get(0)[j]) {

                    // единицы измерения
                    rowHeading = sheet.getRow(rowUnit);
                    cellStyle.createCell(dataBook, rowHeading, column, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, outputUnitAltitude(column, outputUnit));
                }
            }
        }

        // заголовок блока атмосферы
        rowHeading = sheet.getRow(rowName);
        cellStyle.createCell(dataBook, rowHeading, listData.get(0)[0].length, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Параметры атмосферы");
        sheet.addMergedRegion(new CellRangeAddress(rowName, rowName, listData.get(0)[0].length, listData.get(0)[0].length + listData.get(1)[0].length - 1));

        // блок параметров атмосферы. перебор столбцов в блоке
        for (int column = 0; column <= listData.get(1)[0].length - 1; column++) {

            // перебор условий для определения единиц измерений
            for (int j = 0; j <= listInternalOffsets.get(1).length - 1; j++) {

                if (column == listInternalOffsets.get(1)[j]) {

                    // название (тип) величины
                    rowHeading = sheet.getRow(rowType);
                    cellStyle.createCell(dataBook, rowHeading, column + listData.get(0)[0].length, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, atmParamType(j));

                    // единицы измерения
                    rowHeading = sheet.getRow(rowUnit);
                    cellStyle.createCell(dataBook, rowHeading, column + listData.get(0)[0].length, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, outputUnitAtmParam(column, outputUnit));
                }
            }
        }

        // перебор блоков скоростей
        for (int i = 2; i < listData.size(); i++) {

            // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
            int offset = listData.get(0)[0].length + listData.get(1)[0].length + listData.get(i)[0].length * (i - 2);

            // заголовок блока скоростей
            rowHeading = sheet.getRow(rowName);
            cellStyle.createCell(dataBook, rowHeading, offset, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, velocityName(i));
            sheet.addMergedRegion(new CellRangeAddress(rowName, rowName, offset, offset + listData.get(i)[0].length - 1));

            // перебор стобцов в блоке
            for (int column = 0; column <= listData.get(i)[0].length - 1; column++) {

                // перебор условий для определения единиц измерений
                for (int j = 0; j <= listInternalOffsets.get(2).length - 1; j++) {

                    if (column == listInternalOffsets.get(2)[j]) {

                        // название (тип) величины
                        rowHeading = sheet.getRow(rowType);
                        cellStyle.createCell(dataBook, rowHeading, offset + column, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, velocityType(j));

                        // единицы измерения. больше условий богу условий
                        rowHeading = sheet.getRow(rowUnit);
                        cellStyle.createCell(dataBook, rowHeading, offset + column, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, outputUnitVelocity(column, outputUnit));
                    }
                }
            }
        }
        // конец метода
    }

    private String outputUnitAltitude (int column, int [] outputUnit) {

        String unit = "";
        int unitAltitude = outputUnit[0];

        if (column == listInternalOffsets.get(0)[0]) {
            unit = outputUnitAltitude(unitAltitude);
        }
        if (column == listInternalOffsets.get(0)[1]) {
            unit = "фут";
        }
        return unit;
    }

    private String outputUnitVelocity (int column, int[] outputUnit) {

        String unit = "";
        int unitVelocity = outputUnit[1];
        int unitPressure = outputUnit[3];

        if (column == listInternalOffsets.get(2)[4]) {
            unit = unitPressure(unitPressure);
        }
        if (column == listInternalOffsets.get(2)[1]) {
            unit = "-";
        }
        if ((column != listInternalOffsets.get(2)[4]) & (column != listInternalOffsets.get(2)[1])) {
            unit = unitVelocity(unitVelocity);
        }
        return unit;
    }

    private String outputUnitAtmParam (int column, int[] outputUnit) {

        String unit = "";
        int unitVelocity = outputUnit[1];
        int unitDensity = outputUnit[2];
        int unitPressure = outputUnit[3];
        int unitTemperature = outputUnit[4];

        if (column == listInternalOffsets.get(1)[0]) {
            unit = unitDensity(unitDensity);
        }
        if (column == listInternalOffsets.get(1)[1]) {
            unit = unitPressure(unitPressure);
        }
        if (column == listInternalOffsets.get(1)[2]) {
            unit = unitTemperature(unitTemperature);
        }
        if (column == listInternalOffsets.get(1)[3]) {
            unit = unitVelocity(unitVelocity);
        }
        return unit;
    }

    private String unitVelocity(int unitVelocity) {

        switch (unitVelocity) {
            case 0:
                return "м/с";
            case 1:
                return "км/ч";
            case 2:
                return "knot";
            default:
                return "Неизвестный тип";
        }
    }

    private String outputUnitAltitude (int unitAltitude) {

        switch (unitAltitude) {
            case 0:
                return "м";
            case 1:
                return "км";
            default:
                return "Неизвестный тип";
        }
    }

    private String unitTemperature (int unitTemperature) {

        switch (unitTemperature) {
            case 0:
                return "K";
            case 1:
                return "C";
            case 2:
                return "F";
            default:
                return "Неизвестный тип";
        }
    }

    private String unitPressure(int unitPressure) {

        switch (unitPressure) {
            case 0:
                return "Па";
            case 1:
                return "кг/м^2";
            default:
                return "Неизвестный тип";
        }
    }

    private String unitDensity (int unit) {

        if (unit == 0) {
            return "кг/м^3";
        } else {
            return "Неизвестный тип";
        }
    }

    private String velocityName (int name) {

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

    private String velocityType (int type) {

        switch (type) {
            case 0:
                return "Vcas";
            case 1:
                return "M";
            case 2:
                return "Vtas";
            case 3:
                return "Veas";
            case 4:
                return "q";
            default:
                return "Неизвестный тип";
        }
    }

    private String atmParamType (int type) {

        switch (type) {
            case 0:
                return "Плотность";
            case 1:
                return "Атм. давл.";
            case 2:
                return "Температура";
            case 3:
                return "Скорость звука";
            default:
                return "Неизвестный тип";
        }
    }





}
