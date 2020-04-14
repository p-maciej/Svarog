package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.render.Texture;

public class PagedGuiWindow extends GuiWindow {
	
	private List<TextBlock> textBlocks;
	private List<TextBlock> toRender;
	private int currentPage;
	
	
	private Button pageLeft;
	private Button pageRight;
	
	private Line pageLine;
	
	private boolean maxPage;
	
	
	private int interfacePaddingLeft;
	private int interfacePaddingRight;
	private int interfacePaddingTop;
	
	private int textBlockSpacing;
	private int textBlockPaddingLeft;
	private int textBlockPaddingTop;
	
	public PagedGuiWindow(String title, Font font, TextureObject backgroundTexture) {
		super(title, font, backgroundTexture);
		textBlocks = new ArrayList<TextBlock>();
		toRender = new ArrayList<TextBlock>();
		
		this.interfacePaddingLeft = 20;
		this.interfacePaddingRight = 20;
		this.interfacePaddingTop = 45;
		this.textBlockSpacing = 20;
		this.textBlockPaddingLeft = 10;
		this.textBlockPaddingTop = 70;
		
		this.currentPage = 1;
		addPagesElements();
	}
	
	public PagedGuiWindow(String title, Font font, TextureObject backgroundTexture, int interfacePaddingLeft, int interfacePaddingRight, int interfacePaddingTop, int textBlockSpacing, int textBlockPaddingLeft, int textBlockPaddingTop) {
		super(title, font, backgroundTexture);
		textBlocks = new ArrayList<TextBlock>();
		toRender = new ArrayList<TextBlock>();
		
		this.setInterfacePaddingLeft(interfacePaddingLeft);
		this.setInterfacePaddingRight(interfacePaddingRight);
		this.setInterfacePaddingTop(interfacePaddingTop);
		this.setTextBlockPaddingLeft(textBlockPaddingLeft);
		this.setTextBlockSpacing(textBlockSpacing);
		this.setTextBlockPaddingTop(textBlockPaddingTop);
		
		this.currentPage = 1;
		addPagesElements();
	}
	
	@Override
	public void addTextBlock(TextBlock block) {
		textBlocks.add(block);
	}
	
	List<TextBlock> getTextBlocks() {
		return toRender;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	private void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		setRenderBlocks();
		setBlockPositions();
	}	
	
	public void setPageContent() {
		setCurrentPage(1);
	}
	
	private void addPagesElements() {
		int posLeft = -super.getWidth()/2+interfacePaddingLeft;
		int posRight = super.getWidth()/2-interfacePaddingRight;
		int top = super.getHeight()/2-interfacePaddingTop;
		
		Button left = new Button(new Texture("images/window/left.png"), new Vector2f());
		Button right = new Button(new Texture("images/window/right.png"), new Vector2f());
		
		left.setPosition(posLeft, top);
		right.setPosition(posRight, top);
		
		this.pageLeft = left;
		this.pageRight = right;
		
		super.addTextureObject(left);
		super.addTextureObject(right);
		
		Line pageLine = new Line(0, super.getHeight()/2-interfacePaddingTop);
		super.addTextureObject(pageLine);
		this.pageLine = pageLine;
		update();
	}
	
	private void setBlockPositions() {
		int left = -super.getWidth()/2+textBlockPaddingLeft;
		int top = super.getHeight()/2-textBlockPaddingTop;
		for(TextBlock block : toRender) {
			block.setPosition(left, top);
			top -= block.getHeight() + textBlockSpacing;
		}
	}
	
	private void setRenderBlocks() {
		toRender.clear();
		int height = textBlockPaddingTop;
		int lastRenderedIndex = 0;
		int tempHeight = 0;
		int tempPage = 1;
		maxPage = false;

		if(currentPage > 1) {
			for(int i = 0; i < textBlocks.size(); i++) {
				tempHeight += textBlocks.get(i).getHeight() + textBlockSpacing;
				if(tempHeight > super.getHeight()) {
					tempPage++;
					tempHeight = 0;
					if(tempPage == currentPage) {
						lastRenderedIndex = i-1;
						break;
					}
				}
			}
			for(int i = lastRenderedIndex; i < textBlocks.size(); i++) {
				height += textBlocks.get(i).getHeight() + textBlockSpacing;
				if(height < super.getHeight()) {
					toRender.add(textBlocks.get(i));
					if(i+1 == textBlocks.size())
						maxPage = true;
				} else {
					break;
				}
			}
		} else if(currentPage == 1) {
			for(int i = 0; i < textBlocks.size(); i++) {
				height += textBlocks.get(i).getHeight() + textBlockSpacing;
				if(height < super.getHeight()) {
					toRender.add(textBlocks.get(i));
					lastRenderedIndex = i;
				} else {
					break;
				}
			}
		}
	}

	public Button getPageRight() {
		return pageRight;
	}

	public Button getPageLeft() {
		return pageLeft;
	} 
	
	void previousPage() {
		if(currentPage > 1) {
			this.currentPage--;
			this.setCurrentPage(currentPage);
			update();
		}
	}
	
	void nextPage() {
		if(!maxPage) {
			this.currentPage++;
			this.setCurrentPage(currentPage);
			update();
		}
	}
	
	void update() {
		pageLine.setString(Integer.toString(currentPage), super.windowFont);
	}

	public int getInterfacePaddingLeft() {
		return interfacePaddingLeft;
	}

	public void setInterfacePaddingLeft(int interfacePaddingLeft) {
		this.interfacePaddingLeft = interfacePaddingLeft;
	}
	
	public int getInterfacePaddingRight() {
		return interfacePaddingRight;
	}

	public void setInterfacePaddingRight(int interfacePaddingRight) {
		this.interfacePaddingRight = interfacePaddingRight;
	}

	public int getInterfacePaddingTop() {
		return interfacePaddingTop;
	}

	public void setInterfacePaddingTop(int interfacePaddingTop) {
		this.interfacePaddingTop = interfacePaddingTop;
	}

	public int getTextBlockSpacing() {
		return textBlockSpacing;
	}

	public void setTextBlockSpacing(int textBlockSpacing) {
		this.textBlockSpacing = textBlockSpacing;
	}

	public void setTextBlocks(List<TextBlock> textBlocks) {
		this.textBlocks = textBlocks;
	}

	public int getTextBlockPaddingLeft() {
		return textBlockPaddingLeft;
	}

	public void setTextBlockPaddingLeft(int textBlockPaddingLeft) {
		this.textBlockPaddingLeft = textBlockPaddingLeft;
	}

	public int getTextBlockPaddingTop() {
		return textBlockPaddingTop;
	}

	public void setTextBlockPaddingTop(int textBlockPaddingTop) {
		this.textBlockPaddingTop = textBlockPaddingTop;
	}
}
