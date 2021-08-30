package solvers;

import enums.UnitInput;

public class SolverUnitInput {

    // общий конвертер. конвертирует при экспорте в excel из внутренних единиц СИ в заданные пользователем

    public double getUnitInput (UnitInput inputUnitType, double value) {

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
