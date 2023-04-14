package com.example.networkfx.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long>{
    private String fromUser;
    private String toUser;
    private String text;
    private LocalDateTime date;

    public Message(String fromUser, String toUser, String text, LocalDateTime date) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.text = text;
        this.date = date;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return getFromUser().equals(message.getFromUser()) && getToUser().equals(message.getToUser()) && getText().equals(message.getText()) && getDate().equals(message.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromUser(), getToUser(), getText(), getDate());
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
