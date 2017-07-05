/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

/**
 *
 * @author deangel
 */
public class Controlador {

    private double acumulado;
    private double dif;

    public Controlador() {
        this.acumulado = 0;
        this.dif = 0;
    }

    public double controladorP(double erro, double kp) {
        double saida;

        saida = (erro * kp);
        return saida;
    }

    public double controladorD(double erroAtual, double erroAntigo, double kd, double periodo) {
        double saida;

        saida = ((erroAtual - erroAntigo) / periodo) * kd;
        this.dif = saida;

        return saida;
    }

    public double controladorD_time(double erroAtual, double erroAntigo, double Td, double periodo) {
        double saida;

        saida = ((erroAtual - erroAntigo) / periodo) * Td;
        this.dif = saida;

        return saida;

    }

    public double controladorI(double erroAtual, double erroAntigo, double ki, double periodo, double metodoIntegral) {
        double saida;

        
        if (metodoIntegral < 1) {
            saida = ((erroAtual + erroAntigo) * periodo / 2) * ki + this.acumulado;
            this.acumulado = saida;
        } else {
            saida = ((erroAtual) * periodo) * ki + this.acumulado;
            this.acumulado = saida;
        }

        return saida;
    }

    public double controladorI_time(double erroAtual, double erroAntigo, double Ti, double periodo, double metodoIntegral) {
        double saida;

        if (metodoIntegral < 1) {
            saida = ((erroAtual + erroAntigo) * periodo / 2) * (1 / Ti) + this.acumulado;
            this.acumulado = saida;
        } else {
            saida = ((erroAtual) * periodo) * (1 / Ti) + this.acumulado;
            this.acumulado = saida;
        }
        return saida;
    }

    public double controlador_D(double alturaAtual, double alturaAntiga, double k_D, double periodo) {
        double saida;
        saida = ((alturaAtual - alturaAntiga) / periodo) * k_D;
        this.dif = saida;


        return saida;

    }

    public double controlador_D_time(double alturaAtual, double alturaAntiga, double T_D, double periodo) {
        double saida;
        saida = ((alturaAtual - alturaAntiga) / periodo) * T_D;
        this.dif = saida;

        return saida;

    }

    public double ControladorPD(double erroAtual, double erroAntigo, double kp, double kd, double periodo) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorD(erroAtual, erroAntigo, kd, periodo);
        return saida;
    }

    public double ControladorPD_time(double erroAtual, double erroAntigo, double kp, double Td, double periodo) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorD_time(erroAtual, erroAntigo, Td, periodo);
        return saida;
    }

    public double controladorPI(double erroAtual, double erroAntigo, double kp, double ki, double periodo, double metodoIntegral) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorI(erroAtual, erroAntigo, ki, periodo, metodoIntegral);

        return saida;
    }

    public double controladorPI_time(double erroAtual, double erroAntigo, double kp, double Ti, double periodo, double metodoIntegral) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorI_time(erroAtual, erroAntigo, Ti, periodo, metodoIntegral);

        return saida;
    }

    public double controladorPID(double erroAtual, double erroAntigo, double kp, double ki, double kd, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorI(erroAtual, erroAntigo, ki, periodo, metodoIntegral) + this.controladorD(erroAtual, erroAntigo, kd, periodo);

        return saida;
    }

    public double controladorPID_time(double erroAtual, double erroAntigo, double kp, double Ti, double Td, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorI_time(erroAtual, erroAntigo, Ti, periodo, metodoIntegral) + this.controladorD_time(erroAtual, erroAntigo, Td, periodo);

        return saida;
    }

    public double controladorPI_D(double erroAtual, double erroAntigo, double alturaAtual, double alturaAntiga, double kp, double ki, double k_d, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorI(erroAtual, erroAntigo, ki, periodo, metodoIntegral) + this.controlador_D(alturaAtual, alturaAntiga, k_d, periodo);

        return saida;
    }

    public double controladorPI_D_time(double erroAtual, double erroAntigo, double alturaAtual, double alturaAntiga, double kp, double Ti, double T_d, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorI_time(erroAtual, erroAntigo, Ti, periodo, metodoIntegral) + this.controlador_D_time(alturaAtual, alturaAntiga, T_d, periodo);

        return saida;
    }

    public double getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(double acumulado) {
        this.acumulado = acumulado;
    }

    public double getDif() {
        return dif;
    }

    public void setDif(double dif) {
        this.dif = dif;
    }

    public void reset() {
        this.acumulado = 0;
        this.dif = 0;
    }
}
