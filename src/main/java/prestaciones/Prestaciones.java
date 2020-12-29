package prestaciones;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.sql.Date;

import models.Empleado;

/**
 * Objeto de prestaciones. Basado en
 * https://www.dominicanaenlaweb.com/calculo-prestaciones-laborales-republica-dominicana/.
 *
 */
public class Prestaciones {
    public double Preaviso;
    public double Cesantia;
    public double Vacaciones;
    public double SalarioNavidad;

    public Prestaciones(Date fechaDeContratacion, Date fechaDeSalida, double salarioMensual) {
        double salarioxdia = salarioMensual / (double) 23.83;
        long meses = Period.between(fechaDeContratacion.toLocalDate(), LocalDate.now()).toTotalMonths();
        Preaviso = calcPreaviso(salarioxdia, meses);
        Cesantia = calcCesantia(salarioxdia, meses);
        Vacaciones = calcVacaciones(salarioxdia, meses);
        SalarioNavidad = calcSalarioNavidad(salarioMensual, fechaDeSalida);
    }

    private double calcPreaviso(double salarioxdia, long meses) {
        double d = 0.0;
        if (meses >= 3 && meses < 6) {
            d = salarioxdia * (double) 7;
        } else if (meses >= 6 && meses < 12) {
            d = salarioxdia * (double) 14;
        } else if (meses >= 12) {
            d = salarioxdia * (double) 28;
        }
        return d;
    }

    private double calcCesantia(double salarioxdia, long meses) {
        double ces = 0.0;
        if (meses >= 3 && meses < 6) {
            ces = salarioxdia * (double) 6;
        } else if (meses >= 6 && meses < 12) {
            ces = salarioxdia * (double) 13;
        } else if (meses >= 12 && meses < 60) {
            ces = salarioxdia * (double) 21;
        } else if (meses >= 60) {
            ces = salarioxdia * (double) 23 * meses / (double) 12;
        }
        return ces;
    }

    private double calcVacaciones(double salarioxdia, long meses) {
        double vac = 0.0;
        if (meses >= 12 && meses < 60) {
            vac = salarioxdia * (double) 14;
        } else if (meses >= 60) {
            vac = salarioxdia * (double) 18;
        }
        return vac;
    }

    private double calcSalarioNavidad(double salarioMensual, Date fechaSalida) {
        int mesActual = fechaSalida.toLocalDate().getMonthValue();
        return (double) mesActual * salarioMensual / (double) 12;
    }

}