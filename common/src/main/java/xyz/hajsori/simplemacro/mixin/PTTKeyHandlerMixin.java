package xyz.hajsori.simplemacro.mixin;

import de.maxhenkel.voicechat.voice.client.PTTKeyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.hajsori.simplemacro.config.ConsumeKeys;

@Mixin(PTTKeyHandler.class)
public class PTTKeyHandlerMixin {
    @Shadow private boolean pttKeyDown;
    @Shadow private boolean whisperKeyDown;

    /**
     * @author Hajsori
     * @reason For Simple Macro
     */
    @Overwrite
    public boolean isPTTDown() {
        return pttKeyDown || ConsumeKeys.pttKeyDown;
    }

    /**
     * @author Hajsori
     * @reason For Simple Macro
     */
    @Overwrite
    public boolean isWhisperDown() {
        return whisperKeyDown || ConsumeKeys.whisperKeyDown;
    }

    /**
     * @author Hajsori
     * @reason For Simple Macro
     */
    @Overwrite
    public boolean isAnyDown() {
        return pttKeyDown || whisperKeyDown || ConsumeKeys.pttKeyDown || ConsumeKeys.whisperKeyDown;
    }
}
