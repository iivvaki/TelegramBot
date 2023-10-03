package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.services.JokeService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final UserService userService;
    private final JokeService jokeService;
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            Optional<User> user = userService.readUsers(chatId);

            switch (messageText) {
                case "/start":
                    if (user.isEmpty())
                        userService.addUser(new User(chatId, update.getMessage().getChat().getFirstName(), true));
                    startCommandReceive(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/info":
                    sendMessage(chatId, "Цей бот буде надсилати тобі кожний день рандомний жарт! Напиши команду /jokes, щоб воно почало працювати");
                    break;
                case "/jokes":
                    sendDailyMessage();
                    break;
                case "/stopjokes":
                    sendMessage(chatId, "Ви зупинили надсилання жартів.");
                    userService.setStatus(chatId, false);
                    break;
                default:
                    sendMessage(chatId, "Fuck u");
                    break;
            }
        }
    }

    private void startCommandReceive(long chatId, String name){
        String answer = "Hi " + name +", nice to meet you";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 46 23 * * *")
    public void sendDailyMessage(){

        List<User> users = userService.findAllEnable();

        for(User user : users){
            if (user.isEnable()){
                try {
                    sendMessage(user.getId(), jokeService.getJokes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }


}
