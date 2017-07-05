/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

/**
 *
 * @author Andouglasje
 */
public class ControleCascata {
    
    Controlador mestre, escravo;
    double saidaMestre=0, saidaEscravo=0;
    opcaoControlador ctrlMestre, ctrlEscravo;
    double erroAtualEscravo;
    
   
    public void ControleCascata(Controlador mestre, Controlador escravo, opcaoControlador controleMestre, opcaoControlador controleEscravo, double kpMestre, double kpEscravo, double kiMestre, double kiEscravo, double kdMestre, double kdEscravo, double erroAtualMestre, double erroAntigoMestre, double erroAntigoEscravo, double periodoMestre, double periodoEscravo, double metodoIntegralMestre, double metodoIntegralEscravo, double sensor ){
        
        switch (controleMestre){
            case ControlP:
                saidaMestre = this.mestre.controladorP(erroAtualMestre, kpMestre);
                break;
            case ControlPD:
                saidaMestre = this.mestre.ControladorPD(erroAtualMestre, erroAntigoMestre, kpMestre, kdMestre, periodoMestre);
                break;
            case ControlPI:
                saidaMestre = this.mestre.controladorPI(erroAtualMestre, erroAntigoMestre, kpMestre, kiMestre, periodoMestre, metodoIntegralMestre);
                break;
            case ControlPID:
                saidaMestre = this.mestre.controladorPID(erroAtualMestre, erroAntigoMestre, kpMestre, kiMestre, kdMestre, periodoMestre, metodoIntegralMestre);
                break;
           }
        erroAtualEscravo = saidaMestre - sensor;
        switch (controleEscravo){
            case ControlP:
                saidaEscravo = this.escravo.controladorP(erroAtualEscravo, kpEscravo);
                break;
            case ControlPD:
                saidaEscravo = this.escravo.ControladorPD(erroAtualEscravo, erroAntigoEscravo, kpEscravo, kdEscravo, periodoEscravo);
                break;
            case ControlPI:
                saidaEscravo = this.escravo.controladorPI(erroAtualEscravo, erroAntigoEscravo, kpEscravo, kiEscravo, periodoEscravo, metodoIntegralEscravo);
                break;
            case ControlPID:
                saidaEscravo = this.escravo.controladorPID(erroAtualEscravo, erroAntigoEscravo, kpEscravo, kiEscravo, kdEscravo, periodoEscravo, metodoIntegralEscravo);
                break;
           }
        
    }
    
    public double getSaidaEscravo(){
        return saidaEscravo;
    }
    
    enum opcaoControlador{
        ControlP,
        ControlPD,
        ControlPI,
        ControlPID
    }
}
