# Chatting Application Design

A low-level design (LLD) demonstration of a real-time multi-user messaging system using core design patterns.

---

## 🎯 What This Project Demonstrates

This system models **how chat applications like WhatsApp, Slack, and Discord** handle:
- Multiple users communicating in rooms
- Message ordering and sequencing
- Real-time notifications to participants
- Event-driven architecture

---

## 🏗️ Core Design Patterns

### 1. **Observer Pattern** (Behavioral)
**Problem:** How do multiple users get notified when a message arrives?

**Solution:** Users subscribe to a notification engine. When messages arrive, the engine notifies all subscribed users (except the sender).

**Real-World:** Push notifications, email subscriptions, live updates

---

### 2. **Message Queue Pattern** (Concurrency)
**Problem:** Messages might arrive faster than we can process them. How do we maintain order?

**Solution:** Store messages in a FIFO queue. Process them sequentially to guarantee ordering.

**Real-World:** Order confirmation systems, transaction processing, event streaming

---

### 3. **Repository Pattern** (Data Access)
**Problem:** How to manage multiple chat rooms and messages centrally?

**Solution:** A service class acts as a repository - single source of truth for all data operations.

**Real-World:** Database abstraction layers, API gateways

---

## 🏛️ System Architecture

```
User1 → Participant → ChatRoom → NotificationEngine → User2
User2 →              ↓                    ↓
                  MessageQueue       Observer listeners
                  (Sequential)       (Async Notifications)
```

### Key Components

| Component | Role |
|-----------|------|
| **User** | Represents a person |
| **Participant** | User's role/status in a specific chat room |
| **ChatRoom** | Container for participants, messages, and notifications |
| **Message** | Single chat message with sender, content, and sequence ID |
| **ChatService** | Main coordinator - manages rooms and message processing |
| **NotificationEngine** | Broadcasts messages to all observers |
| **UserDevice (Observer)** | Client endpoint that receives notifications |
| **MessageQueue** | FIFO queue ensuring sequential processing |

---

## 📤 How It Works

```
1. User sends message → Message queued (not processed yet)
2. Process messages → Assign sequence number for ordering
3. Store message in ChatRoom
4. Notify all observers → Each observer updates their client
```

**Why this approach?**
- **Async Processing:** Sending and processing are separate steps
- **Guaranteed Ordering:** Sequence IDs ensure messages appear in same order everywhere
- **Scalability:** Queue acts as a buffer during traffic spikes

---

## 🔑 Key Concepts

### Message Ordering
Messages get a **sequence ID** (1, 2, 3...). This ensures all users see messages in the same order, even with network delays.

### Causal Consistency
If User A sends "Hello" then "World", all users must receive "Hello" before "World". Our sequence IDs guarantee this.

### Presence Detection
Track `lastActive` timestamp to show "User is online/away/offline" status.

### Permissions
Users have roles: "admin" (can delete messages) or "member" (participant only).

### Multi-Device Support
Same user can have multiple observers (phone, desktop, web). All get notifications.

---

## 🚀 Production Considerations

| Challenge | Our Approach | Production Solution |
|-----------|-------------|-------------------|
| Single server scaling | Works locally | Load balancer + multiple instances |
| In-memory storage | Data in RAM | Database persistence (MongoDB/PostgreSQL) |
| Message ordering | Sequence counter | Distributed timestamps (Lamport/Vector clocks) |
| Direct notification | Synchronous calls | Message broker (Kafka/RabbitMQ) |
| Network failures | No handling | Retry logic + acknowledgments |
| Data consistency | Single server | Replication + consensus algorithms |

---

## 🎓 Core Concepts to Master

### 1. **Observer Pattern**
- Users "subscribe" to notifications
- Loose coupling between sender and receivers
- Can replace `UserDevice` with `EmailNotifier`, `SMSNotifier`, etc.

### 2. **Asynchronous Processing**
- Separate send (queue) from process (notify) steps
- Prevents blocking on slow operations
- Real-world: async/await, promises, futures

### 3. **Distributed Ordering**
- **Challenge:** Ensuring all users see messages in same order across network
- **Solution:** Logical sequence numbers or vector clocks
- **Production:** Google's Lamport Timestamps, Amazon's CRDT

### 4. **Event-Driven Architecture**
- System reacts to events (message received, user joined, etc.)
- Instead of polling for changes, listeners get notified
- Foundation of modern microservices

### 5. **Thread Safety**
- `lastSequenceId++` is NOT atomic
- Multiple threads can read same value
- Fix: `AtomicInteger` or synchronized blocks

---

## 📚 Interview Questions

**What's the Observer Pattern?**
Design pattern where objects subscribe to state changes. When state changes, all subscribers are notified automatically.

**Why use a message queue instead of direct notifications?**
Ensures FIFO ordering, handles burst traffic, and allows async processing. Prevents overwhelm if messages arrive faster than processing.

**How do you prevent users from receiving their own messages?**
Filter in notification: `if (!observer.getUserId().equals(senderId)) notify();`

**Scale this system to 1M users. What changes?**
- Remove in-memory storage → use database
- Single server → multiple servers behind load balancer
- Direct calls → message broker (Kafka)
- Sequence counter → vector clocks or logical timestamps
- Add replication for fault tolerance

**What's the problem with `lastSequenceId++` in multithreading?**
Race condition. Multiple threads can read the same value and lose increment operations. Use `AtomicInteger.incrementAndGet()`.

**How to implement end-to-end encryption?**
- Each user has public/private key pair
- Encrypt messages with recipient's public key
- Only recipient can decrypt with private key
- Server never sees plaintext

---

## 🎬 Running the Code

```bash
# Compile
javac Main.java

# Run
java Main

# Output:
# User u2 received: Hello Rahul!
```

---

## 💡 Learning Path

**Beginner:** Understand Message, User, Participant, ChatRoom classes and how they connect.

**Intermediate:** Learn Observer pattern, message queue concept, add database persistence, implement user roles/permissions.

**Advanced:** Distribute across multiple servers, implement vector clocks for ordering, add end-to-end encryption, handle network partitions.

---

## 📊 System Design Summary

✅ Observer pattern for decoupled notifications
✅ Message queue for guaranteed ordering
✅ Service layer for centralized logic
✅ Asynchronous processing separates sending from notification
✅ Production-ready foundation for WhatsApp/Slack-scale systems

This is the **architectural blueprint** of real messaging platforms. Understanding these patterns helps you:
- Design scalable systems
- Pass system design interviews
- Build reliable distributed applications

