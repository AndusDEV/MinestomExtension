package dev.andus.playerutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class Settings {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final File settingsFile = new File("playerutils" + File.separator + "config.json");
    private static SettingsState currentSettings = null;

    public static void read() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
            currentSettings = gson.fromJson(reader, SettingsState.class);
        } catch (FileNotFoundException e) {
            currentSettings = new SettingsState();
            try {
                File folder = new File("playerutils");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                write();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void write() throws IOException {
        String json = gson.toJson(currentSettings);
        Writer writer = new FileWriter(settingsFile);
        writer.write(json);
        writer.close();
    }

    private static class SettingsState {
        private final boolean MSG_ENABLED;
        private final boolean TPA_ENABLED;
        private final boolean TPAHERE_ENABLED;
        private final boolean JOIN_ENABLED;
        private final boolean LEAVE_ENABLED;

        private SettingsState() {
            this.MSG_ENABLED = true;
            this.TPA_ENABLED = true;
            this.TPAHERE_ENABLED = true;
            this.JOIN_ENABLED = true;
            this.LEAVE_ENABLED = true;
        }
    }

    public static boolean isMsgEnabled() {
        return currentSettings.MSG_ENABLED;
    }

    public static boolean isTpaEnabled() {
        return currentSettings.TPA_ENABLED;
    }

    public static boolean isTpaHereEnabled() {
        return currentSettings.TPAHERE_ENABLED;
    }

    public static boolean isJoinEventEnabled() {
        return currentSettings.JOIN_ENABLED;
    }

    public static boolean isLeaveEventEnabled() {
        return currentSettings.LEAVE_ENABLED;
    }
}