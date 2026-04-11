import java.time.LocalDateTime;
import java.util.*;

class Message {
    private String messageId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
    private int sequenceId;

    public Message(String messageId, String senderId, String content) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.sequenceId = -1; // assigned later during processing
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessageId() {
        return messageId;
    }
}

class User {
    private String userId;
    private String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}

class Participant {
    private User user;
    private String role; // "admin" or "member"
    private long lastActive; // timestamp
     private Observer observer;

    public Participant(User user, String role, Observer observer) {
        this.user = user;
        this.role = role;
        this.lastActive = System.currentTimeMillis();
        this.observer = observer;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public long getLastActive() {
        return lastActive;
    }

    public void updateLastActive() {
        this.lastActive = System.currentTimeMillis();
    }

    public Observer getObserver() {
        return observer;
    }
}

class ChatRoom {
    private String chatId;
    private List<Participant> participants;
    private List<Message> messages;
    private int lastSequenceId;
    private NotificationEngine notificationEngine;

    public ChatRoom(String chatId) {
        this.chatId = chatId;
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.lastSequenceId = 0;
        this.notificationEngine = new NotificationEngine(); 
    }

    public String getChatId() {
        return chatId;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getNextSequenceId() {
        return ++lastSequenceId;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);

        notificationEngine.addObserver(participant.getObserver());
    }

    public boolean isUserInChat(String userId) {
        for (Participant p : participants) {
            if (p.getUser().getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public NotificationEngine getNotificationEngine() {
        return notificationEngine;
    }
}

class ChatService {

    private Map<String, ChatRoom> chatRooms = new HashMap<>();
    private Map<String, Queue<Message>> messageQueues = new HashMap<>();

    public void createChatRoom(String chatId) {
        chatRooms.put(chatId, new ChatRoom(chatId));
        messageQueues.put(chatId, new LinkedList<>());
    }

    public void addParticipant(String chatId, Participant participant) {
        ChatRoom chatRoom = chatRooms.get(chatId);
        if (chatRoom != null) {
            chatRoom.addParticipant(participant);
        }
    }

    public void sendMessage(String chatId, String senderId, String content) {
        ChatRoom chatRoom = chatRooms.get(chatId);

        if (chatRoom == null) {
            throw new RuntimeException("ChatRoom not found");
        }

        if (!chatRoom.isUserInChat(senderId)) {
            throw new RuntimeException("User not in chat");
        }

        // create message (no sequenceId yet)
        Message message = new Message(UUID.randomUUID().toString(), senderId, content);

        // push to queue
        messageQueues.get(chatId).offer(message);
    }

    // Simulates message processing (like background worker)
    public void processMessages(String chatId) {
        Queue<Message> queue = messageQueues.get(chatId);
        ChatRoom chatRoom = chatRooms.get(chatId);

        while (!queue.isEmpty()) {
            Message msg = queue.poll();

            // assign sequenceId
            int seqId = chatRoom.getNextSequenceId();
            msg.setSequenceId(seqId);

            // store message
            chatRoom.addMessage(msg);

            // Notify ONLY this chat’s observers
            chatRoom.getNotificationEngine().notifyObservers(msg,msg.getSenderId());
        }
    }
}

interface Observer {
    void update(Message message);
    String getUserId();
}

class NotificationEngine {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Message message, String senderId) {
        for (Observer observer : observers) {
            if (!observer.getUserId().equals(senderId)) {
                 observer.update(message);
            }
        }
    }
}

class UserDevice implements Observer {
    private String userId;

    public UserDevice(String userId) {
        this.userId = userId;
    }

    @Override
    public void update(Message message) {
        System.out.println("User " + userId + " received: " + message.getContent());
    }

    @Override
    public String getUserId() {
        return userId;
    }
}


public class Main {
    public static void main(String[] args) {

        ChatService chatService = new ChatService();

        User u1 = new User("u1", "Aman");
        User u2 = new User("u2", "Rahul");

        UserDevice d1 = new UserDevice("u1");
        UserDevice d2 = new UserDevice("u2");

        Participant p1 = new Participant(u1, "admin", d1);
        Participant p2 = new Participant(u2, "member", d2);

        chatService.createChatRoom("chat1");

        chatService.addParticipant("chat1", p1);
        chatService.addParticipant("chat1", p2);

        chatService.sendMessage("chat1", "u1", "Hello Rahul!");

        chatService.processMessages("chat1");
    }
}