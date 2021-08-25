package exportexcel;

import enums.Unit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExportHeading {

    private ArrayList <int[]> listInternalOffsets;
    private XSSFRow rowData;
    private Cell cell;
    private CellStyleText cellStyle = new CellStyleText();
    private UnitMatching unitMatching = new UnitMatching();

    Unit[] unitOutput = new Unit[] {Unit.Kilometer, Unit.Foot, Unit.KilometerPerHr, Unit.KgPerM3, Unit.Pa, Unit.Kelvin, Unit.Dimensionless_Mach};




    public void exportHeading (XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<float[][]> listData, ArrayList<int[]> listInternalOffsets,
                               int[] outputUnit, int globalVerticalOffset) {



        this.listInternalOffsets = listInternalOffsets;


        Unit unit;                                                                                                      // хранит единицы измерения
        CellStyle style;                                                                                                // хранит выбранный стиль ячейки

        cellStyle.createCellStyle(dataBook);


        // вспомогательные переменные
        int rowName = 0 + globalVerticalOffset;                                                                         // номер строки с названием блока
        int rowType = 1 + globalVerticalOffset;                                                                         // номер строки с названием (типом) величины
        int rowUnit = 2 + globalVerticalOffset;                                                                         // номер строки с единицами измерения величины
        int[] rowTemp = new int[] {rowName, rowType, rowUnit};
        int offset = 0;                                                                                                 // для рассчета смещений
        int lenght = 0;                                                                                                 // для расчета длинн блоков (слияние ячеек)


        for (int i = 0; i <= rowTemp.length - 1; i++) {

            // создаем строку
            this.rowData = sheet.createRow(rowTemp[i]);

            // хозяйничаем в строке
            // перебираем блоки
            for (int listDataIndex = 0; listDataIndex <= listData.size() - 1; listDataIndex++) {

                // рассчитываем смещения
                if (listDataIndex ==0) {
                    offset = 0;
                    lenght = listData.get(0)[0].length;
                }
                else {
                    offset = listData.get(listDataIndex - 1)[0].length + offset;
                    lenght = listData.get(listDataIndex)[0].length;
                }

                // заполняем нулевую строку названиями блоков
                if (i == 0) {

                    // создание ячейки и форматирование
                    this.cell = this.rowData.createCell(offset);
                    this.cell.setCellStyle(this.cellStyle.getStyleText());

                    // заголовок
                    this.cell.setCellValue(blockName(listDataIndex));

                    // слияние ячеек
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, offset, offset + lenght - 1));
                }

                // заполняем первую строку названием величин
                if (i == 1) {

                    // роемся по столбцам внутри блока
                    for (int column = 0; column <= listData.get(listDataIndex)[0].length - 1; column++) {

                        // создание ячейки, форматирование, задание значения
                        this.cell = this.rowData.createCell(offset + column);
                        this.cell.setCellStyle(this.cellStyle.getStyleText());
                        this.cell.setCellValue("лпт");
                    }
                }

                // заполняем вторую строку единицами измерения
                if (i == 2) {

                    // роемся по столбцам внутри блока
                    for (int column = 0; column <= listData.get(listDataIndex)[0].length - 1; column++) {

                        // возврат единиц измерения
                        unit = unitMatching.getUnit(listDataIndex, column, listInternalOffsets, unitOutput);

                        // создание ячейки, форматирование, задание значения
                        this.cell = this.rowData.createCell(offset + column);
                        this.cell.setCellStyle(this.cellStyle.getStyleText());
                        this.cell.setCellValue(unit.getUnitName());
                    }
                }
            }
        }
    }

    private String blockName (int name) {

        switch (name) {
            case 0:
                return "Высота";
            case 1:
                return "Параметры атмосферы";
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
