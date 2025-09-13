package xyz.hajsori.simplemacro.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.VoiceChatScreen;
import de.maxhenkel.voicechat.gui.VoiceChatSettingsScreen;
import de.maxhenkel.voicechat.gui.group.GroupScreen;
import de.maxhenkel.voicechat.gui.group.JoinGroupScreen;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
import de.maxhenkel.voicechat.voice.client.*;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.java_websocket.WebSocket;
import xyz.hajsori.simplemacro.mixin.PTTKeyHandlerAccessor;

public class ActionManager {
    public ActionManager(String message, WebSocket ws) {
        ClientVoicechat client = ClientManager.getClient();
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerStateManager playerStates = ClientManager.getPlayerStateManager();
        JsonObject data = new Gson().fromJson(message, JsonObject.class);
        String action = data.get("action").getAsString();
        String target = data.get("target").getAsString();

        switch (action) {
            case "toggle" -> {
                switch (target) {
                    case "microphone":
                        boolean muted = !playerStates.isMuted();
                        playerStates.setMuted(muted);
                        ws.send("{\"isMuted\":" + muted + "}");
                        break;
                    case "voiceChat":
                        boolean disabled = !playerStates.isDisabled();
                        playerStates.setDisabled(disabled);
                        ws.send("{\"isDisabled\":" + disabled + "}");
                        break;
                    case "icons":
                        boolean hidden = !VoicechatClient.CLIENT_CONFIG.hideIcons.get();
                        VoicechatClient.CLIENT_CONFIG.hideIcons.set(hidden);
                        ws.send("{\"isHidden\":" + hidden + "}");
                        break;
                    case "recording":
                        if (client != null) {
                            client.toggleRecording();
                            ws.send("{\"isRecording\":" + (client.getRecorder() != null) + "}");
                        }
                        break;
                }
            }
            case "setScreen" -> {
                switch (target) {
                    case "adjustVolumes":
                        minecraft.execute(() -> minecraft.setScreen(new AdjustVolumesScreen()));
                        break;
                    case "groupManagement":
                        if (client != null && client.getConnection() != null && client.getConnection().getData().groupsEnabled()) {
                            ClientGroup group = playerStates.getGroup();
                            minecraft.execute(() -> {
                                if (group != null) {
                                    minecraft.setScreen(new GroupScreen(group));
                                } else {
                                    minecraft.setScreen(new JoinGroupScreen());
                                }
                            });
                        } else {
                            LocalPlayer player = minecraft.player;
                            if (player != null) {
                                player.sendSystemMessage(Component.translatable("message.voicechat.groups_disabled"));
                            }
                        }
                        break;
                    case "voiceChat":
                        minecraft.execute(() -> minecraft.setScreen(new VoiceChatScreen()));
                        break;
                    case "settings":
                        minecraft.execute(() -> minecraft.setScreen(new VoiceChatSettingsScreen()));
                        break;
                }
            }
            case "activate" -> {
                switch (target) {
                    case "pushToTalk":
                        ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setPttKeyDown(true);
                        break;
                    case "whisper":
                        ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setWhisperKeyDown(true);
                        break;
                }
            }
            case "deactivate" -> {
                switch (target) {
                    case "pushToTalk":
                        ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setPttKeyDown(false);
                        break;
                    case "whisper":
                        ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setWhisperKeyDown(false);
                        break;
                }
            }
        }
/*
        switch (action) {
            case "toggleMicrophone":
                boolean muted = !playerStates.isMuted();
                playerStates.setMuted(muted);
                ws.send("{\"isMuted\":" + muted + "}");
                break;
            case "toggleVoicechat":
                boolean disabled = !playerStates.isDisabled();
                playerStates.setDisabled(disabled);
                ws.send("{\"isDisabled\":" + disabled + "}");
                break;
            case "toggleIcons":
                boolean hidden = !VoicechatClient.CLIENT_CONFIG.hideIcons.get();
                VoicechatClient.CLIENT_CONFIG.hideIcons.set(hidden);
                ws.send("{\"isHidden\":" + hidden + "}");
                break;
            case "toggleRecording":
                if (client != null) {
                    client.toggleRecording();
                    ws.send("{\"isRecording\":" + (client.getRecorder() != null) + "}");
                }
                break;
            case "adjustVolumes":
                minecraft.execute(() -> {
                    minecraft.setScreen(new AdjustVolumesScreen());
                });
                break;
            case "groupManagement":
                if (client != null && client.getConnection() != null && client.getConnection().getData().groupsEnabled()) {
                    ClientGroup group = playerStates.getGroup();
                    minecraft.execute(() -> {
                        if (group != null) {
                            minecraft.setScreen(new GroupScreen(group));
                        } else {
                            minecraft.setScreen(new JoinGroupScreen());
                        }
                    });
                } else {
                    LocalPlayer player = minecraft.player;
                    if (player != null) {
                        player.sendSystemMessage(Component.translatable("message.voicechat.groups_disabled"));
                    }
                }
                break;
            case "voicechatMenu":
                minecraft.execute(() -> {
                    minecraft.setScreen(new VoiceChatScreen());
                });
                break;
            case "settingsMenu":
                minecraft.execute(() -> {
                    minecraft.setScreen(new VoiceChatSettingsScreen());
                });
                break;
            case "pushToTalkActive":
                ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setPttKeyDown(true);
                break;
            case "pushToTalkInactive":
                ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setPttKeyDown(false);
                break;
            case "whisperActive":
                ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setWhisperKeyDown(true);
                break;
            case "whisperInactive":
                ((PTTKeyHandlerAccessor) ClientManager.getPttKeyHandler()).setWhisperKeyDown(false);
                break;
        }*/
    }
}
