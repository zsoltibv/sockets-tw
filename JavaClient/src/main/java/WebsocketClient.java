
import com.example.jettyclient.Message;
import com.example.jettyclient.MessageDecoder;
import com.example.jettyclient.MessageEncoder;
import com.example.jettyclient.MessageType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.*;

@ClientEndpoint( decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebsocketClient {
    private final String uri ="ws://localhost:8080/chat/";
    private Session session;

    public WebsocketClient(String name){
        String userConURI = uri + name;
        try{
            WebSocketContainer container=ContainerProvider.
                    getWebSocketContainer();
            container.connectToServer(this, new URI(userConURI));
        }catch(Exception ex){
            System.err.println("Connection error");
        }

    }

    @OnOpen
    public void onOpen(Session session) throws URISyntaxException, DeploymentException, IOException {
        this.session=session;
    }

    @OnMessage
    public void onMessage(Message message, Session session){
        switch (message.getType()){
            case VIEW_ALL_USERS: {
                System.out.println(message.getContent());
            }
            case VIEW_ALL_GROUPS: {
                System.out.println(message.getContent());
            }
            case CREATE_GROUP: {

            }
            break;
            default: {
                /* NOTHING */
            }
        }
    }

    public void sendMessage(Message message){
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException ex) {
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }
}