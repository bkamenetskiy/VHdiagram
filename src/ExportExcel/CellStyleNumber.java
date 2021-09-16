package exportexcel;

import enums.UnitOutput;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class CellStyleNumber {

    // стили
    private CellStyle styleLength;          // длины

    private CellStyle styleLength_IS;          // длины имперские

    private CellStyle styleVelocity;          // скорости

    private CellStyle styleDensity;          // плотность

    private CellStyle stylePressure;          // давление

    private CellStyle styleTemperature;          // температура

    private CellStyle styleDimensionless_Mach;          // безразмерные

    // сопоставление единицы измерения и стиля ячейки
    protected CellStyle getUnitStyle (UnitOutput unit) {

        CellStyle style = null;

        if ((unit == UnitOutput.Meter) || (unit == UnitOutput.Kilometer)) {

            style = this.styleLength;
        }

        if ((unit == UnitOutput.Foot) || (unit == UnitOutput.Inch)) {

            style = this.styleLength_IS;
        }

        if ((unit == UnitOutput.MeterPerSecond) || (unit == UnitOutput.KilometerPerHr) || (unit == UnitOutput.Knot)) {

            style = this.styleVelocity;
        }

        if ((unit == UnitOutput.Kelvin) || (unit == UnitOutput.Celsius) || (unit == UnitOutput.Fahrenheit)) {

            style = this.styleTemperature;
        }

        if ((unit == UnitOutput.Pa) || (unit == UnitOutput.KPa) || (unit == UnitOutput.KgsPerM2)) {

            style = this.stylePressure;
        }

        if (unit == UnitOutput.KgPerM3) {

            style = this.styleDensity;
        }

        if (unit == UnitOutput.Dimensionless_Mach) {

            style = this.styleDimensionless_Mach;
        }

        return style;
    }

    // создание и настройка стилей
    protected void createCellStyle(XSSFWorkbook dataBook, UnitOutput[] unitOutput) {

        // длины
        DataFormat precisionLength = dataBook.createDataFormat();
        this.styleLength = dataBook.createCellStyle();
        // настройка стиля
        this.styleLength.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleLength.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleLength.setDataFormat(precisionLength.getFormat(unitOutput[0].getPrecision()));              // точность

        // длины имперские
        DataFormat precisionLength_IS = dataBook.createDataFormat();
        this.styleLength_IS = dataBook.createCellStyle();
        // настройка стиля
        this.styleLength_IS.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleLength_IS.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleLength_IS.setDataFormat(precisionLength_IS.getFormat(unitOutput[1].getPrecision()));              // точность

        // скорости
        DataFormat precisionVelocity = dataBook.createDataFormat();
        this.styleVelocity = dataBook.createCellStyle();
        // настройка стиля
        this.styleVelocity.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleVelocity.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleVelocity.setDataFormat(precisionVelocity.getFormat(unitOutput[2].getPrecision()));              // точность

        // плотности
        DataFormat precisionDensity = dataBook.createDataFormat();
        this.styleDensity = dataBook.createCellStyle();
        // настройка стиля
        this.styleDensity.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleDensity.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleDensity.setDataFormat(precisionDensity.getFormat(unitOutput[3].getPrecision()));              // точность

        // давления
        DataFormat precisionPressure = dataBook.createDataFormat();
        this.stylePressure = dataBook.createCellStyle();
        // настройка стиля
        this.stylePressure.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.stylePressure.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.stylePressure.setDataFormat(precisionPressure.getFormat(unitOutput[4].getPrecision()));              // точность

        // температуры
        DataFormat precisionTemperature = dataBook.createDataFormat();
        this.styleTemperature = dataBook.createCellStyle();
        // настройка стиля
        this.styleTemperature.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleTemperature.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleTemperature.setDataFormat(precisionTemperature.getFormat(unitOutput[5].getPrecision()));              // точность

        // безразмерные
        DataFormat precisionDimensionless_Mach = dataBook.createDataFormat();
        this.styleDimensionless_Mach = dataBook.createCellStyle();
        // настройка стиля
        this.styleDimensionless_Mach.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleDimensionless_Mach.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleDimensionless_Mach.setDataFormat(precisionDimensionless_Mach.getFormat(unitOutput[6].getPrecision()));              // точность

    }




}
