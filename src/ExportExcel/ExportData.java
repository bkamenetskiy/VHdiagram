package exportexcel;

import enums.Unit;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import solvers.SolverUnitOutput;

import java.util.ArrayList;

public class ExportData {

    private XSSFRow rowData;
    private SolverUnitOutput unitConvert = new SolverUnitOutput();
    private UnitMatching unitMatching = new UnitMatching();
    private CellStyleNumber cellStyleNumber = new CellStyleNumber();
    private Cell cell;

    Unit[] unitOutput = new Unit[] {Unit.Kilometer, Unit.Foot, Unit.KilometerPerHr, Unit.KgPerM3, Unit.Pa, Unit.Kelvin, Unit.Dimensionless_Mach};

    public void exportData(XSSFWorkbook dataBook, XSSFSheet sheet, ArrayList<float[][]> listData, ArrayList<int[]> listInternalOffsets,
                           int globalVerticalOffset, int localVerticalOffset, int outputAltitudeInc, int rowCount, int[] rowEndIndex) {

        // вспомогательные переменные
        int rowAltitudeInc = 0;                                                                                         // счетчик строк в массивах данных
        int rowSheetInc = 0;                                                                                            // счетчик строк в листе excel
        int offset = 0;                                                                                                 // смещение блоков относительно друг друга
        float currentValue;                                                                                             // вспомогательная переменная для конвертации
        Unit unit;                                                                                                      // хранит единицы измерения
        CellStyle style;                                                                                                // хранит выбранный стиль ячейки

        // создание стилей ячеек
        cellStyleNumber.createCellStyle(dataBook, unitOutput);

        while (rowAltitudeInc <= rowCount) {

            // создание строки
            this.rowData = sheet.createRow(rowSheetInc + localVerticalOffset + globalVerticalOffset);

            // перебираем хранилище данных
            for (int listDataIndex = 0; listDataIndex <= listData.size() - 1; listDataIndex++) {

                // рассчет смещений блоков
                if (listDataIndex ==0) {
                    offset = 0;
                }
                else {
                    offset = listData.get(listDataIndex - 1)[0].length + offset;
                }

                // в хранилище перебираем соответствующий массив данных
                for (int column = 0; column <= listData.get(listDataIndex)[0].length - 1; column++) {

                    // задаем временной переменной текущее значение высоты
                    // условие необходимо, что бы в футы не записались нули, т.к. во время расчета высот метры в имперские единицы не переводились, и столбец заполнялся 0
                    if (listDataIndex == 0) {
                        currentValue = listData.get(listDataIndex)[rowAltitudeInc][listInternalOffsets.get(0)[0]];
                    }
                    else {
                        currentValue = listData.get(listDataIndex)[rowAltitudeInc][column];
                    }

                    // конвертация и запись в ячейку
                    // возврат единиц измерения из сопоставителя
                    unit = unitMatching.getUnit(listDataIndex, column, listInternalOffsets, unitOutput);
                    style = cellStyleNumber.getUnitStyle(unit);

                    // экспорт. отрицательные высоты и параметры атмосферы экспортируются
                    if (listDataIndex < 2) {
                        //rowData.createCell(column + offset).setCellValue(unitConvert.getUnitOutput(unit, currentValue)); - старая версия
                        this.cell = this.rowData.createCell(column + offset);
                        this.cell.setCellValue(unitConvert.getUnitOutput(unit, currentValue));
                        this.cell.setCellStyle(style);

                    }
                    // а вот отрицательные скорости - нет.
                    if ((listDataIndex >= 2) & (currentValue >= 0.0f)){
                        this.cell = this.rowData.createCell(column + offset);
                        this.cell.setCellValue(unitConvert.getUnitOutput(unit, currentValue));
                        this.cell.setCellStyle(style);
                    }
                    // вместо них создается пустая ячейка
                    if ((listDataIndex >= 2) & (currentValue <= 0.0f)){
                        this.rowData.createCell(column + offset).setCellValue("");
                    }


                    // Ma граничен сверху Mc, при этом, если Ma превышает предельное значение, он обращается в -1.
                    // И это только один из нескольких частных случаев, возможных при рассчетах.
                    // Для корректного построения графика необходимо определить последнюю строку, в котрой Ma больше 0.
                    // по этому в rowEndIndex пишутся индексы последних строк каждого из блоков скоростей
                    if ((currentValue > 0.0f) & (listDataIndex >= 2)){

                        rowEndIndex[listDataIndex - 2] = rowSheetInc;
                    }
                }
            }
            rowAltitudeInc = rowAltitudeInc + outputAltitudeInc;                                                        // установка значения следующей высоты для вывода
            rowSheetInc++;                                                                                              // установка индекса следующей строки
        }

        //System.out.println(rowEndIndex[0] +" " + rowEndIndex[1] + " " +rowEndIndex[2] + " " + rowEndIndex[3] + "   " + Unit.Kelvin.getPrecision() );
    }








}
