import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ServerChannel;
import org.jpos.iso.channel.PostChannel;
import org.jpos.util.*;
import packager.Tranzware_Packager;

public class ServerApplication {

    public static void main(String[] args){
        int PORT = 4444;

        Logger logger = new Logger();
        logger.addListener((LogListener) new SimpleLogListener(System.out));
        PostChannel channel = new PostChannel((ISOPackager) new Tranzware_Packager());
        channel.setHeader("6004003800");

        ((LogSource) channel).setLogger(logger, "Sample Gateway");
        ThreadPool serverPool = new ThreadPool(10, 1000);

        ISOServer server = new ISOServer(PORT, (ServerChannel) channel, serverPool);

        server.setLogger(logger, "server");

        server.addISORequestListener(((source, m) -> {
            m.dump(System.out, "INCOMING MESSAGE");
            try{
                m.set(39, "00");
            }catch(ISOException exception){
                exception.printStackTrace();
            }
            return true;
        }));
        server.run();
    }
}
