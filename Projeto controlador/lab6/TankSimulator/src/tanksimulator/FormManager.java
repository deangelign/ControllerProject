/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalElement;
import java.text.DecimalFormat;
import java.util.Locale;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
//import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Deangelo
 */
public class FormManager {

    DecimalFormat decimalFormat;
    
    public FormManager() {
        Locale.setDefault(Locale.US);     
        decimalFormat = new DecimalFormat("0.00000");
            
    }

    public void validateWriteChannel(JRadioButton channel1_Write, JRadioButton channel2_Write,
            JRadioButton channel3_Write, JRadioButton channel4_Write, JRadioButton channel5_Write,
            JRadioButton channel6_Write, JRadioButton channel7_Write, JRadioButton channel8_Write,
            JRadioButton channelSelected) {


        if (channel1_Write != channelSelected) {
            channel1_Write.setSelected(false);
        }

        if (channel2_Write != channelSelected) {
            channel2_Write.setSelected(false);
        }

        if (channel3_Write != channelSelected) {
            channel3_Write.setSelected(false);
        }

        if (channel4_Write != channelSelected) {
            channel4_Write.setSelected(false);
        }

        if (channel5_Write != channelSelected) {
            channel5_Write.setSelected(false);
        }

        if (channel6_Write != channelSelected) {
            channel6_Write.setSelected(false);
        }

        if (channel7_Write != channelSelected) {
            channel7_Write.setSelected(false);
        }

        if (channel8_Write != channelSelected) {
            channel8_Write.setSelected(false);
        }

        if (true) {
            if ((channel1_Write == channelSelected)) {
                channel1_Write.setSelected(true);
            } else if ((channel2_Write == channelSelected)) {
                channel2_Write.setSelected(true);
            } else if ((channel3_Write == channelSelected)) {
                channel3_Write.setSelected(true);
            } else if ((channel4_Write == channelSelected)) {
                channel4_Write.setSelected(true);
            } else if ((channel5_Write == channelSelected)) {
                channel5_Write.setSelected(true);
            } else if ((channel6_Write == channelSelected)) {
                channel6_Write.setSelected(true);
            } else if ((channel7_Write == channelSelected)) {
                channel7_Write.setSelected(true);
            } else if ((channel8_Write == channelSelected)) {
                channel8_Write.setSelected(true);
            }
        }



    }

    public void validateReadChannel(JRadioButton channel1_Read, JRadioButton channel2_Read,
            JRadioButton channel3_Read, JRadioButton channel4_Read, JRadioButton channel5_Read,
            JRadioButton channel6_Read, JRadioButton channel7_Read, JRadioButton channel8_Read, JRadioButton channelSelected) {


        if ((channel1_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel1_Read.setSelected(true);
        } else if ((channel2_Read == channelSelected) && (!channel1_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel2_Read.setSelected(true);
        } else if ((channel3_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel1_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel3_Read.setSelected(true);
        } else if ((channel4_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel1_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel4_Read.setSelected(true);
        } else if ((channel5_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel1_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel5_Read.setSelected(true);
        } else if ((channel6_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel1_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel6_Read.setSelected(true);
        } else if ((channel7_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel1_Read.isSelected()) && (!channel8_Read.isSelected())) {
            channel7_Read.setSelected(true);
        } else if ((channel8_Read == channelSelected) && (!channel2_Read.isSelected()) && (!channel3_Read.isSelected())
                && (!channel4_Read.isSelected()) && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel1_Read.isSelected())) {
            channel8_Read.setSelected(true);
        }





    }

    public int whichChannelIsUsedtoWrite(JRadioButton channel1_Write, JRadioButton channel2_Write,
            JRadioButton channel3_Write, JRadioButton channel4_Write, JRadioButton channel5_Write,
            JRadioButton channel6_Write, JRadioButton channel7_Write, JRadioButton channel8_Write) {

        if (channel1_Write.isSelected()) {
            return 0;
        }

        if (channel2_Write.isSelected()) {
            return 1;
        }

        if (channel3_Write.isSelected()) {
            return 2;
        }

        if (channel4_Write.isSelected()) {
            return 3;
        }

        if (channel5_Write.isSelected()) {
            return 4;
        }

        if (channel6_Write.isSelected()) {
            return 5;
        }

        if (channel7_Write.isSelected()) {
            return 6;
        }

        if (channel8_Write.isSelected()) {
            return 7;
        }

        return -1;
    }

    public int whichChannelIsUsedtoRead(JRadioButton channel1_Read, JRadioButton channel2_Read,
            JRadioButton channel3_Read, JRadioButton channel4_Read, JRadioButton channel5_Read,
            JRadioButton channel6_Read, JRadioButton channel7_Read, JRadioButton channel8_Read) {

        if (channel1_Read.isSelected()) {
            return 0;
        }

        if (channel2_Read.isSelected()) {
            return 1;
        }

        if (channel3_Read.isSelected()) {
            return 2;
        }

        if (channel4_Read.isSelected()) {
            return 3;
        }

        if (channel5_Read.isSelected()) {
            return 4;
        }

        if (channel6_Read.isSelected()) {
            return 5;
        }

        if (channel7_Read.isSelected()) {
            return 6;
        }

        if (channel8_Read.isSelected()) {
            return 7;
        }

        return -1;
    }

    public boolean isEqualChannel(JRadioButton channelWrite, JRadioButton channelRead) {
        if (channelWrite.isSelected() && channelRead.isSelected()) {
            return true;
        }
        return false;
    }

    public boolean isSameNumberReadAndWriteChannel(JRadioButton channel1_Write, JRadioButton channel2_Write,
            JRadioButton channel3_Write, JRadioButton channel4_Write, JRadioButton channel5_Write,
            JRadioButton channel6_Write, JRadioButton channel7_Write, JRadioButton channel8_Write,
            JRadioButton channel1_Read, JRadioButton channel2_Read, JRadioButton channel3_Read,
            JRadioButton channel4_Read, JRadioButton channel5_Read, JRadioButton channel6_Read,
            JRadioButton channel7_Read, JRadioButton channel8_Read) {
        if (this.isEqualChannel(channel1_Write, channel1_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel2_Write, channel2_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel3_Write, channel3_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel4_Write, channel4_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel5_Write, channel5_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel6_Write, channel6_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel7_Write, channel7_Read)) {
            return true;
        }
        if (this.isEqualChannel(channel8_Write, channel8_Read)) {
            return true;
        }
        return false;
    }

    public boolean noOneWriteChanelSelected(JRadioButton channel1_Write, JRadioButton channel2_Write,
            JRadioButton channel3_Write, JRadioButton channel4_Write, JRadioButton channel5_Write,
            JRadioButton channel6_Write, JRadioButton channel7_Write, JRadioButton channel8_Write) {

        if ((!channel1_Write.isSelected()) && (!channel2_Write.isSelected())
                && (!channel3_Write.isSelected()) && (!channel4_Write.isSelected())
                && (!channel5_Write.isSelected()) && (!channel6_Write.isSelected())
                && (!channel7_Write.isSelected()) && (!channel8_Write.isSelected())) {
            return true;
        }
        return false;

    }

    public boolean noOneReadChanelSelected(JRadioButton channel1_Read, JRadioButton channel2_Read,
            JRadioButton channel3_Read, JRadioButton channel4_Read, JRadioButton channel5_Read,
            JRadioButton channel6_Read, JRadioButton channel7_Read, JRadioButton channel8_Read) {

        if ((!channel1_Read.isSelected()) && (!channel2_Read.isSelected())
                && (!channel3_Read.isSelected()) && (!channel4_Read.isSelected())
                && (!channel5_Read.isSelected()) && (!channel6_Read.isSelected())
                && (!channel7_Read.isSelected()) && (!channel8_Read.isSelected())) {
            return true;
        }
        return false;

    }

    public void validateTypeSystem(JRadioButton Radio_1, JRadioButton Radio_2, JRadioButton Radio_Selected) {


        if (Radio_1 != Radio_Selected) {
            Radio_1.setSelected(false);
        }
        if (Radio_2 != Radio_Selected) {
            Radio_2.setSelected(false);
        }

        if (Radio_1 == Radio_Selected && (!Radio_1.isSelected())) {
            Radio_1.setSelected(true);
        }

        if (Radio_2 == Radio_Selected && (!Radio_2.isSelected())) {
            Radio_2.setSelected(true);
        }


    }

    public boolean estaNoCirculoUnitarioP1(JTextField txtf_valorRealP1, JTextField txtf_valorImaginarioP1) {

        try {
            double P1real, P1imaginario;
            P1real = Double.parseDouble(txtf_valorRealP1.getText());
            P1imaginario = Double.parseDouble(txtf_valorImaginarioP1.getText());


            if (P1real * P1real + P1imaginario * P1imaginario <= 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public void DeixarP2IgualP1(JTextField txtf_valorRealP1, JTextField txtf_valorImaginarioP1, JTextField txtf_valorRealP2, JTextField txtf_valorImaginarioP2, Estimador estimador) {


        try {
            double im;
            im = Double.parseDouble(txtf_valorImaginarioP1.getText());

            if (im != 0) {
                txtf_valorRealP2.setText(txtf_valorRealP1.getText());
                im = -1 * im;
                txtf_valorImaginarioP2.setText(Double.toString(im));
            } else {
                txtf_valorImaginarioP2.setText(Double.toString(im));
            }

            estimador.setParteRealP1(Double.parseDouble(txtf_valorRealP1.getText()));
            estimador.setParteImaginariaP1(Double.parseDouble(txtf_valorImaginarioP1.getText()));
            estimador.setParteRealP2(Double.parseDouble(txtf_valorRealP2.getText()));
            estimador.setParteImaginariaP2(Double.parseDouble(txtf_valorImaginarioP2.getText()));
            estimador.updateComplex();
            
        } catch (Exception e) {
        }

    }

    public void DeixarP1IgualP2(JTextField txtf_valorRealP1, JTextField txtf_valorImaginarioP1, JTextField txtf_valorRealP2, JTextField txtf_valorImaginarioP2, Estimador estimador) {
        try {
            double im;
            im = Double.parseDouble(txtf_valorImaginarioP2.getText());

            if (im != 0) {
                txtf_valorRealP1.setText(txtf_valorRealP2.getText());
                im = -1 * im;
                txtf_valorImaginarioP1.setText(Double.toString(im));
            } else {
                txtf_valorImaginarioP1.setText(Double.toString(im));
            }

            estimador.setParteRealP1(Double.parseDouble(txtf_valorRealP1.getText()));
            estimador.setParteImaginariaP1(Double.parseDouble(txtf_valorImaginarioP1.getText()));
            estimador.setParteRealP2(Double.parseDouble(txtf_valorRealP2.getText()));
            estimador.setParteImaginariaP2(Double.parseDouble(txtf_valorImaginarioP2.getText()));
            estimador.updateComplex();

        } catch (Exception e) {
        }

    }

    public boolean estaNoCirculoUnitarioP2(JTextField txtf_valorRealP2, JTextField txtf_valorImaginarioP2) {

        try {
            double P2real, P2imaginario;
            P2real = Double.parseDouble(txtf_valorRealP2.getText());
            P2imaginario = Double.parseDouble(txtf_valorImaginarioP2.getText());

            if (P2real * P2real + P2imaginario * P2imaginario <= 1) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }






    }

    public void atualizarCamposDaMatriz(Estimador estimador, JTextField txtf_valorSuperiorDaMatrizL, JTextField txtf_valorInferiorDaMatrizL) {

        estimador.determinarAMatrizL();

        txtf_valorSuperiorDaMatrizL.setText(decimalFormat.format(estimador.getL1()));
        txtf_valorInferiorDaMatrizL.setText(decimalFormat.format(estimador.getL2()));
        
//        txtf_valorSuperiorDaMatrizL.setText(Double.toString(estimador.getL1()));
//        txtf_valorInferiorDaMatrizL.setText(Double.toString(estimador.getL2()));
    }
    
    public void atualizarCamposDosGanhosSeguidorReferencia(SeguidorDeRerenciaDiscreto seguidor, JTextField k1, JTextField k21, JTextField k22){
        
        seguidor.calcularKzinhos();
        
        
    }
    
}
