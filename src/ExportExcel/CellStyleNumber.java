package exportexcel;

import enums.Unit;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class CellStyleNumber {

    // стили
    private CellStyle styleLength;          // длины
    private DataFormat precisionLength;

    private CellStyle styleLength_IS;          // длины имперские
    private DataFormat precisionLength_IS;

    private CellStyle styleVelocity;          // скорости
    private DataFormat precisionVelocity;

    private CellStyle styleDensity;          // плотность
    private DataFormat precisionDensity;

    private CellStyle stylePressure;          // давление
    private DataFormat precisionPressure;

    private CellStyle styleTemperature;          // температура
    private DataFormat precisionTemperature;

    private CellStyle styleDimensionless_Mach;          // безразмерные
    private DataFormat precisionDimensionless_Mach;

    // сопоставление единицы измерения и стиля ячейки

    protected CellStyle getUnitStyle (Unit unit) {

        CellStyle style = null;

        if ((unit == Unit.Meter) || (unit == Unit.Kilometer)) {

            style = this.styleLength;
        }

        if ((unit == Unit.Foot) || (unit == Unit.Inch)) {

            style = this.styleLength_IS;
        }

        if ((unit == Unit.MeterPerSecond) || (unit == Unit.KilometerPerHr) || (unit == Unit.Knot)) {

            style = this.styleVelocity;
        }

        if ((unit == Unit.Kelvin) || (unit == Unit.Celsius) || (unit == Unit.Fahrenheit)) {

            style = this.styleTemperature;
        }

        if ((unit == Unit.Pa) || (unit == Unit.KPa) || (unit == Unit.KgsPerM2)) {

            style = this.stylePressure;
        }

        if (unit == Unit.KgPerM3) {

            style = this.styleDensity;
        }

        if (unit == Unit.Dimensionless_Mach) {

            style = this.styleDimensionless_Mach;
        }

        return style;
    }

    // создание и настройка стилей
    protected void createCellStyle(XSSFWorkbook dataBook, Unit[] unitOutput) {

        // длины
        this.precisionLength = dataBook.createDataFormat();
        this.styleLength = dataBook.createCellStyle();
        // настройка стиля
        this.styleLength.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleLength.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleLength.setDataFormat(this.precisionLength.getFormat(unitOutput[0].getPrecision()));              // точность

        // длины имперские
        this.precisionLength_IS = dataBook.createDataFormat();
        this.styleLength_IS = dataBook.createCellStyle();
        // настройка стиля
        this.styleLength_IS.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleLength_IS.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleLength_IS.setDataFormat(this.precisionLength_IS.getFormat(unitOutput[1].getPrecision()));              // точность

        // скорости
        this.precisionVelocity = dataBook.createDataFormat();
        this.styleVelocity = dataBook.createCellStyle();
        // настройка стиля
        this.styleVelocity.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleVelocity.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleVelocity.setDataFormat(this.precisionVelocity.getFormat(unitOutput[2].getPrecision()));              // точность

        // плотности
        this.precisionDensity = dataBook.createDataFormat();
        this.styleDensity = dataBook.createCellStyle();
        // настройка стиля
        this.styleDensity.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleDensity.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleDensity.setDataFormat(this.precisionDensity.getFormat(unitOutput[3].getPrecision()));              // точность

        // давления
        this.precisionPressure = dataBook.createDataFormat();
        this.stylePressure = dataBook.createCellStyle();
        // настройка стиля
        this.stylePressure.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.stylePressure.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.stylePressure.setDataFormat(this.precisionPressure.getFormat(unitOutput[4].getPrecision()));              // точность

        // температуры
        this.precisionTemperature = dataBook.createDataFormat();
        this.styleTemperature = dataBook.createCellStyle();
        // настройка стиля
        this.styleTemperature.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleTemperature.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleTemperature.setDataFormat(this.precisionTemperature.getFormat(unitOutput[5].getPrecision()));              // точность

        // безразмерные
        this.precisionDimensionless_Mach = dataBook.createDataFormat();
        this.styleDimensionless_Mach = dataBook.createCellStyle();
        // настройка стиля
        this.styleDimensionless_Mach.setAlignment(HorizontalAlignment.CENTER);                                                      // горизонтальноен выравнивание
        this.styleDimensionless_Mach.setVerticalAlignment(VerticalAlignment.CENTER);                                                // вертикальное выравнивание
        this.styleDimensionless_Mach.setDataFormat(this.precisionDimensionless_Mach.getFormat(unitOutput[6].getPrecision()));              // точность

    }




}
