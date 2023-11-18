import com.example.jettyclient.Message;
import com.example.jettyclient.MessageType;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /* Read the username from keyboard */
        System.out.println("Insert username: ");
        String name = scanner.nextLine();

        WebsocketClient client = new WebsocketClient(name);

        System.out.println("1. CREATE GROUP; \n " +
                "2. ADD TO GROUP; \n" +
                "3. MESSAGE TO GROUP;\n " +
                "4. VIEW ALL USERS; \n" +
                "5. VIEW ALL GROUPS;\n " +
                "6. EXIT\n");

        Boolean isRunning = true;
        while(isRunning){
            Integer option = scanner.nextInt();
            switch (option) {
                case 1: {
                    System.out.println("Insert group name: ");
                    Scanner scannerReadGroupName = new Scanner(System.in);
                    String groupName = scannerReadGroupName.nextLine();

                    Message messageCreateGroup = new Message();
                    messageCreateGroup.setType(MessageType.CREATE_GROUP);
                    messageCreateGroup.setContent(groupName);

                    client.sendMessage(messageCreateGroup);
                }
                break;
                case 2: {
                    System.out.println("Group name: ");
                    Scanner scannerReadGroupName = new Scanner(System.in);
                    String groupName = scannerReadGroupName.nextLine();

                    System.out.println("User name: ");
                    Scanner scannerUserName = new Scanner(System.in);
                    String userToAdd = scannerUserName.nextLine();

                    Message messageAddToGroup = new Message();
                    messageAddToGroup.setType(MessageType.ADD_TO_GROUP);
                    messageAddToGroup.setTo(groupName);
                    messageAddToGroup.setContent(userToAdd);

                    client.sendMessage(messageAddToGroup);
                }
                break;
                case 3: {
                    System.out.println("Group name: ");
                    Scanner scannerReadGroupName = new Scanner(System.in);
                    String groupName = scannerReadGroupName.nextLine();

                    System.out.println("Enter your message: ");
                    Scanner scannerMessage = new Scanner(System.in);
                    String messageContent = scannerMessage.nextLine();

                    Message messageToGroup = new Message();
                    messageToGroup.setType(MessageType.MESSAGE_TO_GROUP);
                    messageToGroup.setTo(groupName);
                    messageToGroup.setContent(messageContent);
                    messageToGroup.setFrom(name);

                    client.sendMessage(messageToGroup);
                }
                break;
                case 4 : {
                    Message messageViewAllUsers = new Message();
                    messageViewAllUsers.setFrom(name);
                    messageViewAllUsers.setType(MessageType.VIEW_ALL_USERS);
                    client.sendMessage(messageViewAllUsers);
                }
                break;
                case 5 : {
                    Message messageViewAllGroups = new Message();
                    messageViewAllGroups.setFrom(name);
                    messageViewAllGroups.setType(MessageType.VIEW_ALL_GROUPS);
                    client.sendMessage(messageViewAllGroups);
                }
                break;
                default: {
                    /* NOTHING */
                }
            }
        }
    }
}
