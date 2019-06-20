package mainAPI.supTools;

import java.util.Queue;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;
import org.java_websocket.server.WebSocketServer;

public class WebSocketServerProcessor extends SynchronousProcessor
{
    private  WebSocketServerWrapper sendingMessage = null;
    private int m_serverPort;

    WebSocketServerProcessor()
    {
        super(1, 0);
    }

    public WebSocketServerProcessor(int port)
    {
        this();
        m_serverPort = port;
        sendingMessage = new WebSocketServerWrapper(port);
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        System.out.println(inputs[0]);

        if (sendingMessage.getIsOpen() == true)
        {
            sendingMessage.send((String) inputs[0]);
        }

        return false;
    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new WebSocketServerProcessor(m_serverPort);
    }

    /**
     * Gets an instance of web socket server associated to this
     * processor
     * @return The web socket server
     */
    public WebSocketServer getWebSocketServer()
    {
        return  sendingMessage;
    }

}
