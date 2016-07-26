## TommyTugger
#### Version 0.1

A JSON pull parser, sax style push parser and a JSON writer

### Puller

```java
Event next;
Puller jsonPuller = new Puller(/*Your string or InputStream*/);
while((next = jsonPuller.next()).getEventType() != EventType.DocumentFinished) {
  /* If event type is key or value then next.getValue() will be populated */	
}
```

### Pusher

```java
Pusher pusher = new Pusher();
pusher.parse(/* Your string or InputStream */, (event) -> {
  /* If event type is key or value then next.getValue() will be populated */	
});
```
