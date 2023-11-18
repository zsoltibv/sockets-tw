package com.example.webscokets;

import com.example.webscokets.dbRunTime.Group;
import com.example.webscokets.dbRunTime.Groups;
import com.example.webscokets.dbRunTime.User;
import com.example.webscokets.dbRunTime.Users;
import com.example.webscokets.model.Message;
import com.example.webscokets.model.MessageDecoder;
import com.example.webscokets.model.MessageEncoder;
import com.example.webscokets.model.MessageType;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private Session session;
    //private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    //private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        User user = new User();
        user.setName(username);
        user.setSession(session);
        Users.INSTANCE.users.add(user);
        System.out.println("User: " + username);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getType()){
            case CREATE_GROUP:{
                Group group = new Group();
                group.setName(message.getContent());
                Groups.INSTANCE.groups.add(group);
            }
            break;
            case MESSAGE_TO_GROUP: {
                String groupName = message.getTo();
                Optional<Group> groupOptional = Groups.INSTANCE.groups
                        .stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst();

                if (groupOptional.isPresent()) {
                    Group group = groupOptional.get();

                    // Iterate through the users in the group and send the message to each user
                    for (User user : group.getUsers()) {
                        // Skip sending the message to the sender
                        if (!user.getSession().equals(session)) {
                            // Create a new message for each user in the group
                            Message groupMessage = new Message();
                            groupMessage.setType(MessageType.MESSAGE_TO_GROUP);
                            groupMessage.setFrom(message.getFrom()); // Set the sender
                            groupMessage.setTo(groupName);
                            groupMessage.setContent(message.getContent()); // Set the message content

                            // Send the message to the user
                            sendMessage(user.getSession(), groupMessage);
                        }
                    }
                }
            }
            break;
            case ADD_TO_GROUP: {
                /* Message.to: group
                *  Message.content: username */
                String groupName = message.getTo();
                Optional<Group> firstGroup = Groups.INSTANCE.groups
                        .stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst();

                Optional<User> firstUser = Users.INSTANCE.users
                        .stream()
                        .filter(user -> user.getName().equals(message.getContent()))
                        .findFirst();

                if(firstGroup.isPresent() && firstUser.isPresent()){
                    firstGroup.get().getUsers().add(firstUser.get());
                }
            }
            case VIEW_ALL_GROUPS: {
                String groups = Groups.INSTANCE.groups
                        .stream()
                        .map(Group::toString)
                        .reduce((s, s2) -> s + ", " + s2)
                        .get();

                Message allGroupsMessage = new Message();
                allGroupsMessage.setContent(groups);
                allGroupsMessage.setType(MessageType.VIEW_ALL_GROUPS);

                this.sendMessage(allGroupsMessage);
            }
            break;
            case VIEW_ALL_USERS: {
                String users = Users.INSTANCE.users
                        .stream()
                        .map(User::getName)
                        .reduce((s, s2) -> s + ", " + s2)
                        .get();

                Message allUsersMessage = new Message();
                allUsersMessage.setContent(users);
                allUsersMessage.setType(MessageType.VIEW_ALL_USERS);
                /* TODO: Return "Mihai, Ioana, Alina, ..."*/
                this.sendMessage(allUsersMessage);
            }
            break;
        }
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error " + throwable);
    }

    private static void broadcast(Message message)  {
        // chatEndpoints.forEach(endpoint -> {
        //     synchronized (endpoint) {
        //         try {
        //             endpoint.session.getBasicRemote()
        //                     .sendObject(message);
        //         } catch (IOException | EncodeException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // });
    }

    private void sendMessage(Message message){
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(Session session, Message message){
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            throw new RuntimeException(e);
        }
    }
}