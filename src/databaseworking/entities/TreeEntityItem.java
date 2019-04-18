/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking.entities;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Username
 */
public class TreeEntityItem extends TreeItem {
	private String name;

	public TreeEntityItem(String name, Node grafic) {
		super(name, grafic);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
