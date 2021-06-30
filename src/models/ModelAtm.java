package models;

public final class ModelAtm {

    final private float dens0 = 1.225f;
    final private float press0 = 101325.0f;

    private float altitude;

    // функция плотности от высоты
    public float getDens() {
        float densH11, densRel;

        if (this.altitude > 11000.0) {
            densH11 = (float) Math.pow(1.0f-11000.0 / 44330.8 , 4.255879);
            densRel = (float) (densH11 * Math.exp(-1.576888 * (this.altitude - 11000.0) / 10000.0));
        }
        else {
            densRel = (float) Math.pow(1 - this.altitude / 44330.8 , 4.255879);
        }
        
        return (densRel * this.dens0);
    }

    // функция атмосферного давления от высоты
    public float getPressStatic() {
        float pressH11, pressRel;

        if (this.altitude > 11000.0) {
            pressH11 = (float) 0.2233612;
            pressRel = (float) (pressH11 * Math.exp(-1.576888 * (this.altitude - 11000.0) / 10000.0));
        }
        else {
            pressRel = (float) Math.pow(1 - this.altitude / 44330.8 , 5.255879);
        }

        return (pressRel * this.press0);
    }

    // функция температуры от высоты
    public float getTemp() {
        float temp;

        if (this.altitude > 11000.0) {
            temp = 216.65f;
        }
        else {
            temp = (288.15f - 6.5f * this.altitude / 1000.0f);
        }
        return temp;
    }

    // функция скорости звука от высоты
    public float getSoundVel() {
        float tempAbs;

        if (this.altitude > 11000.0) {
            tempAbs = 216.65f;
        }
        else {
            tempAbs = (288.15f - 6.5f * this.altitude / 1000.0f);
        }
        return (float) (20.046796 * Math.sqrt(tempAbs));
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }


}
