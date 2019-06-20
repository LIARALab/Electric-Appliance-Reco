package mainAPI.supTools;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collection;

public class WebSocketServerWrapper extends WebSocketServer {

    public boolean isOpen;

    public WebSocketServerWrapper(int port)
    {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3)
    {
        isOpen = false;
    }

    @Override
    public void onError(WebSocket arg0, Exception arg1)
    {
        // Do nothing
    }

    @Override
    public void onMessage(WebSocket arg0, String arg1)
    {
        // Do nothing
    }

    @Override
    public void onOpen(WebSocket arg0, ClientHandshake arg1)
    {
        isOpen = true;
    }

    /**
     * Tells the server to send a message
     * @param message The message to send
     */
    public void send(String message)
    {
        Collection<WebSocket> con = connections();

        for (WebSocket s:con)
        {
            System.out.println(message);
            s.send(message);
        }

    }

    public boolean getIsOpen()
    {
        return isOpen;
    }

}
