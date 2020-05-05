package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.render.Texture;

public class PagedGuiWindow extends GuiWindow {
	public static enum Type {
		headline,
		content,
		normal
	}
	
	private List<WindowTextType> textBlocks;
	private List<WindowTextType> toRender;
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
	private int textBlockIndent;
	
	public PagedGuiWindow(String title, Font font, TextureObject backgroundTexture) {
		super(title, font, backgroundTexture);
		textBlocks = new ArrayList<WindowTextType>();
		toRender = new ArrayList<WindowTextType>();
		
		this.interfacePaddingLeft = 20;
		this.interfacePaddingRight = 20;
		this.interfacePaddingTop = 45;
		this.textBlockSpacing = 20;
		this.textBlockPaddingLeft = 10;
		this.textBlockPaddingTop = 70;
		this.textBlockIndent = 20;
		
		this.currentPage = 1;
		addPagesElements();
	}
	
	public PagedGuiWindow(String title, Font font, TextureObject backgroundTexture, int interfacePaddingLeft, int interfacePaddingRight, int interfacePaddingTop, int textBlockSpacing, int textBlockPaddingLeft, int textBlockPaddingTop) {
		super(title, font, backgroundTexture);
		textBlocks = new ArrayList<WindowTextType>();
		toRender = new ArrayList<WindowTextType>();
		
		this.setInterfacePaddingLeft(interfacePaddingLeft);
		this.setInterfacePaddingRight(interfacePaddingRight);
		this.setInterfacePaddingTop(interfacePaddingTop);
		this.setTextBlockPaddingLeft(textBlockPaddingLeft);
		this.setTextBlockSpacing(textBlockSpacing);
		this.setTextBlockPaddingTop(textBlockPaddingTop);
		
		this.currentPage = 1;
		addPagesElements();
	}
	

	public void addTextBlock(TextBlock block, Type type) {
		textBlocks.add(new WindowTextType(block, type));
	}
	
	List<WindowTextType> getTextBlocks() {
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
		for(WindowTextType block : toRender) {
			int tempLeft = left;
			if(block.getType() == Type.content) {
				tempLeft += textBlockIndent;
			}
			
			block.getBlock().setPosition(tempLeft, top);
			if(block.getType() == Type.content || block.getType() == Type.normal)
				top -= block.getBlock().getHeight() + textBlockSpacing;
			else
				top -= block.getBlock().getHeight();
			
		}
	}
	
	private void setRenderBlocks() {
		toRender.clear();
		int height = textBlockPaddingTop;
		int lastRenderedIndex = 0;
		int tempHeight = textBlockPaddingTop;
		int tempPage = 1;
		
		int headlineRendered = -1;
		maxPage = false;

		if(currentPage > 1) {
			for(int i = 0; i < textBlocks.size(); i++) {
				WindowTextType temp = textBlocks.get(i);
				if(temp.getType() == Type.headline) {
					tempHeight += temp.getBlock().getHeight() + textBlockSpacing;
					headlineRendered = i;
				} else if( temp.getType() == Type.normal) {
					tempHeight += temp.getBlock().getHeight() + textBlockSpacing;
				} else {
					tempHeight += temp.getBlock().getHeight();
				}
				
				if(tempHeight >= super.getHeight()) {
					++tempPage;
					tempHeight = textBlockPaddingTop;
					if(headlineRendered != i-1)
						i = i-1;
					else 
						i = i-2;
					
					if(tempPage == currentPage) {
						lastRenderedIndex = i;
						break;
					}
				}
				
				if(i-1 == headlineRendered)
					headlineRendered = -1;
			}
			for(int i = lastRenderedIndex+1; i < textBlocks.size(); i++) {
				WindowTextType temp = textBlocks.get(i);
				if(temp.getType() == Type.headline) {
					height += temp.getBlock().getHeight() + textBlockSpacing;
					headlineRendered = i;
				} else if(temp.getType() == Type.normal) {
					height += temp.getBlock().getHeight() + textBlockSpacing;
				} else {
					height += temp.getBlock().getHeight();
				}
				
				if(height < super.getHeight()) {
					toRender.add(temp);
					if(i+1 == textBlocks.size())
						maxPage = true;
				} else {
					if(i-1 == headlineRendered)
						toRender.remove(toRender.size()-1);
					break;
				}
				
				if(i-1 == headlineRendered)
					headlineRendered = -1;
			}
		} else if(currentPage == 1) {
			for(int i = 0; i < textBlocks.size(); i++) {
				WindowTextType temp = textBlocks.get(i);
				if(temp.getType() == Type.headline) {
					height += temp.getBlock().getHeight() + textBlockSpacing;
					headlineRendered = i;
				} else if(temp.getType() == Type.normal) {
					height += temp.getBlock().getHeight() + textBlockSpacing;
				} else
					height += temp.getBlock().getHeight();
				
				if(height < super.getHeight()) {
					toRender.add(temp);
					lastRenderedIndex = i;
					if(i+1 == textBlocks.size())
						maxPage = true;
				} else {
					if(i-1 == headlineRendered)
						toRender.remove(toRender.size()-1);
					break;
				}
				
				if(i-1 == headlineRendered)
					headlineRendered = -1;
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
	
	
	class WindowTextType {
		private Type type;
		private TextBlock block;

		private WindowTextType(TextBlock block, Type type) {
			this.setType(type);
			this.setBlock(block);
		}		
		
		TextBlock getBlock() {
			return block;
		}

		private void setBlock(TextBlock block) {
			this.block = block;
		}

		private Type getType() {
			return type;
		}

		private void setType(Type type) {
			this.type = type;
		}
	}
}
