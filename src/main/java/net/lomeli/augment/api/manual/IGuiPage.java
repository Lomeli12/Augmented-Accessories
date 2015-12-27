package net.lomeli.augment.api.manual;

import net.minecraft.client.gui.GuiScreen;

public interface IGuiPage {
    GuiScreen openPage(String parentID);

    String getParentID();

    String getName();

    String getID();
}
