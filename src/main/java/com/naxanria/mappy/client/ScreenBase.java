package com.naxanria.mappy.client;

import com.mojang.blaze3d.platform.GlStateManager;
//import com.sun.org.apache.xml.internal.security.utils.I18n;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.fml.ForgeI18n;
import org.lwjgl.opengl.GL11;

public class ScreenBase extends Screen
{
  public final Screen parent;
  
  protected ScreenBase(TextComponent title)
  {
    this(title, null);
  }
  
  public ScreenBase(TextComponent title, Screen parent)
  {
    super(title);
    this.parent = parent;
  }
  
  @Override
  public void render(int int_1, int int_2, float float_1)
  {
    renderBackground();
    for (IGuiEventListener listener :
      children)
    {
      if (listener instanceof IRenderable)
      {
        ((IRenderable) listener).render(int_1, int_2, float_1);
      }
    }
    renderForeground();
  }
  
  public void renderForeground()
  {
  }
  
  @Override
  public void onClose()
  {
    minecraft.displayGuiScreen(parent);
  }
  
  public void renderTexture(int x, int y, int width, int height, String id)
  {
    renderTexture(x, y, width, height, new ResourceLocation(id));
  }
  
  public void renderTexture(int x, int y, int width, int height, ResourceLocation id)
  {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder builder = tessellator.getBuffer();
    minecraft.getTextureManager().bindTexture(id);
    GlStateManager.color4f(1, 1, 1, 1);
    builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    
    builder.pos(x, y + height, 0).tex(0f, 1f).color(255, 255, 255, 255).endVertex();
    builder.pos(x + width, y + height, 0).tex(1f, 1f).color(255, 255, 255, 255).endVertex();
    builder.pos(x + width, y, 0).tex(1f, 0f).color(255, 255, 255, 255).endVertex();
    builder.pos(x, y, 0).tex(0f, 0f).color(255, 255, 255, 255).endVertex();
    
    tessellator.draw();
  }
  
  public void renderTexture(int x, int y, int width, int height, float u, float v, int r, int g, int b, int a, ResourceLocation id)
  {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder builder = tessellator.getBuffer();
    minecraft.getTextureManager().bindTexture(id);
    GlStateManager.color4f(1, 1, 1, 1);
    builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
  
    builder.pos(x, y + height, 0).tex(0f, v).color(r, g, b, a).endVertex();
    builder.pos(x + width, y + height, 0).tex(u, v).color(r, g, b, a).endVertex();
    builder.pos(x + width, y, 0).tex(u, 0f).color(r, g, b, a).endVertex();
    builder.pos(x, y, 0).tex(0f, 0f).color(r, g, b, a).endVertex();
  
    tessellator.draw();
  }
  
  public void renderTextureModal(int x, int y, int width, int height, int textureWidth, int textureHeight, ResourceLocation id)
  {
    renderTexture(x, y, width, height, (float) width / (float) textureWidth, (float) height / (float) textureHeight, 255, 255, 255, 255, id);
  }
  
  public void renderTextureRepeating(int x, int y, int width, int height, int textureHeight, int textureWidth, String id)
  {
    renderTextureRepeating(x, y, width, height, textureHeight, textureWidth, new ResourceLocation(id));
  }
  
  public void renderTextureRepeating(int x, int y, int width, int height, int textureHeight, int textureWidth, ResourceLocation id)
  {
    for (int xp = 0; xp < width; xp += textureWidth)
    {
      int w = (xp + textureWidth < width) ? textureWidth : width - xp;
      for (int yp = 0; yp < height; yp += textureHeight)
      {
        int h = (yp + textureHeight < height) ? textureHeight : height - yp;
        renderTextureModal(x + xp, y + yp, w, h, textureWidth, textureHeight, id);
      }
    }
  }
  
  public String lang(String key)
  {
    return I18n.format("mappy.gui." + key); //I18n.translate("mappy.gui." + key);
  }
  
}
