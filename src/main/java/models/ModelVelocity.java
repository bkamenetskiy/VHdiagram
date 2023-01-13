package models;

/**
 * Модель расчета скоростей.
 * Основные зависимости приведены в книгах: Гудков, Лешаков "Внешние нагрузки и прочность летательных аппаратов" и
 * Халенков "Нагрузки, действующие на самолет" (том 1)
 * Адаптирована в ЦАГИ. Для удобства определение истинной и индикаторной скорости проводится через соответствующее им число маха.
 */

public final class ModelVelocity {

    // скоростной напор
    public double getDynPress(double dens, double velocityTAS) {
        return ((dens * Math.pow(velocityTAS , 2)) / 2);
    }

    // пересчет индикаторной земной (Vcas) в число Маха
    public double getVcasToMach(double PressStatic, double casV) {
        double mach0, mach, dynPress, press;
        double pressRel = (PressStatic / 101325.0); // P0
        mach0 = (casV / 340.294); // soundVel0
        dynPress = (Math.pow(1.0 + 0.2 * Math.pow(mach0 , 2.0) , 3.5) - 1.0);
        press = dynPress / pressRel + 1;
        mach = Math.pow(5.0 * (Math.pow(press, (2.0 / 7.0)) - 1.0) , 0.5);
        return mach;
    }

    // пересчет числа Маха в истинную (Vtas)
    public double getMachToVtas(double soundVelocity, double mach) {
        return mach * soundVelocity;
    }

    // пересчет Vtas в индикаторную (Veas)
    public double getVtasToVeas(double dens, double tasV) {
        double dynPress = ((dens * Math.pow(tasV , 2.0)) / 2.0);
        return Math.sqrt((2.0 * dynPress) / 1.225);
    }

}
