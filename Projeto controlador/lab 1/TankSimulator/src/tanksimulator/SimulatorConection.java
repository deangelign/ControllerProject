/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

//import br.ufrn.dca.controle.QuanserClient;
//import br.ufrn.dca.controle.QuanserClientException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.OptionPaneUI;

/**
 *
 * @author Deangelo
 */
public class SimulatorConection extends Thread {

    private static double alpha = 6.25;
    private static double A1 = 15.5179;
    private static double a1 = 0.17813919765;
    private static double Km = 4.6;
    private QuanserClient quanserClient;
    private double[] readValue;
    private int writeChannel;
    private boolean[] readChannel;
    private double TensionValue;
    private double tensioOffset;
    private double tensionPeriodo;
    private boolean OnSystem;
    private JTextField txtF_amplitude;
    private JTextField txtF_offset;
    private JTextField txtF_periodo;
    private GeradorDeSinais geradorDeSinais;
    // 0 - degrau
    // 1 - senoidal
    // 2 - quadrada
    // 3 - serrote
    // 4 - aleatorio
    private int signalType;
    private long tempoInicial_nano;
    private long tempoAtual_nano;
    private boolean habilitaEscrita;
    private double[] PVetor;
    private boolean connectionStatus;
    private double[] altura;
    private double valor = 0;
    private boolean malhaAberta;
    private double setpointAltura;
    private double setpointTensao;
    private double erro;

    public SimulatorConection() {
        this.connectionStatus = false;
        this.OnSystem = false;
        this.habilitaEscrita = false;
        this.malhaAberta = true;
        this.signalType = 0;
        this.readValue = new double[8];
        this.altura = new double[8];
        this.readChannel = new boolean[8];
        this.setpointAltura = 0;

        this.readChannel[0] = false;
        this.readChannel[1] = false;
        this.readChannel[2] = false;
        this.readChannel[3] = false;
        this.readChannel[4] = false;
        this.readChannel[5] = false;
        this.readChannel[6] = false;
        this.readChannel[7] = false;
    }

    public void run() {
        this.conversionAmplitude();
        this.conversionOffset();
        this.conversionPeriodo();
        //this.generatePoints();

        double tempoS;
        //double valor = 0;
        this.tempoInicial_nano = System.nanoTime();
        this.OnSystem = false;

        //double erro = 1;
        //double altura = 0;

        while (true) {



            if (this.OnSystem) {

                //deltan = this.tempoAtual_nano - deltan;
                //deltas = deltan/Math.pow(10, 9);
                this.tempoAtual_nano = System.nanoTime();

                tempoS = (this.tempoAtual_nano - tempoInicial_nano) / (Math.pow(10, 9));

                try {
                    if (this.readChannel[0]) {
                        this.readValue[0] = this.quanserClient.read(0);
                        this.altura[0] = alpha * this.readValue[0];
                    }

                    if (this.readChannel[1]) {
                        this.readValue[1] = this.quanserClient.read(1);
                        this.altura[1] = alpha * this.readValue[1];
                    }

                    if (this.readChannel[2]) {
                        this.readValue[2] = this.quanserClient.read(2);
                        this.altura[2] = alpha * this.readValue[2];
                    }

                    if (this.readChannel[3]) {
                        this.readValue[3] = this.quanserClient.read(3);
                        this.altura[3] = alpha * this.readValue[3];
                    }

                    if (this.readChannel[4]) {
                        this.readValue[4] = this.quanserClient.read(4);
                        this.altura[4] = alpha * this.readValue[4];
                    }

                    if (this.readChannel[5]) {
                        this.readValue[5] = this.quanserClient.read(5);
                        this.altura[5] = alpha * this.readValue[5];
                    }

                    if (this.readChannel[6]) {
                        this.readValue[6] = this.quanserClient.read(6);
                        this.altura[6] = alpha * this.readValue[6];
                    }

                    if (this.readChannel[7]) {
                        this.readValue[7] = this.quanserClient.read(7);
                        this.altura[7] = alpha * this.readValue[7];
                    }

                } catch (QuanserClientException ex) {
                    //Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error ao ler informção da planta. O programa será sera desconectado e desligado", "Tank Simulator", JOptionPane.ERROR_MESSAGE);
                    this.connectionStatus = false;
                    this.quanserClient = null;
                    this.OnSystem = false;
                    this.habilitaEscrita = false;
                    Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                }

                this.PVetor[5] = this.altura[1];
                //System.out.println("altura2: " + this.altura[1]);

                if (this.isMalhaAberta()) {
                    if (this.signalType == 0) {
                        this.valor = this.geradorDeSinais.degrau(tempoS, this.tensionPeriodo, this.TensionValue, this.tensioOffset);
                    } else if (this.signalType == 1) {
                        this.valor = this.geradorDeSinais.senoidalFuction(tempoS, this.tensionPeriodo, this.TensionValue, this.tensioOffset);
                    } else if (this.signalType == 2) {
                        this.valor = this.geradorDeSinais.squareWaveFunction(tempoS, this.tensionPeriodo, this.TensionValue, this.tensioOffset);
                    } else if (this.signalType == 3) {
                        this.valor = this.geradorDeSinais.funcaoDenteDeSerra(tempoS, this.tensionPeriodo, this.TensionValue, this.tensioOffset);
                    } else if (this.signalType == 4) {
                        this.valor = this.geradorDeSinais.FuncaoAleatoria(tempoS, tensionPeriodo, this.TensionValue, this.tensioOffset);
                    }
                } else {
                    //if (erro > (10 ^ -3)) {
                    //this.erro = this.setpointTensao - this.readValue[0];
                    this.erro = (this.setpointAltura - (this.readValue[0] * alpha));
                    this.valor = this.erro;
                    this.PVetor[14] = this.setpointAltura;
                    this.PVetor[12] = this.erro / alpha;
                    this.PVetor[13] = this.erro;

                    if (this.valor > 3) {
                        this.valor = 3;
                    }
                    if (this.valor < -3) {
                        this.valor = -3;
                    }
                    //}
                }


                if (true) {
                    if ((this.altura[0] > 27 || this.altura[1] > 27) && this.valor > 1.75) {
                        try {
                            //System.out.println("1");
                            if (PVetor[4] < 1) {
                                this.quanserClient.write(this.writeChannel, 1.75);
                                //this.TensionValue = 1.75;
                                //this.OnSystem = false;

                                //this.txtF_amplitude.setText("1.75");
                                PVetor[4] = 2;
                            } else {
                                this.PVetor[0] = tempoS;
                                this.PVetor[1] = 1.75;
                                this.PVetor[2] = altura[0];
                                this.PVetor[3] = 0;

                            }
                        } catch (QuanserClientException ex) {
                            JOptionPane.showMessageDialog(null, "Error no envio de dados para planta. O programa será sera desconectado e desligado", "Tank Simulator", JOptionPane.ERROR_MESSAGE);
                            this.connectionStatus = false;
                            this.quanserClient = null;
                            this.OnSystem = false;
                            this.habilitaEscrita = false;
                            //Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                            Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                        }



                    } else if (this.altura[0] <= 3 && this.valor < 0) {
                        try {
                            //System.out.println("1");
                            if (PVetor[17] < 1) {
                                this.quanserClient.write(this.writeChannel, 0);
                                //this.TensionValue = 0;
                                //this.OnSystem = false;


                                //this.txtF_amplitude.setText();
                                PVetor[17] = 2;
                            } else {
                                this.PVetor[0] = tempoS;
                                this.PVetor[1] = 0;
                                this.PVetor[2] = altura[0];
                                this.PVetor[3] = 0;

                            }
                        } catch (QuanserClientException ex) {
                            JOptionPane.showMessageDialog(null, "Error no envio de dados para planta. O programa será sera desconectado e desligado", "Tank Simulator", JOptionPane.ERROR_MESSAGE);
                            this.connectionStatus = false;
                            this.quanserClient = null;
                            this.OnSystem = false;
                            this.habilitaEscrita = false;
                            //Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                            Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else if ((this.altura[0] > 29 || this.altura[1] > 29)) {
                        try {
                            if (this.PVetor[18] < 1) {
                                this.quanserClient.write(this.writeChannel, 0);
                                this.PVetor[18] = 2;
                                
                            } else {
                                this.PVetor[0] = tempoS;
                                this.PVetor[1] = 0;
                                this.PVetor[2] = altura[0];
                                this.PVetor[3] = 0;
                                
                            }
                        } catch (QuanserClientException ex) {

                            Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        if (this.PVetor[3] > 1) {
                            try {
                                this.quanserClient.write(this.writeChannel, valor);
                                this.PVetor[0] = tempoS;
                                this.PVetor[1] = valor;

                                if (this.readChannel[0]) {
                                    this.PVetor[2] = altura[0];
                                }

                                this.PVetor[3] = 0;
                                this.PVetor[4] = 0;
                                this.PVetor[17] = 0;
                                this.PVetor[18] = 0;

                            } catch (QuanserClientException ex) {
                                JOptionPane.showMessageDialog(null, "Error no envio de dados para planta. O programa será sera desconectado e desligado", "Tank Simulator", JOptionPane.ERROR_MESSAGE);
                                this.connectionStatus = false;
                                this.quanserClient = null;
                                this.OnSystem = false;
                                this.habilitaEscrita = false;
                                Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                }

            } else {
                if (habilitaEscrita) {
                    try {
                        this.quanserClient.write(writeChannel, 0);
                    } catch (QuanserClientException ex) {
                        //Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE,
                        //         null, ex);
                        JOptionPane.showMessageDialog(null, "Error no envio de dados para planta. O programa será sera desconectado e desligado", "Tank Simulator", JOptionPane.ERROR_MESSAGE);
                        this.connectionStatus = false;
                        this.quanserClient = null;
                        this.OnSystem = false;
                        this.habilitaEscrita = false;
                        Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.habilitaEscrita = false;
                }
            }


        }

    }

    //public void conversion() {
    //}
    public void conversionAmplitude() {

        try {
            this.TensionValue = Double.parseDouble(txtF_amplitude.getText());
            if (this.TensionValue > 3) {
                this.TensionValue = 3;
                //this.txtF_amplitude.setText("3.0");
            }
            if (this.TensionValue < 0) {
                this.TensionValue = 0;
                //this.txtF_amplitude.setText("0.0");
            }

            this.geradorDeSinais.setAmplitudeRand((this.TensionValue * Math.random()));

        } catch (Exception e) {
            //System.out.println("valor de tensão invalido");
        }


    }

    public void conversionSetpoint(JTextField txtF_setpoint) {
        try {
            this.setpointAltura = Double.parseDouble(txtF_setpoint.getText());
            this.setpointTensao = this.setpointAltura / alpha;
            this.PVetor[14] = this.setpointAltura;
        } catch (Exception e) {
        }


    }

    public double H_bomba(double tempo) {
        return (((Km / A1) * this.valor) - ((a1 / A1) * Math.sqrt(2 * 980 * this.altura[0]))) * tempo;
    }

    public void conversionPeriodo() {
        try {
            this.tensionPeriodo = Double.parseDouble(this.txtF_periodo.getText());
            this.geradorDeSinais.setPeriodoRand(this.tensionPeriodo * Math.random());

        } catch (Exception e) {
        }

    }

    public void conversionOffset() {

        try {
            this.tensioOffset = Double.parseDouble(this.txtF_offset.getText());
        } catch (Exception e) {
        }
    }

    /*
     * public void getValueInReadChannel() { try {
     * this.quanserClient.read(this.readChannel); } catch
     * (QuanserClientException ex) {
     * Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE,
     * null, ex); } }
     */
    public double getTensionValue() {
        return TensionValue;
    }

    public void setTensionValue(double TensionValue) {
        this.TensionValue = TensionValue;
    }

    public int getWriteChannel() {
        return writeChannel;
    }

    public void setWriteChannel(int writeChannel) {
        this.writeChannel = writeChannel;
    }

    public boolean isOnSystem() {
        return OnSystem;
    }

    public void setOnSystem(boolean OnSystem) {
        this.OnSystem = OnSystem;
    }

    public void turnOffSystem() {
        this.OnSystem = false;
        this.TensionValue = 0;
        this.tensioOffset = 0;
        this.tensionPeriodo = 0;
        this.habilitaEscrita = true;



    }

    public void turnOnSystem() {
        this.OnSystem = true;
        this.tempoInicial_nano = System.nanoTime();

    }

    public int getSignalType() {
        return signalType;
    }

    public void setSignalType(int signalType) {
        this.signalType = signalType;
    }

    public double getTensioOffset() {
        return tensioOffset;
    }

    public void setTensioOffset(double tensioOffset) {
        this.tensioOffset = tensioOffset;
    }

    public double getTensionPeriodo() {
        return tensionPeriodo;
    }

    public void setTensionPeriodo(double tensionPeriodo) {
        this.tensionPeriodo = tensionPeriodo;
    }

    public void refreshConnection(String ip, int porta, int writeChannel,
            int Channel, JTextField txtF_amplitude, JTextField txtF_offset,
            JTextField txtF_periodo, int signal, double[] PVector, boolean[] canaisLeitura) {
        try {
            this.quanserClient = new QuanserClient(ip, porta);
            this.writeChannel = writeChannel;
            this.readChannel[Channel] = true;
            this.TensionValue = 0;
            this.OnSystem = false;
            this.txtF_amplitude = txtF_amplitude;
            this.txtF_offset = txtF_offset;
            this.txtF_periodo = txtF_periodo;
            //this.signalType = signal;

            this.tempoInicial_nano = System.nanoTime();
            this.tempoAtual_nano = this.tempoInicial_nano;
            this.habilitaEscrita = true;

            this.geradorDeSinais = new GeradorDeSinais();
            this.PVetor = PVector;
            this.connectionStatus = true;

            this.readChannel = canaisLeitura;


            JOptionPane.showMessageDialog(null, "Conexão bem sucedida", "Tank Simulator", JOptionPane.INFORMATION_MESSAGE);


        } catch (QuanserClientException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar", "Tank Simulator", JOptionPane.ERROR_MESSAGE);
            this.quanserClient = null;
            //Logger.getLogger(SimulatorConection.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public boolean isConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isMalhaAberta() {
        return malhaAberta;
    }

    public void setMalhaAberta(boolean malhaAberta) {
        this.malhaAberta = malhaAberta;
    }
}
