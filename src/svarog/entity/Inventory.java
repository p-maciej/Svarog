package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import svarog.objects.Item;

public class Inventory {

	private List<Item> items = new ArrayList<>();
	
	public Inventory(List<Item>items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void removeItemById(int id) {
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getId() == id) {
				items.remove(i);
			}
		}
	}
}
