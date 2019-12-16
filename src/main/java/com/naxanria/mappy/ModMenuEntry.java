package com.naxanria.mappy;

import com.naxanria.mappy.client.DrawPosition;
import com.naxanria.mappy.config.Config;
import com.naxanria.mappy.config.Settings;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;

import java.util.function.Function;

public class ModMenuEntry implements ModMenuApi
{
  private static final String RESET = "text.cloth.reset_value";

  @Override
  public String getModId()
  {
    return Mappy.MODID;
  }

  private Screen getScreen(Screen parent)
  {
    ConfigBuilder builder = ConfigBuilder.create();
    builder.setParentScreen(parent);
    builder.setTitle("Mappy Config");
    builder.setSavingRunnable(this::saveConfig);

    ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
    entryBuilder.setResetButtonKey(RESET);

    ConfigCategory general = builder.getOrCreateCategory(lang("category.general"));
    general.addEntry(entryBuilder.startIntField(lang("offset"), Settings.offset).setDefaultValue(() -> Settings.offset)
      .setSaveConsumer((i) -> Settings.offset = i).build());
    EnumSelectorBuilder<DrawPosition> drawPositionEntry =
      entryBuilder.startEnumSelector(lang("draw_position"), DrawPosition.class, Settings.drawPosition);
    drawPositionEntry.setDefaultValue(() -> Settings.drawPosition);
    drawPositionEntry.setSaveConsumer((p) -> Settings.drawPosition = p);
    general.addEntry(drawPositionEntry.build());
    general.addEntry(
      entryBuilder.startIntField(lang("map_size"), Settings.mapSize).setDefaultValue(() -> Settings.mapSize)
        .setSaveConsumer((i) -> Settings.mapSize = i).setMin(32).setMax(1024).build());
//    general.addOption(new IntegerSliderEntry(lang("map_scale"), 1, 8, Settings.scale, RESET, () -> Settings.scale, (i) -> Settings.scale = i));
    general.addEntry(entryBuilder.startBooleanToggle(lang("map_move"), Settings.moveMapForEffects)
      .setDefaultValue(() -> Settings.moveMapForEffects).setSaveConsumer((b) -> Settings.moveMapForEffects = b)
      .build());
    general.addEntry(entryBuilder.startBooleanToggle(lang("show_in_chat"), Settings.showInChat)
      .setDefaultValue(() -> Settings.showInChat).setSaveConsumer((b) -> Settings.showInChat = b).build());

    general.addEntry(
      entryBuilder.startBooleanToggle(lang("shaded"), Settings.shaded).setDefaultValue(() -> Settings.shaded)
        .setSaveConsumer((b) -> Settings.shaded = b).build());
    general.addEntry(entryBuilder.startIntSlider(lang("shade_strength"), 18 - Settings.maxDifference, 0, 16)
      .setDefaultValue(() -> 18 - Settings.maxDifference).setSaveConsumer((i) -> Settings.maxDifference = 18 - i)
      .build());


    ConfigCategory mapInfo = builder.getOrCreateCategory(lang("category.info"));
    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_grid"), Settings.drawChunkGrid)
      .setDefaultValue(() -> Settings.drawChunkGrid).setSaveConsumer((b) -> Settings.drawChunkGrid = b).build());
    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_position"), Settings.showPosition)
      .setDefaultValue(() -> Settings.showPosition).setSaveConsumer((b) -> Settings.showPosition = b).build());
    mapInfo.addEntry(
      entryBuilder.startBooleanToggle(lang("show_biome"), Settings.showBiome).setDefaultValue(() -> Settings.showBiome)
        .setSaveConsumer((b) -> Settings.showBiome = b).build());
    mapInfo.addEntry(
      entryBuilder.startBooleanToggle(lang("show_fps"), Settings.showFPS).setDefaultValue(() -> Settings.showFPS)
        .setSaveConsumer((b) -> Settings.showFPS = b).build());
    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_game_time"), Settings.showTime)
      .setDefaultValue(() -> Settings.showTime).setSaveConsumer((b) -> Settings.showTime = b).build());
    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_direction"), Settings.showDirection)
      .setDefaultValue(() -> Settings.showDirection).setSaveConsumer((b) -> Settings.showDirection = b).build());

    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_player_names"), Settings.showPlayerNames)
      .setDefaultValue(() -> Settings.showPlayerNames).setSaveConsumer((b) -> Settings.showPlayerNames = b).build());
    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_player_heads"), Settings.showPlayerHeads)
      .setDefaultValue(() -> Settings.showPlayerHeads).setSaveConsumer((b) -> Settings.showPlayerHeads = b).build());
    mapInfo.addEntry(entryBuilder.startBooleanToggle(lang("show_entities"), Settings.showEntities)
      .setDefaultValue(() -> Settings.showEntities).setSaveConsumer((b) -> Settings.showEntities = b).build());

    ConfigCategory optimization = builder.getOrCreateCategory(lang("category.optimization"));
    optimization.addEntry(entryBuilder.startIntField(lang("update_cycle"), Settings.updatePerCycle)
      .setDefaultValue(() -> Settings.updatePerCycle).setSaveConsumer((i) -> Settings.updatePerCycle = i).setMin(1)
      .setMax(1000).build());
    optimization.addEntry(
      entryBuilder.startIntField(lang("prune_delay"), Settings.pruneDelay).setDefaultValue(() -> Settings.pruneDelay)
        .setSaveConsumer((i) -> Settings.pruneDelay = i).setMin(10).setMax(600).build());
    optimization.addEntry(
      entryBuilder.startIntField(lang("prune_amount"), Settings.pruneAmount).setDefaultValue(() -> Settings.pruneAmount)
        .setSaveConsumer((i) -> Settings.pruneAmount = i).setMin(100).setMax(5000).build());

    if (Settings.showItemConfigInGame)
    {
      ConfigCategory items = builder.getOrCreateCategory(lang("category.items"));
      items.addEntry(entryBuilder.startTextDescription(lang("item_description")).build());
      items.addEntry(entryBuilder.startBooleanToggle(lang("item_in_hotbar"), Settings.inHotBar)
        .setDefaultValue(() -> Settings.inHotBar).setSaveConsumer((b) -> Settings.inHotBar = b).build());
      items.addEntry(
        entryBuilder.startTextField(lang("item_show_map"), Settings.mapItem).setDefaultValue(() -> Settings.mapItem)
          .setSaveConsumer((s) -> Settings.mapItem = s).build());
      items.addEntry(entryBuilder.startTextField(lang("item_show_position"), Settings.positionItem)
        .setDefaultValue(() -> Settings.positionItem).setSaveConsumer((s) -> Settings.positionItem = s).build());
      items.addEntry(entryBuilder.startTextField(lang("item_show_biome"), Settings.biomeItem)
        .setDefaultValue(() -> Settings.biomeItem).setSaveConsumer((s) -> Settings.biomeItem = s).build());
      items.addEntry(
        entryBuilder.startTextField(lang("item_show_time"), Settings.timeItem).setDefaultValue(() -> Settings.timeItem)
          .setSaveConsumer((s) -> Settings.timeItem = s).build());
    }
    builder.setDoesConfirmSave(false);

    return builder.build();
  }

  private String lang(String key)
  {
    return I18n.translate("mappy.config." + key);
  }


  private void saveConfig()
  {
    Config.instance.save();
    System.out.println("Saved config");
    Mappy.map.onConfigChanged();
  }

  @Override
  public Function<Screen, ? extends Screen> getConfigScreenFactory()
  {
    return (this::getScreen);
  }
}
