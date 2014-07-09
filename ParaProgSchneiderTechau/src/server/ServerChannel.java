package server;

import org.jcsp.lang.*;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel implements CSProcess {

    private ChannelOutput output;
    private ChannelInput input;
    public One2OneChannel o2oChannelOutPut;

    boolean tog;



    public ServerChannel(boolean tog){
        //-- Ausgehender Daten; von uns gesendet
        o2oChannelOutPut = Channel.one2one();
        output = o2oChannelOutPut.out();
        this.tog = tog;
    }


    public void setInputChannel(One2OneChannel arg){
        if(input == null & arg != null) {
            input = arg.in();
        }
    }

    public void send(Message arg){
        output.write(arg);
    }

    public Message recive(){
        return (Message) input.read();
    }

    @Override
    public void run() {

        if(tog) {
            send(new Message(5));
        }
        else {
            Message iIn = (Message) input.read();
            System.out.println(iIn.i*2);

        }

    }
}
