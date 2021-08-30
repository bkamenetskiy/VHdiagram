package models;

public final class ModelAtmosphere {

    final private double dens0 = 1.225;
    final private double press0 = 101325.0;

    private double altitude;

    // функция плотности от высоты (кг/м3)
    public double getDens() {
        double densH11, densRel;

        if (this.altitude > 11000.0) {
            densH11 = Math.pow(1.0-11000.0 / 44330.8 , 4.255879);
            densRel = (densH11 * Math.exp(-1.576888 * (this.altitude - 11000.0) / 10000.0));
        }
        else {
            densRel = Math.pow(1 - this.altitude / 44330.8 , 4.255879);
        }
        
        return densRel * this.dens0;
    }

    // функция атмосферного давления от высоты (Па)
    public double getPressStatic() {
        double pressH11, pressRel;

        if (this.altitude > 11000.0) {
            pressH11 = 0.2233612;
            pressRel = (pressH11 * Math.exp(-1.576888 * (this.altitude - 11000.0) / 10000.0));
        }
        else {
            pressRel = Math.pow(1 - this.altitude / 44330.8 , 5.255879);
        }
        return (pressRel * this.press0);
    }

    // функция температуры от высоты (K)
    public double getTemp() {
        double temp;

        if (this.altitude > 11000.0) {
            temp = 216.65;
        }
        else {
            temp = (288.15 - 6.5 * this.altitude / 1000.0);
        }
        return temp;
    }

    // функция скорости звука от высоты (м/с)
    public double getSoundVel() {
        double tempAbs;

        if (this.altitude > 11000.0) {
            tempAbs = 216.65;
        }
        else {
            tempAbs = (288.15 - 6.5 * this.altitude / 1000.0);
        }
        return (20.046796 * Math.sqrt(tempAbs));
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }


}
