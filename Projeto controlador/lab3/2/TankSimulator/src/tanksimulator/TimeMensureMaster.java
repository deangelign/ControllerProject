/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

/**
 *
 * @author deangel
 */
public class TimeMensureMaster {

    private double tempoAnterior;
    private double TempoDeSubida0100;
    private double TempoDeSubida1090;
    private double TempoDeSubida2080;
    private double tempoDePico;
    private double valorDePico;
    private boolean jaFoiDeterminadoOTempoDeSubida0100pt1;
    private boolean jaFoiDeterminadoOTempoDeSubida0100pt2;
    private boolean jaFoiDeterminadoOTempoDeSubida1090pt1;
    private boolean jaFoiDeterminadoOTempoDeSubida1090pt2;
    private boolean jaFoiDeterminadoOTempoDeSubida2080pt1;
    private boolean jaFoiDeterminadoOTempoDeSubida2080pt2;
    private double picoParaCima;
    private double picoParaBaixo;
    private double variacaoDoErro;
    private double tempoAcomodacao10;
    private double tempoAcomodacao7;
    private double tempoAcomodacao5;
    private double tempoAcomodacao2;
    private boolean jaFoiDeterminadoTempoAcomodacao5;
    private boolean jaFoiDeterminadoTempoAcomodacao2;
    private boolean jaFoiDeterminadoTempoAcomodacao10;
    private boolean jaFoiDeterminadoTempoAcomodacao7;
    private double nivelAnterior;
    private boolean taSubindo;
    private boolean taDescendo;
    private double setpointAntigo;
    private double valorEfetivo;
    private double overshootper;
    private double undershootper;
    private double valorUnderShoot;
    //
    private int iter10;
    private int iter5;
    private int iter2;

    public TimeMensureMaster() {

        this.jaFoiDeterminadoOTempoDeSubida0100pt1 = false;
        this.jaFoiDeterminadoOTempoDeSubida0100pt2 = false;
        this.jaFoiDeterminadoOTempoDeSubida1090pt1 = false;
        this.jaFoiDeterminadoOTempoDeSubida1090pt2 = false;
        this.jaFoiDeterminadoOTempoDeSubida2080pt1 = false;
        this.jaFoiDeterminadoOTempoDeSubida2080pt2 = false;
        this.jaFoiDeterminadoTempoAcomodacao5 = false;
        this.jaFoiDeterminadoTempoAcomodacao2 = false;
        this.jaFoiDeterminadoTempoAcomodacao10 = false;
        this.jaFoiDeterminadoTempoAcomodacao7 = false;
        this.tempoAcomodacao10 = 0;
        this.tempoAnterior = 0;
        this.tempoDePico = 0;
        this.TempoDeSubida0100 = 0;
        this.variacaoDoErro = 0;
        this.tempoAcomodacao5 = 0;
        this.tempoAcomodacao2 = 0;
        this.valorDePico = 0;
        this.picoParaCima = 999999;
        this.picoParaBaixo = -999999;
        this.nivelAnterior = 0;
        this.taDescendo = false;
        this.taSubindo = true;
        this.setpointAntigo = 0;
        this.valorEfetivo = 0;
        this.overshootper = 0;

    }

    //tempo de subida de 0-100
    //tempo de subida de 10-90
    //tempo de subida de 20-80
    //tempo de acomodação de 2%
    //tempo de acomodação de 5%
    //tempo de acomodação de 7%
    //tempo de acomodação de 10%
    public void analisarSaida(double tempo, double tempoAnterior, double setpoint, double nivel, double erroAtual, double erroAnterior) {

        this.variacaoDoErro = erroAtual - erroAnterior;
        this.valorEfetivo = Math.abs(setpoint - this.setpointAntigo);

        //tempo de subida de 0-100
        if (true) {
            if (!this.jaFoiDeterminadoOTempoDeSubida0100pt1) {
                if (setpoint > setpointAntigo) {
                    if (nivel >= setpointAntigo + valorEfetivo * 0) {
                        TempoDeSubida0100 = tempo;
                        
                        this.jaFoiDeterminadoOTempoDeSubida0100pt1 = true;
                    }
                } else {
                    if (nivel <= setpointAntigo - valorEfetivo * 0) {
                        TempoDeSubida0100 = tempo;
                        this.jaFoiDeterminadoOTempoDeSubida0100pt1 = true;
                    }
                }
            }

            if (this.jaFoiDeterminadoOTempoDeSubida0100pt1 && (!this.jaFoiDeterminadoOTempoDeSubida0100pt2)) {
                if (setpoint > setpointAntigo) {
                    if (nivel >= setpointAntigo + valorEfetivo * 1) {
                        TempoDeSubida0100 = tempo - TempoDeSubida0100;
                        this.jaFoiDeterminadoOTempoDeSubida0100pt2 = true;
                    }
                } else {
                    if (nivel <= setpointAntigo - valorEfetivo * 1) {
                        TempoDeSubida0100 = tempo - TempoDeSubida0100;
                        this.jaFoiDeterminadoOTempoDeSubida0100pt2 = true;
                    }
                }
            }


        }


        //tempo de subida de 10-90
        if (true) {
            if (!this.jaFoiDeterminadoOTempoDeSubida1090pt1) {
                if (setpoint > setpointAntigo) {
                    if (nivel >= setpointAntigo + valorEfetivo * 0.1) {
                        TempoDeSubida1090 = tempo;
                        this.jaFoiDeterminadoOTempoDeSubida1090pt1 = true;
                    }
                } else {
                    if (nivel <= setpointAntigo - valorEfetivo * 0.1) {
                        TempoDeSubida1090 = tempo;
                        this.jaFoiDeterminadoOTempoDeSubida1090pt1 = true;
                    }
                }
            }


            if (this.jaFoiDeterminadoOTempoDeSubida1090pt1 && (!this.jaFoiDeterminadoOTempoDeSubida1090pt2)) {
                if (setpoint > setpointAntigo) {
                    if (nivel >= setpointAntigo + valorEfetivo * 0.9) {
                        TempoDeSubida1090 = tempo - TempoDeSubida1090;
                        this.jaFoiDeterminadoOTempoDeSubida1090pt2 = true;
                    }
                } else {
                    if (nivel <= setpointAntigo - valorEfetivo * 0.9) {
                        TempoDeSubida1090 = tempo - TempoDeSubida1090;
                        this.jaFoiDeterminadoOTempoDeSubida1090pt2 = true;
                    }
                }
            }

        }


        //tempo de subida de 20-80
        if (true) {
            if (!this.jaFoiDeterminadoOTempoDeSubida2080pt1) {
                if (setpoint > setpointAntigo) {
                    if (nivel >= setpointAntigo + valorEfetivo * 0.2) {
                        TempoDeSubida2080 = tempo;
                        this.jaFoiDeterminadoOTempoDeSubida2080pt1 = true;
                    }
                } else {
                    if (nivel <= setpointAntigo - valorEfetivo * 0.2) {
                        TempoDeSubida2080 = tempo;
                        this.jaFoiDeterminadoOTempoDeSubida2080pt1 = true;
                    }
                }
            }


            if (this.jaFoiDeterminadoOTempoDeSubida2080pt1 && (!this.jaFoiDeterminadoOTempoDeSubida2080pt2)) {
                if (setpoint > setpointAntigo) {
                    if (nivel >= setpointAntigo + valorEfetivo * 0.8) {
                        TempoDeSubida2080 = tempo - TempoDeSubida2080;
                        this.jaFoiDeterminadoOTempoDeSubida2080pt2 = true;
                    }
                } else {
                    if (nivel <= setpointAntigo - valorEfetivo * 0.8) {
                        TempoDeSubida2080 = tempo - TempoDeSubida2080;
                        this.jaFoiDeterminadoOTempoDeSubida2080pt2 = true;
                    }
                }
            }


        }


        //valor de pico e undershoot
        if (this.jaFoiDeterminadoOTempoDeSubida0100pt2) {
            if (setpoint > setpointAntigo) {

                if (this.valorDePico < nivel && nivel > setpoint) {
                    this.valorDePico = nivel;
                    this.tempoDePico = tempo;
                    this.overshootper = Math.abs(this.valorDePico - setpoint) / (valorEfetivo) * 100;
                }
                if (nivel > nivelAnterior && nivel < setpoint) {

                    if ((Math.abs(nivelAnterior - setpoint) / (valorEfetivo) * 100) > undershootper) {

                        this.undershootper = Math.abs(((nivel + nivelAnterior) / 2) - setpoint) / (valorEfetivo) * 100;
                    }
                }

            }
        }

        //valor de pico e undershoot
        if (jaFoiDeterminadoOTempoDeSubida0100pt2) {
            if (setpoint < setpointAntigo) {
                if (this.valorDePico > nivel && nivel < setpoint) {
                    this.valorDePico = nivel;
                    this.tempoDePico = tempo;
                    this.overshootper = Math.abs(this.valorDePico - setpoint) / (valorEfetivo) * 100;
                }

                if (nivel < nivelAnterior && nivel > setpoint) {
                    if ((Math.abs(nivel - setpoint) / (valorEfetivo) * 100) > this.undershootper) {
                        this.undershootper = Math.abs(((nivel + nivelAnterior) / 2) - setpoint) / (valorEfetivo) * 100;
                    }
                }

            }
        }


        //tempo de acomodação de 10%
        if (this.jaFoiDeterminadoOTempoDeSubida0100pt2) {
            if (nivel < ((valorEfetivo * 0.1) + setpoint) && nivel > setpoint - (valorEfetivo * 0.1)) {
                //if (this.iter10 == 0) {
                if (!this.jaFoiDeterminadoTempoAcomodacao10) {
                    this.tempoAcomodacao10 = tempo;
                    this.jaFoiDeterminadoTempoAcomodacao10 = true;
                }
                //}
                //this.iter10++;

                //if (iter10 > 20) {
                //    this.jaFoiDeterminadoTempoAcomodacao10 = true;
                //}
            } else {
                //this.iter10 = 0;
                this.tempoAcomodacao10 = 0;
                this.jaFoiDeterminadoTempoAcomodacao10 = false;
            }
        }


        //tempo de acomodação de 7%
        if (this.jaFoiDeterminadoOTempoDeSubida0100pt2) {
            if (nivel < ((valorEfetivo * 0.07) + setpoint) && nivel > setpoint - (valorEfetivo * 0.07)) {
                //if (this.iter10 == 0) {
                if (!this.jaFoiDeterminadoTempoAcomodacao7) {
                    this.tempoAcomodacao7 = tempo;
                    this.jaFoiDeterminadoTempoAcomodacao7 = true;
                }
                //}
                //this.iter10++;

                //if (iter10 > 20) {
                //    this.jaFoiDeterminadoTempoAcomodacao10 = true;
                //}
            } else {
                //this.iter10 = 0;
                this.tempoAcomodacao7 = 0;
                this.jaFoiDeterminadoTempoAcomodacao7 = false;
            }
        }



        //tempo de acomodação de 5%
        if (this.jaFoiDeterminadoOTempoDeSubida0100pt2) {
            if (nivel < (valorEfetivo * 0.05) + setpoint && nivel > setpoint - (valorEfetivo * 0.05)) {
                //if (this.iter5 == 0) {
                if (!this.jaFoiDeterminadoTempoAcomodacao5) {
                    this.tempoAcomodacao5 = tempo;
                    this.jaFoiDeterminadoTempoAcomodacao5 = true;
                }

                //}
                //this.iter5++;

                //if (iter5 > 20) {
                //this.jaFoiDeterminadoTempoAcomodacao5 = true;
                //}
            } else {
                //this.iter5 = 0;
                this.tempoAcomodacao5 = 0;
                this.jaFoiDeterminadoTempoAcomodacao5 = false;
            }
        }


        //tempo de acomodação 2
        if (this.jaFoiDeterminadoOTempoDeSubida0100pt2) {
            if (nivel < (valorEfetivo * 0.02) + setpoint && nivel > setpoint - (valorEfetivo * 0.02)) {
                //if (this.iter2 == 0) {
                if (!this.jaFoiDeterminadoTempoAcomodacao2) {
                    this.tempoAcomodacao2 = tempo;
                    this.jaFoiDeterminadoTempoAcomodacao2 = true;
                }
                //}
                //this.iter2++;

                //if (iter2 > 20) {
                //    this.jaFoiDeterminadoTempoAcomodacao2 = true;
                //}
            } else {
                //this.iter2 = 0;
                this.tempoAcomodacao2 = 0;
                this.jaFoiDeterminadoTempoAcomodacao2 = false;

            }
        }









        this.tempoAnterior = tempo;
        this.nivelAnterior = nivel;

    }

    public void reset() {
        this.jaFoiDeterminadoOTempoDeSubida0100pt1 = false;
        this.jaFoiDeterminadoOTempoDeSubida0100pt2 = false;
        this.jaFoiDeterminadoOTempoDeSubida1090pt1 = false;
        this.jaFoiDeterminadoOTempoDeSubida1090pt2 = false;
        this.jaFoiDeterminadoOTempoDeSubida2080pt1 = false;
        this.jaFoiDeterminadoOTempoDeSubida2080pt2 = false;
        this.jaFoiDeterminadoTempoAcomodacao5 = false;
        this.jaFoiDeterminadoTempoAcomodacao2 = false;
        this.jaFoiDeterminadoTempoAcomodacao10 = false;
        //this.valorDePico = 0;
        this.tempoAnterior = 0;
        this.tempoDePico = 0;
        this.TempoDeSubida0100 = 0;
        this.variacaoDoErro = 0;
        this.tempoAcomodacao5 = 0;
        this.tempoAcomodacao2 = 0;
        //this.valorDePico = 0;
        this.iter10 = 0;
        this.iter5 = 0;
        this.iter2 = 0;
        this.setpointAntigo = 0;
        this.overshootper = 0;
        this.TempoDeSubida1090 = 0;
        this.TempoDeSubida2080 = 0;

    }

    public boolean isJaFoiDeterminadoOTempoDeSubida0100pt1() {
        return jaFoiDeterminadoOTempoDeSubida0100pt1;
    }

    public void setJaFoiDeterminadoOTempoDeSubida0100pt1(boolean jaFoiDeterminadoOTempoDeSubida0100pt1) {
        this.jaFoiDeterminadoOTempoDeSubida0100pt1 = jaFoiDeterminadoOTempoDeSubida0100pt1;
    }

    public boolean isJaFoiDeterminadoOTempoDeSubida0100pt2() {
        return jaFoiDeterminadoOTempoDeSubida0100pt2;
    }

    public void setJaFoiDeterminadoOTempoDeSubida0100pt2(boolean jaFoiDeterminadoOTempoDeSubida0100pt2) {
        this.jaFoiDeterminadoOTempoDeSubida0100pt2 = jaFoiDeterminadoOTempoDeSubida0100pt2;
    }

    public double getUndershootper() {
        return undershootper;
    }

    public void setUndershootper(double undershootper) {
        this.undershootper = undershootper;
    }

    public boolean isJaFoiDeterminadoTempoAcomodacao2() {
        return jaFoiDeterminadoTempoAcomodacao2;
    }

    public void setJaFoiDeterminadoTempoAcomodacao2(boolean jaFoiDeterminadoTempoAcomodacao2) {
        this.jaFoiDeterminadoTempoAcomodacao2 = jaFoiDeterminadoTempoAcomodacao2;
    }

    public boolean isJaFoiDeterminadoTempoAcomodacao5() {
        return jaFoiDeterminadoTempoAcomodacao5;
    }

    public void setJaFoiDeterminadoTempoAcomodacao5(boolean jaFoiDeterminadoTempoAcomodacao5) {
        this.jaFoiDeterminadoTempoAcomodacao5 = jaFoiDeterminadoTempoAcomodacao5;
    }

    public double getPicoParaBaixo() {
        return picoParaBaixo;
    }

    public void setPicoParaBaixo(double picoParaBaixo) {
        this.picoParaBaixo = picoParaBaixo;
    }

    public double getPicoParaCima() {
        return picoParaCima;
    }

    public void setPicoParaCima(double picoParaCima) {
        this.picoParaCima = picoParaCima;
    }

    public double getTempoAcomodacao2() {
        return tempoAcomodacao2;
    }

    public void setTempoAcomodacao2(double tempoAcomodacao2) {
        this.tempoAcomodacao2 = tempoAcomodacao2;
    }

    public double getTempoAcomodacao5() {
        return tempoAcomodacao5;
    }

    public void setTempoAcomodacao5(double tempoAcomodacao5) {
        this.tempoAcomodacao5 = tempoAcomodacao5;
    }

    public double getTempoAnterior() {
        return tempoAnterior;
    }

    public void setTempoAnterior(double tempoAnterior) {
        this.tempoAnterior = tempoAnterior;
    }

    public double getTempoDePico() {
        return tempoDePico;
    }

    public void setTempoDePico(double tempoDePico) {
        this.tempoDePico = tempoDePico;
    }

    public double getTempoDeSubida0100() {
        return TempoDeSubida0100;
    }

    public void setTempoDeSubida0100(double TempoDeSubida0100) {
        this.TempoDeSubida0100 = TempoDeSubida0100;
    }

    public double getValorDePico() {
        return valorDePico;
    }

    public void setValorDePico(double valorDePico) {
        this.valorDePico = valorDePico;
    }

    public double getVariacaoDoErro() {
        return variacaoDoErro;
    }

    public void setVariacaoDoErro(double variacaoDoErro) {
        this.variacaoDoErro = variacaoDoErro;
    }

    public boolean isJaFoiDeterminadoTempoAcomodacao10() {
        return jaFoiDeterminadoTempoAcomodacao10;
    }

    public void setJaFoiDeterminadoTempoAcomodacao10(boolean jaFoiDeterminadoTempoAcomodacao10) {
        this.jaFoiDeterminadoTempoAcomodacao10 = jaFoiDeterminadoTempoAcomodacao10;
    }

    public double getNivelAnterior() {
        return nivelAnterior;
    }

    public void setNivelAnterior(double nivelAnterior) {
        this.nivelAnterior = nivelAnterior;
    }

    public boolean isTaDescendo() {
        return taDescendo;
    }

    public void setTaDescendo(boolean taDescendo) {
        this.taDescendo = taDescendo;
    }

    public boolean isTaSubindo() {
        return taSubindo;
    }

    public void setTaSubindo(boolean taSubindo) {
        this.taSubindo = taSubindo;
    }

    public double getTempoAcomodacao10() {
        return tempoAcomodacao10;
    }

    public void setTempoAcomodacao10(double tempoAcomodacao10) {
        this.tempoAcomodacao10 = tempoAcomodacao10;
    }

    public double getSetpointAntigo() {
        return setpointAntigo;
    }

    public void setSetpointAntigo(double setpointAntigo) {
        this.setpointAntigo = setpointAntigo;
    }

    public double getValorEfetivo() {
        return valorEfetivo;
    }

    public void setValorEfetivo(double valorEfetivo) {
        this.valorEfetivo = valorEfetivo;
    }

    public int getIter10() {
        return iter10;
    }

    public void setIter10(int iter10) {
        this.iter10 = iter10;
    }

    public int getIter2() {
        return iter2;
    }

    public void setIter2(int iter2) {
        this.iter2 = iter2;
    }

    public int getIter5() {
        return iter5;
    }

    public void setIter5(int iter5) {
        this.iter5 = iter5;
    }

    public double getOvershootper() {
        return overshootper;
    }

    public void setOvershootper(double overshootper) {
        this.overshootper = overshootper;
    }

    public double getTempoDeSubida1090() {
        return TempoDeSubida1090;
    }

    public void setTempoDeSubida1090(double TempoDeSubida1090) {
        this.TempoDeSubida1090 = TempoDeSubida1090;
    }

    public double getTempoDeSubida2080() {
        return TempoDeSubida2080;
    }

    public void setTempoDeSubida2080(double TempoDeSubida2080) {
        this.TempoDeSubida2080 = TempoDeSubida2080;
    }

    public boolean isJaFoiDeterminadoOTempoDeSubida1090pt1() {
        return jaFoiDeterminadoOTempoDeSubida1090pt1;
    }

    public void setJaFoiDeterminadoOTempoDeSubida1090pt1(boolean jaFoiDeterminadoOTempoDeSubida1090pt1) {
        this.jaFoiDeterminadoOTempoDeSubida1090pt1 = jaFoiDeterminadoOTempoDeSubida1090pt1;
    }

    public boolean isJaFoiDeterminadoOTempoDeSubida1090pt2() {
        return jaFoiDeterminadoOTempoDeSubida1090pt2;
    }

    public void setJaFoiDeterminadoOTempoDeSubida1090pt2(boolean jaFoiDeterminadoOTempoDeSubida1090pt2) {
        this.jaFoiDeterminadoOTempoDeSubida1090pt2 = jaFoiDeterminadoOTempoDeSubida1090pt2;
    }

    public boolean isJaFoiDeterminadoOTempoDeSubida2080pt1() {
        return jaFoiDeterminadoOTempoDeSubida2080pt1;
    }

    public void setJaFoiDeterminadoOTempoDeSubida2080pt1(boolean jaFoiDeterminadoOTempoDeSubida2080pt1) {
        this.jaFoiDeterminadoOTempoDeSubida2080pt1 = jaFoiDeterminadoOTempoDeSubida2080pt1;
    }

    public boolean isJaFoiDeterminadoOTempoDeSubida2080pt2() {
        return jaFoiDeterminadoOTempoDeSubida2080pt2;
    }

    public void setJaFoiDeterminadoOTempoDeSubida2080pt2(boolean jaFoiDeterminadoOTempoDeSubida2080pt2) {
        this.jaFoiDeterminadoOTempoDeSubida2080pt2 = jaFoiDeterminadoOTempoDeSubida2080pt2;
    }

    public boolean isJaFoiDeterminadoTempoAcomodacao7() {
        return jaFoiDeterminadoTempoAcomodacao7;
    }

    public void setJaFoiDeterminadoTempoAcomodacao7(boolean jaFoiDeterminadoTempoAcomodacao7) {
        this.jaFoiDeterminadoTempoAcomodacao7 = jaFoiDeterminadoTempoAcomodacao7;
    }

    public double getTempoAcomodacao7() {
        return tempoAcomodacao7;
    }

    public void setTempoAcomodacao7(double tempoAcomodacao7) {
        this.tempoAcomodacao7 = tempoAcomodacao7;
    }

    public double getValorUnderShoot() {
        return valorUnderShoot;
    }

    public void setValorUnderShoot(double valorUnderShoot) {
        this.valorUnderShoot = valorUnderShoot;
    }
    
    
    
}
