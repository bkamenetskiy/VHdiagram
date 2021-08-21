package solvers;

import enums.Unit;

public class SolverUnitOutput {

    // общий конвертер. конвертирует при экспорте в excel из внутренних единиц СИ в заданные пользователем

    public float getUnitOutput (Unit outputUnitType, float value) {

        switch (outputUnitType) {

            // длины
            case Meter:
                return value;
            case Kilometer:
                return value / 1000.0f;
            case Foot:
                return value * 3.280839895f;

            // скорости
            case MeterPerSecond:
                return value;
            case KilometerPerHr:
                return value * 60.0f * 60.0f / 1000.0f;
            case Knot:
                return value / 0.51444444444444f;

            // температуры
            case Kelvin:
                return value;
            case Celsius:
                return value - 273.15f;
            case Fahrenheit:
                return value * (9.0f/5.0f) - 459.67f;

            // давления
            case Pa:
                return value;
            case KPa:
                return value / 1000.0f;
            case KgsPerM2:
                return value / 9.81f;

            // Плотности
            case KgPerM3:
                return value;

            // Безразмерные величины
            case Dimensionless:
                return value;

            default:
                return -1.0f;
        }
    }



    // конвертер длинн
    public float getLenghtOutput (Unit unitType, float meter) {

        switch (unitType) {
            case Meter:
                return meter;
            case Kilometer:
                return meter / 1000.0f;
            case Foot:
                return meter * 3.280839895f;
            default:
                return meter;
        }
    }

    // конвертер скоростей
    public float getVelocityOutput (Unit unitType, float meterPerSecond) {

        switch (unitType) {
            case MeterPerSecond:
                return meterPerSecond;
            case KilometerPerHr:
                return meterPerSecond * 60.0f * 60.0f / 1000.0f;
            case Knot:
                return meterPerSecond / 0.51444444444444f;
            default:
                return meterPerSecond;
        }
    }

    // конвертер температур
    public float getTemperatureOutput (Unit unitType, float kelvin) {

        switch (unitType) {
            case Kelvin:
                return kelvin;
            case Celsius:
                return kelvin - 273.15f;
            case Fahrenheit:
                return kelvin * (9.0f/5.0f) - 459.67f;
            default:
                return kelvin;
        }
    }

    // конвертер давлений
    public float getPressureOutput (Unit unitType, float pa) {

        switch (unitType) {
            case Pa:
                return pa;
            case KPa:
                return pa / 1000.0f;
            case KgsPerM2:
                return pa / 9.81f;
            default:
                return pa;
        }
    }






}
