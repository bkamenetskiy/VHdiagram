package enums;

public enum Unit {

    Meter("0.0", "м"), Kilometer("0.000", "км"),                                                        // длины метрические
    Foot("0.0", "фт"), Inch("0.0", "дюйм"),                                                             // длины имперские
    MeterPerSecond("0.000", "м/с"), KilometerPerHr("0.00", "км/ч"), Knot("0.0", "узел"),       // скорости
    Kelvin("0.0", "K"), Celsius("0.0", "C"), Fahrenheit("0.0", "F"),                        // температуры
    Pa("0.0", "Па"), KPa("0.0", "кПа"), KgsPerM2("0.0", "кг/м^2"),                          // давления
    KgPerM3("0.0000", "кг/м^3"),                                                                                       // плотности
    Dimensionless_Mach("0.000", "-");                                                                                             // безразмерные величины


    private String format;
    private String unitName;

    // конструктор
    Unit(String format, String name) {
        this.format = format;
        this.unitName = name;
    }

    // метод. возвращает количество разрядов в ячейке
    public String getPrecision() {
        return this.format;
    }

    // метод. возвращает наименование
    public String getUnitName () {
        return this.unitName;
    }




}


