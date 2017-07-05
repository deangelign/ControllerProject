/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

import java.text.DecimalFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.commons.math3.analysis.function.Sqrt;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author deangel
 */
//importante matrizes
/**
 *
 * L = |L1| |L2|
 *
 * C = |C1 C2|
 *
 * LC = |L1C1 L1C2| |L2C1 L2C2|
 *
 * G = |G11 G12| |G21 G22|
 *
 * G - LC = |G11 - L1C1 G12 - L1C2| |G21 - L2C1 G22 - L2C2|
 *
 * Xtil = |nivel_real_do_tanque1 - nivel_estimado_do_tanque1_anterior |
 * |nivel_real_do_tanque2 - nivel_estimado_do_tanque2_anterior |
 *
 * Xtil = |x1til| |x2til|
 *
 * L1_estimado = (G11 - L1C1)(x1til) + (G12 - L1C2)(x2til) L2_estimado - (G21 -
 * L2C1)(x1til) + (G22 - L2C2)(x2til)
 *
 */
public class Estimador {

    Complex P1, P2, aux;
    double parteRealP1, parteImaginariaP1, parteRealP2, parteImaginariaP2;
    double valorEstimadoDoTanque1Atual, valorEstimadoDoTanque1Anterior;
    double valorEstimadoDoTanque2Atual, valorEstimadoDoTanque2Anterior;
    double valorAlturaDoTanque1, valorAlturaDoTanque2;
    double erroAlturaTanque1, erroAlturaTanque2;
    double L1, L2;
    double C1, C2;
    double G11, G12, G21, G22;
    double H1, H2;
    DecimalFormat decimalFormat;

    public Estimador(Complex P1, Complex P2, double parteRealP1, double parteImaginariaP1, double parteRealP2, double parteImaginariaP2, double valorEstimadoDoTanque1Atual, double valorEstimadoDoTanque1Anterior, double valorEstimadoDoTanque2Atual, double valorEstimadoDoTanque2Anterior, double valorAlturaDoTanque1, double valorAlturaDoTanque2, double erroAlturaTanque1, double erroAlturaTanque2, double L1, double L2, double C1, double C2, double G11, double G12, double G21, double G22) {
        this.P1 = P1;
        this.P2 = P2;
        this.parteRealP1 = parteRealP1;
        this.parteImaginariaP1 = parteImaginariaP1;
        this.parteRealP2 = parteRealP2;
        this.parteImaginariaP2 = parteImaginariaP2;
        this.valorEstimadoDoTanque1Atual = valorEstimadoDoTanque1Atual;
        this.valorEstimadoDoTanque1Anterior = valorEstimadoDoTanque1Anterior;
        this.valorEstimadoDoTanque2Atual = valorEstimadoDoTanque2Atual;
        this.valorEstimadoDoTanque2Anterior = valorEstimadoDoTanque2Anterior;
        this.valorAlturaDoTanque1 = valorAlturaDoTanque1;
        this.valorAlturaDoTanque2 = valorAlturaDoTanque2;
        this.erroAlturaTanque1 = erroAlturaTanque1;
        this.erroAlturaTanque2 = erroAlturaTanque2;
        this.L1 = L1;
        this.L2 = L2;
        this.C1 = C1;
        this.C2 = C2;
        this.G11 = G11;
        this.G12 = G12;
        this.G21 = G21;
        this.G22 = G22;

        this.aux = new Complex(0, 0);
    }

    public Estimador() {
        this.parteImaginariaP1 = 0;
        this.parteRealP1 = 0;

        this.parteImaginariaP2 = 0;
        this.parteRealP2 = 0;

        this.valorEstimadoDoTanque1Atual = 0;
        this.valorEstimadoDoTanque1Anterior = 0;
        this.valorEstimadoDoTanque2Atual = 0;
        this.valorEstimadoDoTanque2Anterior = 0;
        this.valorAlturaDoTanque1 = 0;
        this.valorAlturaDoTanque2 = 0;
        this.erroAlturaTanque1 = 0;
        this.erroAlturaTanque2 = 0;
        this.L1 = 0;
        this.L2 = 0;
        this.C1 = 0;
        this.C2 = 0;
        this.G11 = 0;
        this.G12 = 0;
        this.G21 = 0;
        this.G22 = 0;

        this.P1 = new Complex(0, 0);
        this.P2 = new Complex(0, 0);
        this.aux = new Complex(0, 0);
        this.H1 = 0;
        this.H2 = 0;

        Locale.setDefault(Locale.US);
        decimalFormat = new DecimalFormat("0.0000000");


    }

    //inicializar as matrizes exceto a matriz L
    public void inicializarMatrizes() {
        this.C1 = 0;
        this.C2 = 1;

        this.G11 = 0.99345901778;
        this.G12 = 0;
        this.G21 = 6.52153009329 * Math.pow(10, -3);
        this.G22 = 0.99345701778;

        this.H1 = 0.029546120506;
        this.H2 = 0.00009685363;
    }

    public void determinarAMatrizL() {



        this.aux = new Complex(0, 0);

        this.L1 = ((this.P1.add(P2)).multiply(152.8395).add((this.P1.multiply(this.P2)).multiply(-153.8462).add(151.8395))).abs();
        this.L2 = (((this.P1.add(P2)).multiply(-1.0033)).add(1.9935)).abs();

    }

    public double getL2() {
        return L2;
    }

    public void setL2(double L2) {
        this.L2 = L2;
    }

    public void updateComplex() {
        this.P1 = new Complex(this.getParteRealP1(), this.getParteImaginariaP1());
        this.P2 = new Complex(this.getParteRealP2(), this.getParteImaginariaP2());
    }

    public void calcularEstimacaoDoNivel(double nivelDoTanque2, double valorTensaoNaBomba) {
        //calcula o erro

        double deltaY = nivelDoTanque2 - valorEstimadoDoTanque2Anterior;

        this.valorEstimadoDoTanque1Atual = (G11 * valorEstimadoDoTanque1Anterior + G12 * valorEstimadoDoTanque1Anterior) + (L1 * deltaY) + H1 * valorTensaoNaBomba;
        this.valorEstimadoDoTanque2Atual = (G21 * valorEstimadoDoTanque1Anterior + G22 * valorEstimadoDoTanque2Anterior) + (L2 * deltaY) + H2 * valorTensaoNaBomba;

        this.valorEstimadoDoTanque1Anterior = this.valorEstimadoDoTanque1Atual;
        this.valorEstimadoDoTanque2Anterior = this.valorEstimadoDoTanque2Atual;
    }

    public void calculaPolos(double L_superior, double L_inferior, JTextField P1real, JTextField P1Imaginario, JTextField P2real, JTextField P2Imaginario) {

        double A, B, C, D, E, F, K1, K2, delta;

        Complex R1, R2;

        /*
         * A = 152.8395; B = 153.8462; C = 151.8395; E = 1.9935; D = 1.0033; K1
         * = (-L_inferior + E) / D; K2 = ((-A * L_inferior) / (D * B)) + ((A *
         * E) / (D * B)) + (L_superior / B) - (C/B);
         *
         * delta = (K1 * K1) - 4 * K2; if (delta >= 0) { R1 = new Complex((-K1 /
         * 2) + (Math.sqrt(delta) / 2), 0); R2 = new Complex((-K1 / 2) -
         * (Math.sqrt(delta) / 2), 0); } else { delta = -delta; R1 = new
         * Complex((-K1 / 2), (Math.sqrt(delta) / 2)); R2 = new Complex((-K1 /
         * 2), (-Math.sqrt(delta) / 2)); }
         *
         * System.out.println("R1: " + R1.getReal() + " + j" +
         * R1.getImaginary()); System.out.println("R2: " + R2.getReal() + " + j"
         * + R2.getImaginary()); if (R1.abs() < 1) { }
         */


        double a = 153.366196623, b, c;
        double p[] = new double[4];
        b = -153.366196623 * (L_inferior - 1.98691642808);
        c = L_superior - 151.366182264 - 152.362907793 * (L_inferior - 1.98691642808);

        if ((b * b - 4 * a * c) < 0) {
            p[2] = ((-1) * b) / (2 * a);
            p[3] = Math.sqrt(Math.abs(b * b - 4 * a * c)) / (2 * a);
            p[0] = L_inferior - 1.98691642808 - p[2];
            p[1] = (-1) * p[3];
            System.out.println(p[0] + " + j" + p[1] + "\n" + p[2] + " + j" + p[3]);
//            System.out.println("Error");
        } else {
            p[2] = ((-1) * b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
            p[3] = 0;
            p[1] = 0;
            p[0] = L_inferior - 1.98691642808 - p[2];

            System.out.println(p[0] + "\n" + p[2]);
        }

        System.out.println("");
        //JOptionPane.showMessageDialog(null, "Valor fora do circulo unitario");
        //if ((p[0] * p[0]) + (p[1] * p[1]) <= 1 && (p[2] * p[2]) + (p[3] * p[3]) <= 1) {
            P1real.setText(decimalFormat.format(p[0]));
            P1Imaginario.setText(decimalFormat.format(p[1]));
            P2real.setText(decimalFormat.format(p[2]));
            P2Imaginario.setText(decimalFormat.format(p[3]));
            
//            P1real.setText(Double.toString(p[0]));
//            P1Imaginario.setText(Double.toString(p[1]));
//            P2real.setText(Double.toString(p[2]));
//            P2Imaginario.setText(Double.toString(p[3]));
            
        //}

//        return true;
    }
    
    public void reset(){
        this.valorEstimadoDoTanque1Anterior = 0;
        this.valorEstimadoDoTanque2Anterior = 0;
    }

    public double getC1() {
        return C1;
    }

    public void setC1(double C1) {
        this.C1 = C1;
    }

    public double getC2() {
        return C2;
    }

    public void setC2(double C2) {
        this.C2 = C2;
    }

    public double getG11() {
        return G11;
    }

    public void setG11(double G11) {
        this.G11 = G11;
    }

    public double getG12() {
        return G12;
    }

    public void setG12(double G12) {
        this.G12 = G12;
    }

    public double getG21() {
        return G21;
    }

    public void setG21(double G21) {
        this.G21 = G21;
    }

    public double getG22() {
        return G22;
    }

    public void setG22(double G22) {
        this.G22 = G22;
    }

    public double getL1() {
        return L1;
    }

    public void setL1(double L1) {
        this.L1 = L1;
    }

    public Complex getP1() {
        return P1;
    }

    public void setP1(Complex P1) {
        this.P1 = P1;
    }

    public Complex getP2() {
        return P2;
    }

    public void setP2(Complex P2) {
        this.P2 = P2;
    }

    public Complex getAux() {
        return aux;
    }

    public void setAux(Complex aux) {
        this.aux = aux;
    }

    public double getErroAlturaTanque1() {
        return erroAlturaTanque1;
    }

    public void setErroAlturaTanque1(double erroAlturaTanque1) {
        this.erroAlturaTanque1 = erroAlturaTanque1;
    }

    public double getErroAlturaTanque2() {
        return erroAlturaTanque2;
    }

    public void setErroAlturaTanque2(double erroAlturaTanque2) {
        this.erroAlturaTanque2 = erroAlturaTanque2;
    }

    public double getParteImaginariaP1() {
        return parteImaginariaP1;
    }

    public void setParteImaginariaP1(double parteImaginariaP1) {
        this.parteImaginariaP1 = parteImaginariaP1;
    }

    public double getParteImaginariaP2() {
        return parteImaginariaP2;
    }

    public void setParteImaginariaP2(double parteImaginariaP2) {
        this.parteImaginariaP2 = parteImaginariaP2;
    }

    public double getParteRealP1() {
        return parteRealP1;
    }

    public void setParteRealP1(double parteRealP1) {
        this.parteRealP1 = parteRealP1;
    }

    public double getParteRealP2() {
        return parteRealP2;
    }

    public void setParteRealP2(double parteRealP2) {
        this.parteRealP2 = parteRealP2;
    }

    public double getValorAlturaDoTanque1() {
        return valorAlturaDoTanque1;
    }

    public void setValorAlturaDoTanque1(double valorAlturaDoTanque1) {
        this.valorAlturaDoTanque1 = valorAlturaDoTanque1;
    }

    public double getValorAlturaDoTanque2() {
        return valorAlturaDoTanque2;
    }

    public void setValorAlturaDoTanque2(double valorAlturaDoTanque2) {
        this.valorAlturaDoTanque2 = valorAlturaDoTanque2;
    }

    public double getValorEstimadoDoTanque1Anterior() {
        return valorEstimadoDoTanque1Anterior;
    }

    public void setValorEstimadoDoTanque1Anterior(double valorEstimadoDoTanque1Anterior) {
        this.valorEstimadoDoTanque1Anterior = valorEstimadoDoTanque1Anterior;
    }

    public double getValorEstimadoDoTanque1Atual() {
        return valorEstimadoDoTanque1Atual;
    }

    public void setValorEstimadoDoTanque1Atual(double valorEstimadoDoTanque1Atual) {
        this.valorEstimadoDoTanque1Atual = valorEstimadoDoTanque1Atual;
    }

    public double getValorEstimadoDoTanque2Anterior() {
        return valorEstimadoDoTanque2Anterior;
    }

    public void setValorEstimadoDoTanque2Anterior(double valorEstimadoDoTanque2Anterior) {
        this.valorEstimadoDoTanque2Anterior = valorEstimadoDoTanque2Anterior;
    }

    public double getValorEstimadoDoTanque2Atual() {
        return valorEstimadoDoTanque2Atual;
    }

    public void setValorEstimadoDoTanque2Atual(double valorEstimadoDoTanque2Atual) {
        this.valorEstimadoDoTanque2Atual = valorEstimadoDoTanque2Atual;
    }
}
