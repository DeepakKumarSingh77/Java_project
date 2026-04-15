# Social Media Application - Low Level Design

## Overview
This is a Java-based implementation of a Social Media Application's low-level design (LLD). It demonstrates core features like user management, posting (images/videos), commenting, liking, feed generation, and notifications using object-oriented principles.

## Features
- **User Management**: Create users with followers.
- **Posts**: Support for image and video posts.
- **Interactions**: Comments (with replies) and likes on posts/comments.
- **Feed Generation**: Implements Push and Pull FanOut strategies for generating user feeds.
- **Notifications**: Send and retrieve notifications for users.

## Architecture
### Key Classes
- `User`: Represents a user with followers and posts.
- `Post` (Abstract): Base class for posts, with subclasses `ImagePost` and `VideoPost`.
- `Comment`: Handles comments and replies.
- `Like`: Represents likes on posts or comments.
- `FanOutStrategy`: Interface for feed generation strategies (`PushFanOutStrategy`, `PullFanOutStrategy`).
- `FeedService`: Generates feeds based on follower count.
- `NotificationService`: Manages notifications.

### Design Patterns
- **Strategy Pattern**: For feed generation (Push vs Pull).
- **Factory Pattern**: (Not fully implemented in this demo, but extensible).
- **Observer Pattern**: Implicit in notifications.

## How to Run
1. Ensure Java is installed (JDK 8+).
2. Compile the code: `javac Main.java`
3. Run the application: `java Main`

## Example Output
```
---- FEED FOR ALICE ----
Post by: Bob | Content: Bob's Image Post | Likes: 1
Post by: Charlie | Content: Charlie's Video | Likes: 0

---- NOTIFICATIONS FOR BOB ----
Alice liked your post
Alice commented on your post
```

## Notes
- This is a simplified LLD example for educational purposes.
- No persistence layer; data is in-memory.
- FanOut strategy switches based on follower count (>10k uses Pull, else Push).