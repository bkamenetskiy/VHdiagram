package solvers;

import enums.UnitInput;
import enums.UnitOutput;

/**
 * Конвертер единиц измерения. Общий для входящих и исходящих данных.
 * Все внутренние рассчеты проводятся в единицах СИ. Конвертер приводит исходные данные к СИ.
 * Единицы измерения исходящих данных задаются пользователем и внутренние рассчеты в СИ переводятся в них.
 */

public class SolverUnitConverter {

    // конвертирует при экспорте в excel из внутренних единиц СИ в заданные пользователем
    public double getSIToUnitOutput(UnitOutput outputUnitType, double value) {

        switch (outputUnitType) {

            // длины
            case Meter:
                return value;
            case Kilometer:
                return value / 1000.0;
            case Foot:
                return value * 3.280839895;

            // скорости
            case MeterPerSecond:
                return value;
            case KilometerPerHr:
                return value * 60.0 * 60.0 / 1000.0;
            case Knot:
                return value / 0.51444444444444;

            // температуры
            case Kelvin:
                return value;
            case Celsius:
                return value - 273.15;
            case Fahrenheit:
                return value * (9.0/5.0) - 459.67;

            // давления
            case Pa:
                return value;
            case KPa:
                return value / 1000.0;
            case KgsPerM2:
                return value / 9.81;

            // Плотности
            case KgPerM3:
                return value;

            // Безразмерные величины
            case Dimensionless_Mach:
                return value;

            default:
                return -1.0;
        }
    }

    // конвертирует исходные данные в СИ
    public double getUnitInputToSI (UnitInput inputUnitType, double value) {

        switch (inputUnitType) {

            // длины
            case Meter:
                return value;
            case Kilometer:
                return value * 1000.0;
            case Foot:
                return value / 3.280839895;

            // скорости
            case MeterPerSecond:
                return value;
            case KilometerPerHr:
                return value * 1000.0 / 60.0 / 60.0;
            case Knot:
                return value * 0.51444444444444;

            default:
                return -1.0;
        }
    }
}
