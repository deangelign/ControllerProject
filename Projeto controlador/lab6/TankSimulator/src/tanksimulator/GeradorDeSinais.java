/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

/**
 *
 * @author Deangelo
 */
public class GeradorDeSinais{
    
     private double amplitudeRand;
     private double periodoRand;
    
    public GeradorDeSinais() {    
    }
    
    //public double degrau(double t){
    //    return this.Amplitude;
    //}
    
    
    public double degrau(double t,double periodo_segundo, double amplitude, double offset){
        
        if(amplitude > 3){
           
            return 3;
        }
        if(amplitude < -3){
            
            return -3;
        }
        
        return amplitude;
    }
        
    public double senoidalFuction(double t,double periodo_segundo, double amplitude, double offset ){
        double aux = (((amplitude/2) * Math.sin(  (2*Math.PI/periodo_segundo) * t)  ) + offset);
        
        if(aux > 3){
            return 3;
        }
        if(aux < -3){
            return -3;
        }
        return aux;
    }
    
    public double squareWaveFunction(double t, double periodo_segundo, double amplitude, double offset){
        
        double tempo_ondaQuadrada = t % periodo_segundo;
        
        if(tempo_ondaQuadrada <= periodo_segundo/2){
            if( (amplitude/2) + offset > 3){
                return 3;
            }
            if( (amplitude/2) + offset < -3){
                return -3;
            }
            
            return ( (amplitude/2) + offset);
        }
        else{
            return ((-amplitude/2)+offset);
        }
        
    }
        
    public double funcaoDenteDeSerra(double t, double periodo_segundo, double amplitude, double offset){
        double aux;
        double tempo_denteDeSerra = t % periodo_segundo;
        double inclinacao = amplitude/periodo_segundo;
        aux = ((inclinacao*tempo_denteDeSerra) + offset - (amplitude/2));
        
        if(aux > 3){
            return 3;
        }
        if(aux < -3){
            return -3;
        }
        
        return aux;
        
    }
    
    public double FuncaoAleatoria(double t, double periodo_segundo, double amplitude, double offset){
        double aux = t % this.periodoRand;
        
        if(aux < 0.3){
            this.gerarFuncaoAleatoria(amplitude, periodo_segundo);
        }
        
        if(this.amplitudeRand > 3){
            return 3;
        }
        
        return (this.amplitudeRand + offset);
        
    }
    
    public void gerarFuncaoAleatoria(double amplitude, double periodo){
        
        double numero_randomico = Math.random() * 10;
        int sentido;
        
        if(numero_randomico < 5){
            sentido = -1;
        }
        else{
            sentido = 1;
        }
        
        this.amplitudeRand = Math.random() * (amplitude/2) * sentido;
        double aux = ( (Math.random()*8) + 2) ;
        this.periodoRand = (aux/10.0)*periodo;
        
        
//        System.out.println("amplitude: " + amplitude);
//        System.out.println("periodo: " + periodo);
//        System.out.println("amplitudeRand: "+ amplitudeRand);
//        System.out.println("periodoRand: " + periodoRand);
//        System.out.println("aux: " + aux);
    }

    public double getAmplitudeRand() {
        return amplitudeRand;
    }

    public void setAmplitudeRand(double amplitudeRand) {
        this.amplitudeRand = amplitudeRand;
    }

    public double getPeriodoRand() {
        return periodoRand;
    }

    public void setPeriodoRand(double periodoRand) {
        this.periodoRand = periodoRand;
    }
    
    
    
            

    
    
    
    
    
}
