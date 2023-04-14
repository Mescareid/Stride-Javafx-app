package com.example.networkfx.service;

import com.example.networkfx.domain.Message;
import com.example.networkfx.domain.validators.ValidationException;
import com.example.networkfx.repository.database.MessageDatabaseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceMessage{
    private final MessageDatabaseRepository RepoMessage;

    public ServiceMessage(MessageDatabaseRepository repository){
        RepoMessage = repository;
    }

    public Message addMessage(String fromUser, String toUser, String text, LocalDateTime date) throws ValidationException {

        Message message = new Message(fromUser, toUser, text, date);
        return RepoMessage.save(message);
    }

    public List<Message> findAllMessages(){
        return StreamSupport.stream(RepoMessage.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<Message> findConversation(String username1, String username2){
        List<Message> conversation = new ArrayList<>();
        for(Message m : findAllMessages()){
            if(m.getFromUser().equals(username1) && m.getToUser().equals(username2)){
                conversation.add(m);
            }
            if(m.getToUser().equals(username1) && m.getFromUser().equals(username2)){
                conversation.add(m);
            }
        }
        
        Comparator<Message> comparatorAsc = Comparator.comparing(Message::getDate);
        conversation.sort(comparatorAsc);

        return conversation;
    }
}
