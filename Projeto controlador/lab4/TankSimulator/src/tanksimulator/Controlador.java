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

    private double acumulado1, acumulado2;
    private double dif1, dif2;
    private double erroCascataMestreAtual, erroCascataMestreAntigo, erroCascataEscravoAtual, erroCascataEscravoAntigo;
    private double nivelDoTanque1Antigo, nivelDoTanque2Antigo;
    private double saidaMestre, saidaEscravo;
    double[] PVector;
    
    public Controlador() {
        this.acumulado1 = 0;
        this.dif1 = 0;
        this.erroCascataMestreAtual = 0;
        this.erroCascataMestreAntigo = 0;
        this.erroCascataEscravoAtual = 0;
        this.erroCascataEscravoAntigo = 0;
        this.nivelDoTanque1Antigo = 0;
        this.nivelDoTanque2Antigo = 0;
        this.saidaMestre = 0;
        this.saidaEscravo = 0;
        
    }

    public double[] getPVector() {
        return PVector;
    }

    public void setPVector(double[] PVector) {
        this.PVector = PVector;
    }

    
    
    public double controladorP(double erro, double kp) {
        double saida;

        saida = (erro * kp);
        PVector[29] = saida;
        return saida;
    }

    public double controladorD(double erroAtual, double erroAntigo, double kd, double periodo) {
        double saida;

        saida = ((erroAtual - erroAntigo) / periodo) * kd;
        this.dif1 = saida;

        PVector[31] = saida;
        return saida;
    }
    
    public double controladorDEscravo(double erroAtual, double erroAntigo, double kd, double periodo) {
        double saida;

        saida = ((erroAtual - erroAntigo) / periodo) * kd;
        this.dif2 = saida;

        return saida;
    }


    public double controladorD_time(double erroAtual, double erroAntigo, double Td, double periodo) {
        double saida;

        saida = ((erroAtual - erroAntigo) / periodo) * Td;
        this.dif1 = saida;

        return saida;

    }

    public double controladorI(double erroAtual, double erroAntigo, double ki, double periodo, double metodoIntegral) {
        double saida;


        if (metodoIntegral < 1) {
            saida = ((erroAtual + erroAntigo) * periodo / 2) * ki + this.acumulado1;
            this.acumulado1 = saida;
        } else {
            saida = ((erroAtual) * periodo) * ki + this.acumulado1;
            this.acumulado1 = saida;
        }

        PVector[30] = saida;
        return saida;
    }
    
    public double controladorIEscravo(double erroAtual, double erroAntigo, double ki, double periodo, double metodoIntegral) {
        double saida;


        if (metodoIntegral < 1) {
            saida = ((erroAtual + erroAntigo) * periodo / 2) * ki + this.acumulado2;
            this.acumulado2 = saida;
        } else {
            saida = ((erroAtual) * periodo) * ki + this.acumulado2;
            this.acumulado2 = saida;
        }

        return saida;
    }


    public double controladorI_time(double erroAtual, double erroAntigo, double Ti, double periodo, double metodoIntegral) {
        double saida;

        if (metodoIntegral < 1) {
            saida = ((erroAtual + erroAntigo) * periodo / 2) * (1 / Ti) + this.acumulado1;
            this.acumulado1 = saida;
        } else {
            saida = ((erroAtual) * periodo) * (1 / Ti) + this.acumulado1;
            this.acumulado1 = saida;
        }
        return saida;
    }

    public double controlador_D(double alturaAtual, double alturaAntiga, double k_D, double periodo) {
        double saida;
        saida = ((alturaAtual - alturaAntiga) / periodo) * k_D;
        this.dif1 = saida;


        return saida;

    }
    
    public double controlador_DEscravo(double alturaAtual, double alturaAntiga, double k_D, double periodo) {
        double saida;
        saida = ((alturaAtual - alturaAntiga) / periodo) * k_D;
        this.dif2 = saida;


        return saida;

    }


    public double controlador_D_time(double alturaAtual, double alturaAntiga, double T_D, double periodo) {
        double saida;
        saida = ((alturaAtual - alturaAntiga) / periodo) * T_D;
        this.dif1 = saida;

        return saida;

    }

    public double ControladorPD(double erroAtual, double erroAntigo, double kp, double kd, double periodo) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorD(erroAtual, erroAntigo, kd, periodo);
        return saida;
    }
    
    public double ControladorPDEscravo(double erroAtual, double erroAntigo, double kp, double kd, double periodo) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorDEscravo(erroAtual, erroAntigo, kd, periodo);
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
    
   public double controladorPIEscravo(double erroAtual, double erroAntigo, double kp, double ki, double periodo, double metodoIntegral) {
        double saida;
        saida = this.controladorP(erroAtual, kp) + this.controladorIEscravo(erroAtual, erroAntigo, ki, periodo, metodoIntegral);

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
    
   public double controladorPIDEscravo(double erroAtual, double erroAntigo, double kp, double ki, double kd, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorIEscravo(erroAtual, erroAntigo, ki, periodo, metodoIntegral) + this.controladorDEscravo(erroAtual, erroAntigo, kd, periodo);

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
    
        public double controladorPI_DEscravo(double erroAtual, double erroAntigo, double alturaAtual, double alturaAntiga, double kp, double ki, double k_d, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorIEscravo(erroAtual, erroAntigo, ki, periodo, metodoIntegral) + this.controlador_DEscravo(alturaAtual, alturaAntiga, k_d, periodo);

        return saida;
    }


    public double controladorPI_D_time(double erroAtual, double erroAntigo, double alturaAtual, double alturaAntiga, double kp, double Ti, double T_d, double periodo, double metodoIntegral) {
        double saida;

        saida = this.controladorP(erroAtual, kp) + this.controladorI_time(erroAtual, erroAntigo, Ti, periodo, metodoIntegral) + this.controlador_D_time(alturaAtual, alturaAntiga, T_d, periodo);

        return saida;
    }

    public double controladorCascataMestre(double kp_mestre, double ki_mestre, double kd_mestre,
            int tipoControleMestre, double nivelTanque2, double setpoint, double metodoDaIntegral) {
        this.erroCascataMestreAtual = setpoint - nivelTanque2;
        
        if (tipoControleMestre == 0) { //controlador P
            saidaMestre = this.controladorP(erroCascataMestreAtual, kp_mestre);
        } else if (tipoControleMestre == 1) { //controlador PI
            saidaMestre = this.controladorPI(erroCascataMestreAtual, erroCascataMestreAntigo, kp_mestre, ki_mestre, 0.1, metodoDaIntegral);
        } else if (tipoControleMestre == 2) { //controlador PD
            saidaMestre = this.ControladorPD(erroCascataMestreAtual, erroCascataMestreAntigo, kp_mestre, kd_mestre, 0.1);
        } else if (tipoControleMestre == 3) { // controlador PID
            saidaMestre = this.controladorPID(erroCascataMestreAtual, erroCascataMestreAntigo, kp_mestre, ki_mestre,
                    kd_mestre, 0.1, metodoDaIntegral);
        } else if (tipoControleMestre == 4) {//PI-D
            saidaMestre = this.controladorPI_D(erroCascataMestreAtual, erroCascataMestreAntigo, nivelTanque2, nivelDoTanque2Antigo, kp_mestre, ki_mestre, kd_mestre, 0.1, metodoDaIntegral);
        }
        this.nivelDoTanque2Antigo = nivelTanque2;
        this.erroCascataMestreAntigo = this.erroCascataMestreAtual;
        return saidaMestre;

    }
    
        public double controladorCascataEscravo(double kp_escravo, double ki_escravo, double kd_escravo,
            int tipoControleEscravo, double nivelTanque1, double saidaMestre, double metodoDaIntegral) {
        this.erroCascataEscravoAtual = saidaMestre -  nivelTanque1;
        

        if (tipoControleEscravo == 0) { //controlador P
            saidaEscravo =  this.controladorP(erroCascataEscravoAtual, kp_escravo);
        } else if (tipoControleEscravo == 1) { //controlador PI
            saidaEscravo = this.controladorPIEscravo(erroCascataEscravoAtual, erroCascataEscravoAntigo, kp_escravo, ki_escravo, 0.1, metodoDaIntegral);
        } else if (tipoControleEscravo == 2) { //controlador PD
            saidaEscravo = this.ControladorPDEscravo(erroCascataEscravoAtual, erroCascataEscravoAntigo, kp_escravo, kd_escravo, 0.1);
        } else if (tipoControleEscravo == 3) { // controlador PID
            saidaEscravo = this.controladorPIDEscravo(erroCascataEscravoAtual, erroCascataEscravoAntigo, kp_escravo, ki_escravo,
                    kd_escravo, 0.1, metodoDaIntegral);
        } else if (tipoControleEscravo == 4) {//PI-D
            saidaEscravo = this.controladorPI_DEscravo(erroCascataEscravoAtual, erroCascataEscravoAntigo, nivelTanque1, nivelDoTanque2Antigo, kp_escravo, ki_escravo, kd_escravo, 0.1, metodoDaIntegral);
        }
        this.nivelDoTanque1Antigo = nivelTanque1;
        this.erroCascataEscravoAntigo = this.erroCascataEscravoAtual;
        return saidaEscravo;
    }
        
   public double controladorCascataCompleto(double kp_mestre, double ki_mestre, double kd_mestre,
            int tipoControleMestre, double nivelTanque2, double setpoint, double metodoDaIntegral,
            double kp_escravo, double ki_escravo, double kd_escravo,
            int tipoControleEscravo, double nivelTanque1){
       
       
   
       this.controladorCascataMestre(kp_mestre, ki_mestre, kd_mestre, tipoControleMestre, nivelTanque2, setpoint, metodoDaIntegral);
       
       System.out.println("saidaMestre: " + saidaMestre);
       
       return this.controladorCascataEscravo(kp_escravo, ki_escravo, kd_escravo, tipoControleEscravo, nivelTanque1, this.saidaMestre, metodoDaIntegral);
   }     

    public double getAcumulado1() {
        return acumulado1;
    }

    public void setAcumulado1(double acumulado1) {
        this.acumulado1 = acumulado1;
    }

    public double getDif1() {
        return dif1;
    }

    public void setDif1(double dif1) {
        this.dif1 = dif1;
    }

    public void reset() {
        this.acumulado1 = 0;
        this.dif1 = 0;
    }

    public double getErroCascataEscravoAntigo() {
        return erroCascataEscravoAntigo;
    }

    public void setErroCascataEscravoAntigo(double erroCascataEscravoAntigo) {
        this.erroCascataEscravoAntigo = erroCascataEscravoAntigo;
    }

    public double getErroCascataEscravoAtual() {
        return erroCascataEscravoAtual;
    }

    public void setErroCascataEscravoAtual(double erroCascataEscravoAtual) {
        this.erroCascataEscravoAtual = erroCascataEscravoAtual;
    }

    public double getErroCascataMestreAntigo() {
        return erroCascataMestreAntigo;
    }

    public void setErroCascataMestreAntigo(double erroCascataMestreAntigo) {
        this.erroCascataMestreAntigo = erroCascataMestreAntigo;
    }

    public double getErroCascataMestreAtual() {
        return erroCascataMestreAtual;
    }

    public void setErroCascataMestreAtual(double erroCascataMestreAtual) {
        this.erroCascataMestreAtual = erroCascataMestreAtual;
    }

    public double getNivelDoTanque1Antigo() {
        return nivelDoTanque1Antigo;
    }

    public void setNivelDoTanque1Antigo(double nivelDoTanque1Antigo) {
        this.nivelDoTanque1Antigo = nivelDoTanque1Antigo;
    }

    public double getNivelDoTanque2Antigo() {
        return nivelDoTanque2Antigo;
    }

    public void setNivelDoTanque2Antigo(double nivelDoTanque2Antigo) {
        this.nivelDoTanque2Antigo = nivelDoTanque2Antigo;
    }

    public double getSaidaEscravo() {
        return saidaEscravo;
    }

    public void setSaidaEscravo(double saidaEscravo) {
        this.saidaEscravo = saidaEscravo;
    }

    public double getSaidaMestre() {
        return saidaMestre;
    }

    public void setSaidaMestre(double saidaMestre) {
        this.saidaMestre = saidaMestre;
    }

    public double getAcumulado2() {
        return acumulado2;
    }

    public void setAcumulado2(double acumulado2) {
        this.acumulado2 = acumulado2;
    }

    public double getDif2() {
        return dif2;
    }

    public void setDif2(double dif2) {
        this.dif2 = dif2;
    }
    
    
}
