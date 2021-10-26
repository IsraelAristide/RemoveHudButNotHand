package ca.blutopia.removegui.gui;

import ca.blutopia.removegui.ModConfigHolder;
import ca.blutopia.removegui.removegui;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class configScreen extends Screen {

    private final removegui mod;

    private OptionsList optionlist;
    private Checkbox checkbox;
    private CycleButton<String> cycle;
    private Checkbox checkbox2;
    private Checkbox checkbox3;

    public configScreen(removegui main) {
        super(new TextComponent("Remove Gui but not hand settings"));
        mod = main;
    }

    @Override
    protected  void init() {

        this.checkbox3 = new Checkbox(120, 220, 20, 20, new TextComponent("Remove Hand"), ModConfigHolder.COMMON.removeHand.get(), true) {
            @Override
            public void onPress() {
                boolean togglemod = ModConfigHolder.COMMON.removeHand.get();

                if (togglemod) {
                    ModConfigHolder.COMMON.removeHand.set(false);
                } else {
                    ModConfigHolder.COMMON.removeHand.set(true);
                }

                super.onPress();
            }
        };

        this.checkbox2 = new Checkbox(120, 120, 20, 20, new TextComponent("Remove HUD but not Hand"), ModConfigHolder.COMMON.toggleMod.get(), true ) {
            @Override
            public void onPress() {
                boolean togglemod = ModConfigHolder.COMMON.toggleMod.get();

                if (togglemod) {
                    ModConfigHolder.COMMON.toggleMod.set(false);
                } else {
                    ModConfigHolder.COMMON.toggleMod.set(true);
                }

                super.onPress();
            }
        };

        this.checkbox = new Checkbox(120, 20, 20, 20, new TextComponent("Outline Blocks when looking at them"), ModConfigHolder.COMMON.highlightBlocks.get(), true) {

            @Override
            public void onPress() {

                boolean highlight = ModConfigHolder.COMMON.highlightBlocks.get();

                if (highlight) {
                    ModConfigHolder.COMMON.highlightBlocks.set(false);
                } else {
                    ModConfigHolder.COMMON.highlightBlocks.set(true);
                }


                super.onPress();
            }
        };

        this.optionlist = new OptionsList(
                Minecraft.getInstance(),
                this.width, this.height,
                24, this.height - 32,
                25
        );

        this.addWidget(this.optionlist);

        optionlist.addBig(new Option("Outline Blocks") {
            @Override
            public AbstractWidget createButton(Options p_91719_, int p_91720_, int p_91721_, int p_91722_) {
                return checkbox;
            }
        });

        optionlist.addBig(new Option("Remove HUD but not Hand") {
            @Override
            public AbstractWidget createButton(Options p_91719_, int p_91720_, int p_91721_, int p_91722_) {
                return checkbox2;
            }

        });

        optionlist.addBig(new Option("Remove Hand") {
            @Override
            public AbstractWidget createButton(Options p_91719_, int p_91720_, int p_91721_, int p_91722_) {
                return checkbox3;
            }
        });

        this.addRenderableWidget(new Button((this.width - 200)/2, this.height - 26, 200, 20, new TextComponent("Done"), button -> this.onClose()));

    }

    @Override
    public void render(PoseStack stack, int mousex, int mousey, float partialTicks) {
        this.renderBackground(stack);

        this.optionlist.render(stack, mousex, mousey, partialTicks);

        drawCenteredString(stack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);

        super.render(stack, mousex, mousey, partialTicks);

    }

}

