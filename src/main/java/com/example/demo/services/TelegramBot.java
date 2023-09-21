package com.example.demo.services;

import com.example.demo.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    public TelegramBot(BotConfig botConfig){
        this.botConfig = botConfig;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "start the bot"));
        listOfCommands.add(new BotCommand("/info", "give you some info about this bot"));
        listOfCommands.add(new BotCommand("/jokes", "bot start to give you jokes"));
        listOfCommands.add(new BotCommand("/stopjokes", "bot stop to give you jokes"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if(messageText.contains("/jokes")){

            }
            switch (messageText) {
                case "/start":
                    startCommandReceive(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/info":
                    sendMessage(chatId, "Цей бот буде надсилати тобі кожний день рандомний жарт! Напиши команду /jokes, щоб воно почало працювати");
                    break;
                case "/jokes":

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

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
