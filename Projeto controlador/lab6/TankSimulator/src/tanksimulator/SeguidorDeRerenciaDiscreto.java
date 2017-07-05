/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author deangel
 */
public class SeguidorDeRerenciaDiscreto {

    double nivelDoTanque1Atual;
    double nivelDoTanque1Antigo;
    double nivelDoTanque2Atual;
    double nivelDoTanque2Antigo;
    double valorDeTensaoAtual;
    double valorDeTensaoAntigo;
    Complex p1, p2, p3;
    
    double k1,k21,k22;
    double uk; //tensão da bomba
    double erroAcumulado;
    
    
    public SeguidorDeRerenciaDiscreto(double nivelDoTanque1Atual, double nivelDoTanque1Antigo, double nivelDoTanque2Atual, double nivelDoTanque2Antigo, double valorDeTensaoAtual, double valorDeTensaoAntigo, Complex p1, Complex p2, Complex p3) {
        this.nivelDoTanque1Atual = nivelDoTanque1Atual;
        this.nivelDoTanque1Antigo = nivelDoTanque1Antigo;
        this.nivelDoTanque2Atual = nivelDoTanque2Atual;
        this.nivelDoTanque2Antigo = nivelDoTanque2Antigo;
        this.valorDeTensaoAtual = valorDeTensaoAtual;
        this.valorDeTensaoAntigo = valorDeTensaoAntigo;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public SeguidorDeRerenciaDiscreto() {
        this.nivelDoTanque1Atual = 0;
        this.nivelDoTanque1Antigo = 0;
        this.nivelDoTanque2Atual = 0;
        this.nivelDoTanque2Antigo = 0;
        this.valorDeTensaoAtual = 0;
        this.valorDeTensaoAntigo = 0;
        this.p1 = new Complex(0, 0);
        this.p2 = new Complex(0, 0);
        this.p3 = new Complex(0, 0);
        
        this.k1 = 0;
        this.k21 = 0;
        this.k22 = 0;
        
        uk = 0;
    }
    
    public void calcularOProximoValorDeTensao(){
        Complex argumentoX2, argumentoX1, argumentoU;
        argumentoX1 = new Complex(0,0);
        argumentoX2 = new Complex(0,0);
        argumentoU = new Complex(0,0);
        
        
        argumentoX2 = argumentoX2.add(p1.multiply(5122.660073735987975));
        argumentoX2 = argumentoX2.add(p2.multiply(5122.660073735987975));
        argumentoX2 = argumentoX2.add(p3.multiply(5122.660073735987975));
        argumentoX2 = argumentoX2.add(p1.multiply(p2.multiply(- 5156.398296106652)));
        argumentoX2 = argumentoX2.add(p1.multiply(p3.multiply(- 5156.398296106652)));
        argumentoX2 = argumentoX2.add(p2.multiply(p3.multiply(- 5156.398296106652)));
        argumentoX2 = argumentoX2.add(p1.multiply(p2.multiply(p3.multiply(5190.3587209331399))));
        argumentoX2 = argumentoX2.add(- 5089.1425999544290814116014327822);
        
        argumentoX2.multiply(nivelDoTanque2Antigo);
        
        argumentoX1 = argumentoX1.add(p1.multiply(-50.4599300864818));
        argumentoX1 = argumentoX1.add(p2.multiply( -50.4599300864818));
        argumentoX1 = argumentoX1.add(p3.multiply( -50.4599300864818));
        argumentoX1 = argumentoX1.add(p1.multiply(p2.multiply(16.9431824702138249)));
        argumentoX1 = argumentoX1.add(p1.multiply(p3.multiply(16.9431824702138249)));
        argumentoX1 = argumentoX1.add(p2.multiply(p3.multiply(16.9431824702138249)));
        argumentoX1 = argumentoX1.add(p1.multiply(p2.multiply(p3.multiply(17.0172415282675696))));
        argumentoX1 = argumentoX1.add(83.5373533872147);
        
        argumentoX1 = argumentoX1.multiply(-nivelDoTanque1Antigo); //ja coloquei o sinal de menos
        
        argumentoU = argumentoU.add(p1.multiply(0.99999999999));
        argumentoU = argumentoU.add(p2.multiply( 0.99999999999));
        argumentoU = argumentoU.add(p3.multiply( 0.99999999999));
        argumentoU = argumentoU.add(p1.multiply(p2.multiply(0.00000000000000460273)));
        argumentoU = argumentoU.add(p1.multiply(p3.multiply(0.00000000000000460273)));
        argumentoU = argumentoU.add(p2.multiply(p3.multiply(0.00000000000000460273)));
        argumentoU = argumentoU.add(p1.multiply(p2.multiply(p3.multiply( 0.000000000000004677192))));
        argumentoU = argumentoU.add(-1.9869140355599765387);

        argumentoU = argumentoU.multiply(valorDeTensaoAntigo);
        
        valorDeTensaoAtual = (argumentoU.add(argumentoX1.add(argumentoX2))).abs();
        valorDeTensaoAntigo = valorDeTensaoAtual;
        
        nivelDoTanque1Antigo = nivelDoTanque1Atual;
        nivelDoTanque2Antigo = nivelDoTanque2Atual;
    }
    
    public void calculaTensaoNaBomba(){
        this.uk = k1*(this.erroAcumulado) - (k21*nivelDoTanque1Atual + k22*nivelDoTanque2Atual);
    }
    
    public void calcularKzinhos(){
        
        Complex aux1 = new Complex(0, 0);
        Complex aux21 = new Complex(0, 0);
        Complex aux22 = new Complex(0, 0);
        
        aux1 = aux1.add(p1.multiply(-5173.34147898996));
        aux1 = aux1.add(p2.multiply(-5173.34147898996));
        aux1 = aux1.add(p3.multiply(-5173.34147898996));
        
        aux1 = aux1.add(p1.multiply(p2.multiply(5173.34147898996)));
        aux1 = aux1.add(p1.multiply(p3.multiply(5173.34147898996)));
        aux1 = aux1.add(p2.multiply(p3.multiply(5173.34147898996)));
        
        aux1 = aux1.add(p1.multiply(p2.multiply(p3.multiply(-5173.34147898996))));
        
        aux1 = aux1.add(5173.34147898996);
        
        k1 = aux1.abs();
        
        aux21 = aux21.add(p1.multiply( - 8.45311684659));
        aux21 = aux21.add(p2.multiply( - 8.45311684659));
        aux21 = aux21.add(p3.multiply( - 8.45311684659));
        
        aux21 = aux21.add(p1.multiply(p2.multiply( - 8.4900656236222)));
        aux21 = aux21.add(p1.multiply(p3.multiply( - 8.4900656236222)));
        aux21 = aux21.add(p2.multiply(p3.multiply( - 8.4900656236222)));
        
        aux21 = aux21.add(p1.multiply(p2.multiply(p3.multiply(- 8.52717590464563))));
        
        aux21 = aux21.add(58.91304693307279);

        k21 = aux21.abs();
        
        aux22 = aux22.add(p1.multiply(- 2572.5767506301));
        aux22 = aux22.add(p2.multiply( - 2572.5767506301));
        aux22 = aux22.add(p3.multiply( - 2572.5767506301));
        
        aux22 = aux22.add(p1.multiply(p2.multiply( - 2583.82154547)));
        aux22 = aux22.add(p1.multiply(p3.multiply( - 2583.82154547)));
        aux22 = aux22.add(p2.multiply(p3.multiply( - 2583.82154547)));
        
        aux22 = aux22.add(p1.multiply(p2.multiply(p3.multiply(7774.18026640966))));
        
        aux22 = aux22.add( 7695.2368243661);
        
        k22 = aux22.abs();
    }
    
    
    
    public void updateNiveis(double nivelDoTanque1, double nivelDoTanque2){
        this.nivelDoTanque1Atual = nivelDoTanque1;
        this.nivelDoTanque2Atual = nivelDoTanque2;
    }
        
    public double getNivelDoTanque1Antigo() {
        return nivelDoTanque1Antigo;
    }

    public void setNivelDoTanque1Antigo(double nivelDoTanque1Antigo) {
        this.nivelDoTanque1Antigo = nivelDoTanque1Antigo;
    }

    public double getNivelDoTanque1Atual() {
        return nivelDoTanque1Atual;
    }

    public void setNivelDoTanque1Atual(double nivelDoTanque1Atual) {
        this.nivelDoTanque1Atual = nivelDoTanque1Atual;
    }

    public double getNivelDoTanque2Antigo() {
        return nivelDoTanque2Antigo;
    }

    public void setNivelDoTanque2Antigo(double nivelDoTanque2Antigo) {
        this.nivelDoTanque2Antigo = nivelDoTanque2Antigo;
    }

    public double getNivelDoTanque2Atual() {
        return nivelDoTanque2Atual;
    }

    public void setNivelDoTanque2Atual(double nivelDoTanque2Atual) {
        this.nivelDoTanque2Atual = nivelDoTanque2Atual;
    }

    public Complex getP1() {
        return p1;
    }

    public void setP1(Complex p1) {
        this.p1 = p1;
    }

    public Complex getP2() {
        return p2;
    }

    public void setP2(Complex p2) {
        this.p2 = p2;
    }

    public Complex getP3() {
        return p3;
    }

    public void setP3(Complex p3) {
        this.p3 = p3;
    }

    public double getValorDeTensaoAntigo() {
        return valorDeTensaoAntigo;
    }

    public void setValorDeTensaoAntigo(double valorDeTensaoAntigo) {
        this.valorDeTensaoAntigo = valorDeTensaoAntigo;
    }

    public double getValorDeTensaoAtual() {
        return valorDeTensaoAtual;
    }

    public void setValorDeTensaoAtual(double valorDeTensaoAtual) {
        this.valorDeTensaoAtual = valorDeTensaoAtual;
    }

    public double getErroAcumulado() {
        return erroAcumulado;
    }

    public void setErroAcumulado(double erroAcumulado) {
        this.erroAcumulado = erroAcumulado;
    }

    public double getK1() {
        return k1;
    }

    public void setK1(double k1) {
        this.k1 = k1;
    }

    public double getK21() {
        return k21;
    }

    public void setK21(double k21) {
        this.k21 = k21;
    }

    public double getK22() {
        return k22;
    }

    public void setK22(double k22) {
        this.k22 = k22;
    }

    public double getUk() {
        return uk;
    }

    public void setUk(double uk) {
        this.uk = uk;
    }
    
}
