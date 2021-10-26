package ca.blutopia.removegui.gui;

import ca.blutopia.removegui.ModConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.util.text.TranslationTextComponent;

public final class configScreen extends Screen {

    private OptionsRowList optionsRowList;

    public configScreen() {
        super(new TranslationTextComponent("Remove HUD but not Hand Config"));
    }

    @Override
    protected void init() {
        this.optionsRowList = new OptionsRowList(
                Minecraft.getInstance(), this.width, this.height,
                24, this.height - 32,
                25
        );

        this.optionsRowList.addOption(new BooleanOption(
                "Highlight Blocks",
                unused -> ModConfigHolder.COMMON.highlightBlocks.get(),
                (unused, newValue) -> ModConfigHolder.COMMON.highlightBlocks.set(newValue)
        ));

        this.optionsRowList.addOption(new BooleanOption(
                "Remove Hud but not Hand",
                unused -> ModConfigHolder.COMMON.toggleMod.get(),
                (unused, newValue) -> ModConfigHolder.COMMON.toggleMod.set(newValue)
        ));

        this.optionsRowList.addOption(new BooleanOption(
                "Remove Hand but not Hud",
                unused -> ModConfigHolder.COMMON.removeHand.get(),
                (unused, newValue) -> ModConfigHolder.COMMON.removeHand.set(newValue)
        ));


        this.children.add(this.optionsRowList);

        this.addButton(new Button(
                (this.width = 200) / 2,
                this.height - 26,
                200, 20,
                I18n.format("gui.done"),
                button -> this.onClose()
        ));
    }

    @Override
    public void render(int mousex, int mousey, float partialTicks) {
        this.renderBackground();
        this.optionsRowList.render( mousex, mousey, partialTicks);
        drawCenteredString( this.font, this.title.getString(),
                this.width/2, 8, 0xFFFFFF);

        super.render( mousex, mousey, partialTicks);
    }
}