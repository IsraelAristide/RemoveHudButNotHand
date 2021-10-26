package ca.blutopia.removegui;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigHolder {
    public static class Common {
        private static final boolean highlightBlocksDefault = true;
        private static final boolean toggleModDefault = false;
        private static final boolean removeHandDefault = false;

        public final ConfigValue<Boolean> highlightBlocks;
        public final ConfigValue<Boolean> toggleMod;
        public final ConfigValue<Boolean> removeHand;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Config");
            this.highlightBlocks = builder.comment("Enable Block Highlihts").define("Highlight Blocks", highlightBlocksDefault);
            this.toggleMod = builder.comment("Remove GUI but not Hand").define("Enable Mod", toggleModDefault);
            this.removeHand = builder.comment("Remove Hand but not GUI").define("Remove Hand", removeHandDefault);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}
