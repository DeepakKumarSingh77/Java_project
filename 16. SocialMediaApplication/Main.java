import java.util.*;

class User {
    private String userId;
    private String name;
    private String email;

    private Set<User> followers = new HashSet<>();
    private List<Post> posts = new ArrayList<>();

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void addFollower(User user) {
        followers.add(user);
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getPosts() {
        return posts;
    }
}

abstract class Post {
    protected String postId;
    protected String content;
    protected User author;

    protected List<Comment> comments = new ArrayList<>();
    protected List<Like> likes = new ArrayList<>();

    protected long createdAt;

    public Post(String postId, String content, User author) {
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.createdAt = System.currentTimeMillis();
    }

    public String getPostId() {
        return postId;
    }

    public User getAuthor() {
        return author;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public int getLikesCount() {
        return likes.size();
    }

    public List<Comment> getComments() {
        return comments;
    }
}

class ImagePost extends Post {
    private String imageUrl;

    public ImagePost(String postId, String content, User author, String imageUrl) {
        super(postId, content, author);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

class VideoPost extends Post {
    private String videoUrl;

    public VideoPost(String postId, String content, User author, String videoUrl) {
        super(postId, content, author);
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}


class Comment {
    private String commentId;
    private String content;
    private User author;
    private Post post;

    private Comment parentComment;
    private List<Comment> replies = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();

    public Comment(String commentId, String content, User author,
                   Post post, Comment parentComment) {
        this.commentId = commentId;
        this.content = content;
        this.author = author;
        this.post = post;
        this.parentComment = parentComment;
    }

    public String getCommentId() {
        return commentId;
    }

    public User getAuthor() {
        return author;
    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public List<Comment> getReplies() {
        return replies;
    }
}

class Like {
    private String likeId;
    private User user;
    private String targetId; // postId or commentId

    public Like(String likeId, User user, String targetId) {
        this.likeId = likeId;
        this.user = user;
        this.targetId = targetId;
    }

    public User getUser() {
        return user;
    }

    public String getTargetId() {
        return targetId;
    }
}

interface FanOutStrategy {
    List<Post> generateFeed(User user);
}

// Precompute feed — faster reads
class PushFanOutStrategy implements FanOutStrategy {

    @Override
    public List<Post> generateFeed(User user) {
        List<Post> feed = new ArrayList<>();

        for (User follower : user.getFollowers()) {
            feed.addAll(follower.getPosts());
        }

        feed.sort((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));

        return feed;
    }
}

// Compute feed on demand — simple
class PullFanOutStrategy implements FanOutStrategy {
    @Override
    public List<Post> generateFeed(User user) {
        List<Post> feed = new ArrayList<>();

        for (User follower : user.getFollowers()) {
            feed.addAll(follower.getPosts());
        }

        feed.sort((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));

        return feed;
    }
}

class FeedService {
    private FanOutStrategy strategy;
    public List<Post> getFeed(User user) {
        if(user.getFollowers().size() > 10000) {
            strategy = new PullFanOutStrategy();
        } else {
            strategy = new PushFanOutStrategy();
        }
        return strategy.generateFeed(user);
    }
}

class Notification {
    private String notificationId;
    private User receiver;
    private String message;
    private boolean isRead;
    private long createdAt;

    public Notification(String id, User receiver, String message) {
        this.notificationId = id;
        this.receiver = receiver;
        this.message = message;
        this.isRead = false;
        this.createdAt = System.currentTimeMillis();
    }

    public void markAsRead() {
        isRead = true;
    }

    public String getMessage() {
        return message;
    }
}


class NotificationService {

    private Map<String, List<Notification>> notifications = new HashMap<>();

    public void sendNotification(User user, String message) {
        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                user,
                message
        );

        notifications
            .computeIfAbsent(user.getUserId(), k -> new ArrayList<>())
            .add(notification);
    }

    public List<Notification> getNotifications(User user) {
        return notifications.getOrDefault(user.getUserId(), new ArrayList<>());
    }
}

public class Main {
    public static void main(String[] args) {

        User alice = new User("U1", "Alice", "alice@mail.com");
        User bob = new User("U2", "Bob", "bob@mail.com");
        User charlie = new User("U3", "Charlie", "charlie@mail.com");

        alice.addFollower(bob);
        alice.addFollower(charlie);

        Post post1 = new ImagePost("P1", "Bob's Image Post", bob, "img1.jpg");
        Post post2 = new VideoPost("P2", "Charlie's Video", charlie, "video1.mp4");

        bob.addPost(post1);
        charlie.addPost(post2);

        Comment c1 = new Comment("C1", "Nice post!", alice, post1, null);
        post1.addComment(c1);

        Comment reply = new Comment("C2", "Thanks!", bob, post1, c1);
        c1.addReply(reply);

        Like like1 = new Like("L1", alice, post1.getPostId());
        post1.addLike(like1);

        Like like2 = new Like("L2", bob, c1.getCommentId());
        c1.addLike(like2);

        FeedService feedService = new FeedService();
        List<Post> feed = feedService.getFeed(alice);

        System.out.println("---- FEED FOR ALICE ----");
        for (Post p : feed) {
            System.out.println("Post by: " + p.getAuthor().getName() +
                               " | Content: " + p.content +
                               " | Likes: " + p.getLikesCount());
        }

        NotificationService notificationService = new NotificationService();

        notificationService.sendNotification(bob, "Alice liked your post");
        notificationService.sendNotification(bob, "Alice commented on your post");

        List<Notification> bobNotifications =
                notificationService.getNotifications(bob);

        System.out.println("\n---- NOTIFICATIONS FOR BOB ----");
        for (Notification n : bobNotifications) {
            System.out.println(n.getMessage());
        }
    }
}