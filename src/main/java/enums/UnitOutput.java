package enums;

public enum UnitOutput {

    Meter("0.0", "м", 1), Kilometer("0.000", "км", 4),                                                        // длины метрические
    Foot("0.0", "фт", 3), Inch("0.0", "дюйм", 3),                                                             // длины имперские
    MeterPerSecond("0.000", "м/с", 5), KilometerPerHr("0.00", "км/ч", 5), Knot("0.0", "узел", 5),       // скорости
    Kelvin("0.0", "K", 3), Celsius("0.0", "C", 3), Fahrenheit("0.0", "F", 3),                        // температуры
    Pa("0.0", "Па", 3), KPa("0.0", "кПа", 3), KgsPerM2("0.0", "кг/м^2", 3),                          // давления
    KgPerM3("0.0000", "кг/м^3", 5),                                                                                       // плотности
    Dimensionless_Mach("0.0000", "-", 5);                                                                                             // безразмерные величины


    private String format;
    private String unitName;
    private int precision;

    // конструктор
    UnitOutput(String format, String name, int precision) {
        this.format = format;
        this.unitName = name;
        this.precision = precision;
    }

    // метод. возвращает количество разрядов в ячейке (формат строки)
    public String getPrecision() {
        return this.format;
    }

    // метод. возвращает наименование
    public String getUnitName () {
        return this.unitName;
    }

    // метод. возвращает точность округления для библиотечного метода округления
    public int getUnitPrecision() {
        return this.precision;
    }



}


