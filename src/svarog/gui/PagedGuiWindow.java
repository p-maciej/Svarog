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
	
	public PagedGuiWindow(String title, Font font, TextureObject backgroundTexture) {
		super(title, font, backgroundTexture);
		textBlocks = new ArrayList<TextBlock>();
		toRender = new ArrayList<TextBlock>();
		
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
		int posLeft = -super.getWidth()/2+20;
		int posRight = super.getWidth()/2-20;
		int top = super.getHeight()/2-45;
		
		Button left = new Button(new Texture("images/window/left.png"), new Vector2f());
		Button right = new Button(new Texture("images/window/right.png"), new Vector2f());
		
		left.setPosition(posLeft, top);
		right.setPosition(posRight, top);
		
		this.pageLeft = left;
		this.pageRight = right;
		
		super.addTextureObject(left);
		super.addTextureObject(right);
		
		Line pageLine = new Line(0, super.getHeight()/2-45);
		super.addTextureObject(pageLine);
		this.pageLine = pageLine;
		update();
	}
	
	private void setBlockPositions() {
		int left = -super.getWidth()/2+10;
		int top = super.getHeight()/2-70;
		for(TextBlock block : toRender) {
			block.setPosition(left, top);
			top -= block.getHeight();
		}
	}
	
	private void setRenderBlocks() {
		toRender.clear();
		int height = 0;
		int lastRenderedIndex = -1;
		int tempHeight = 0;
		int tempPage = 1;
		maxPage = false;

		if(currentPage > 1) {
			for(int i = 0; i < textBlocks.size(); i++) {
				tempHeight += textBlocks.get(i).getHeight();
				if(tempHeight > super.getHeight()) {
					tempPage++;
					tempHeight = 0;
					if(tempPage == currentPage) {
						lastRenderedIndex  = i;
						break;
					}
				}
			}
			for(int i = lastRenderedIndex; i < textBlocks.size(); i++) {
				height += textBlocks.get(i).getHeight();
				if(height < super.getHeight()) {
					toRender.add(textBlocks.get(i));
					lastRenderedIndex = i;
					if(i+1 == textBlocks.size())
						maxPage = true;
				} else {
					break;
				}
			}
		} else if(currentPage == 1) {
			for(int i = 0; i < textBlocks.size(); i++) {
				height += textBlocks.get(i).getHeight();
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
}
