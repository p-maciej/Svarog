package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.font.Font;
import svarog.gui.font.TextBlock;
import svarog.language.LanguageLoader;
import svarog.render.Texture;

public class DialogContainer {
	private Group dialog;
	private Button dialogButton;
	
	private static Font dialogFont;
	private static Font answerFont;
	private static Font answerHoverFont;
	private static TextureObject dialogTop;
	private static BufferedImage dialogCenter;
	
	
	private int dialogXOffset;
	private int dialogYOffset;
	
	private static boolean isDialogClosing = false;
	
	private static Button closeButton;

	DialogContainer(int dialogXOffset, int dialogYOffset) {
		this.setDialogXOffset(dialogXOffset);
		this.setDialogYOffset(dialogYOffset);
		this.dialog = null;
	}
	
	Group createDialog(Dialog dialog, LanguageLoader language) {
		if(dialogTop != null && dialogCenter != null && answerFont != null && answerHoverFont != null && dialogFont != null) {
			Group group = new Group();
			
			int yOffset = 36;
			int interspace = 8;
			TextBlock content = new TextBlock(550, new Vector2f());
			content.setString(dialogFont, language.getValue(dialog.getContent()));

			int height = (int)content.getHeight()-(int)dialogTop.getHeight()+yOffset;
			if(height < 0)
				height = 0;
			
			int top = 0;
			int left = (int)-dialogTop.getWidth()/2+15;
			
			
			List<TextBlockButton> answers = new ArrayList<TextBlockButton>();
			
			for(int i = dialog.getAnswers().size()-1; i >= 0; i--) {
				Answer answer = dialog.getAnswers().get(i);
				
				TextBlockButton ans = new TextBlockButton(535, new Vector2f());
				ans.setString(answerFont, answerHoverFont, language.getValue(answer.getContent()));
				top -= ans.getHeight()+interspace;
				height += ans.getHeight()+interspace;
				ans.move(left+15, top);
				answer.setObjectId(ans.getId());
				answers.add(ans);
			}
	
			top -= content.getHeight()+interspace;
			
			if(height*dialogTop.getWidth()*4 > 0) {
				ByteBuffer center = BufferUtils.createByteBuffer(height*(int)dialogTop.getWidth()*4);
			
				for(int j = 0; j < dialogTop.getWidth(); j++) {
					for(int i = 0; i < height; i++) {
						int pixel = dialogCenter.getRGB(j, 0);
						center.put(((byte)((pixel >> 16) & 0xFF)));
						center.put(((byte)((pixel >> 8) & 0xFF)));
						center.put((byte)(pixel & 0xFF));
						center.put(((byte)((pixel >> 24) & 0xFF)));
					}
				}
				center.flip();
				
				TextureObject centerTexture = new TextureObject(new Texture(center, (int)dialogTop.getWidth(), height));	
				centerTexture.move(0, -height/2);
				group.addGuiObject(centerTexture);
			}
	
			content.move(left, top);
			
			dialogTop.setPosition(0, height+dialogTop.getHeight()/2);
			
			if(closeButton == null)
			 closeButton = new Button(new Texture("images/dialog/close_dialog.png"), new Vector2f(-left, -top+10));
			
			
			closeButton.setPosition(-left, -top+10);
			
			dialogButton = closeButton;
			
			
			group.addGuiObject(dialogTop);	
			
			
			for(TextBlockButton ans : answers)
				group.addGuiObject(ans);
			
			group.addGuiObject(content);
			group.addGuiObject(closeButton);
			
			this.dialog = group;
			group.setStickTo(stickTo.Bottom);
			group.move(dialogXOffset/2, dialogYOffset);
			
			return group;
		} else
			throw new IllegalStateException("Set all of required textures and fonts for dialog in renderer!");
	}
	
	int getDialogId() {
		if(dialog != null)
			return dialog.getId();
		else return -1;
	}
	
	void closeDialog(GuiRenderer renderer) {
		if(this.dialog != null) {
			renderer.removeGroup(this.dialog);
			this.dialog = null;
		}
	}
	
	public boolean isDialogOpen() {
		if(this.dialog !=  null)
			return true;
		else 
			return false;
	}
	
	 Button getDialogButton() {
		return dialogButton;
	}

	int getDialogXOffset() {
		return dialogXOffset;
	}

	void setDialogXOffset(int dialogXOffset) {
		this.dialogXOffset = dialogXOffset;
	}

	int getDialogYOffset() {
		return dialogYOffset;
	}

	void setDialogYOffset(int dialogYOffset) {
		this.dialogYOffset = dialogYOffset;
	}
	
	public static void setDialogFont(Font font) {
		dialogFont = font;
	}

	public static void setAnswerFont(Font font) {
		answerFont = font;
	}

	public static void setAnswerHoverFont(Font font) {
		answerHoverFont = font;
	}
	
	public static void setTopDialog(BufferedImage topDialog) {
		TextureObject tempDialogTop = new TextureObject(new Texture(topDialog));
		dialogTop = tempDialogTop;
	}
	
	public static void setCenterDialog(BufferedImage centerImage) {
		dialogCenter = centerImage;
	}
	
	public static boolean isDialogClosing() {
		return isDialogClosing;
	}

	public static void setDialogClosing(boolean state) {
		isDialogClosing = state;
	}
}
