package xyz.hajsori.simplemacro.mixin;

import de.maxhenkel.voicechat.voice.client.PTTKeyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PTTKeyHandler.class)
public interface PTTKeyHandlerAccessor {
    @Accessor("pttKeyDown")
    void setPttKeyDown(boolean value);

    @Accessor("whisperKeyDown")
    void setWhisperKeyDown(boolean value);
}
