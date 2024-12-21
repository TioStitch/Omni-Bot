package org.tiostitch.omnibot.configuration;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.OmniLogger;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.types.BotConfiguration;
import org.tiostitch.omnibot.configuration.types.DatabaseConfiguration;
import org.tiostitch.omnibot.configuration.types.LanguageConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

@Getter @Setter
@RequiredArgsConstructor
public class Configuration {

    private final OmniCore omniCore;

    private BotConfiguration botConfiguration;
    private LanguageConfiguration languageConfiguration;
    private DatabaseConfiguration databaseConfiguration;

    private ArrayList<Item> loadedItems;

    public void init(boolean export) {

        final Gson gson = new Gson();

        if (export) {
            exportFiles();
        }

        botConfiguration = gson.fromJson(getJsonByFile("botConfiguration.json"), BotConfiguration.class);
        configureBot();

        languageConfiguration = gson.fromJson(getJsonByFile("langConfiguration.json"), LanguageConfiguration.class);
        databaseConfiguration = gson.fromJson(getJsonByFile("dataConfiguration.json"), DatabaseConfiguration.class);

        OmniLogger.success("Configuração carregada com sucesso!");

    }

    public void configureBot() {

        final JDA bot = omniCore.getBOT();
        final String message = botConfiguration.getACTIVITY_MESSAGE();
        final String streaming_url = botConfiguration.getSTREAMING_URL();

        switch (botConfiguration.getACTIVITY()) {
            case PLAYING:
                bot.getPresence().setActivity(Activity.playing(message));
                break;
            case WATCHING:
                bot.getPresence().setActivity(Activity.watching(message));
                break;
            case COMPETING:
                bot.getPresence().setActivity(Activity.competing(message));
                break;
            case LISTENING:
                bot.getPresence().setActivity(Activity.listening(message));
                break;
            case STREAMING:
                bot.getPresence().setActivity(Activity.streaming(message, streaming_url));
                break;
        }

        bot.getPresence().setStatus(botConfiguration.getONLINE_STATUS());
    }

    public void exportFiles() {

        final String PATH = "./";

        final String[] filesToExtract = {
                "botConfiguration.json",
                "dataConfiguration.json",
                "itemConfiguration.json",
                "langConfiguration.json"
        };

        for (String fileName : filesToExtract) {
            extractFileFromJar(PATH, "configurations/" + fileName, fileName);
        }

        OmniLogger.success("Arquivos inicializados com sucesso!");
    }

    private void extractFileFromJar(String targetDir, String resourcePath, String outputFileName) {
        try (InputStream inputStream = getClass().getResourceAsStream("/" + resourcePath)) {
            if (inputStream == null) {
                System.err.println("Recurso não encontrado: " + resourcePath);
                return;
            }

            File outputFile = new File(targetDir + outputFileName);

            // Cria o arquivo no diretório atual
            if (outputFile.createNewFile()) {
                System.out.println("Arquivo criado: " + outputFile.getAbsolutePath());
            }

            // Copia o conteúdo do recurso para o arquivo externo
            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("Arquivo " + outputFileName + " copiado com sucesso.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao extrair o arquivo " + outputFileName + ": " + e.getMessage());
        }
    }

    public String getJsonByFile(String fileName) {
        final File configFile = new File(fileName);

        try (Scanner scanner = new Scanner(configFile, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            return "";
        }

    }
}
