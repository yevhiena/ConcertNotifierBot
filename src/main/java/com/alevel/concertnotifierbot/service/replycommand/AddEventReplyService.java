package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserService;
import com.alevel.concertnotifierbot.service.dataoperations.UserServiceImpl;
import com.alevel.concertnotifierbot.util.Emoji;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;

@Slf4j
@Service
public class AddEventReplyService implements ReplyCommandService{
    private UserService userService;

    public AddEventReplyService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage getReply(long chatId) {
        String message;

        try {
            boolean admin = userService.isAdmin(chatId);
            if(!admin) message ="Sorry, only admin can add concerts";
            else { message = Emoji.EXCLAMATION + "Please input data which matches the following pattern:\n\n" +
                        "Artist name \nyyyy-mm-dd \nPrice \nPlace \nLink to website";
            }
            log.info("Return message for user with chatId:{}", chatId);
        } catch (DataNotFoundException e) {
            log.error("User not found with chatId: {}. Cannot check role", chatId, e);
            message = "Sorry, only admin can add concerts";
        }

        return new SendMessage(chatId, message);
    }
}
