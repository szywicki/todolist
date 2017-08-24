package com.libertymutual.goforcode.todolist.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.libertymutual.goforcode.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {
	private ArrayList<ToDoItem> items;
	private int nextId = 1;
	
    /**
     * Assigns a new id to the ToDoItem and saves it to the file.
     * @param item The to-do item to save to the file.
     */
    public void create(ToDoItem item) {
    	if (items.size() > 0) {
    		nextId = items.get(items.size() - 1).getId() +1;
    		
    	} else
    		nextId = 1;
    	
    	item.setId(nextId);
               
		try (FileWriter writer = new FileWriter("todolist.csv", true);
			CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {
			String[] record = {Integer.toString(item.getId()), item.getText(), Boolean.toString(item.isComplete())};
			printer.printRecord(record);
			
		} catch (IOException e) {
			System.out.println("writing error");
		}
    }
    
    
    /**
     * Get all the items from the file. 
     * @return A list of the items. If no items exist, returns an empty list.
     */

    
    public List<ToDoItem> getAll() {
    	
    	
		try (FileReader reader = new FileReader("todolist.csv");
			CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
			
			List<CSVRecord> record = CSVFormat.DEFAULT.parse(reader).getRecords();
			items= new ArrayList<ToDoItem>();
			
		for  (CSVRecord current : record) {
			ToDoItem item = new ToDoItem();
			item.setId(Integer.parseInt(current.get(0)));
			item.setText(current.get(1));
			item.setComplete(Boolean.parseBoolean(current.get(2)));
			items.add(item);
		}
		
		if (items.isEmpty()) {
			return Collections.emptyList();
		}
			
		} catch (IOException e) {
			System.out.println("reading error");
		}
		
		
        return items;
    }

    /**
     * Gets a specific ToDoItem by its id.
     * @param id The id of the ToDoItem.
     * @return The ToDoItem with the specified id or null if none is found.
     */
    public ToDoItem getById(int id) {
    	
    	 for (ToDoItem current : items) {
    		 if (current.getId() == id) {
//    			 System.out.println(current.toString());
    			 return current;
    			 
    		 } 
    		 }
    	 		return null;
    	 }

    /**
     * Updates the given to-do item in the file.
     * @param item The item to update.
     */
    public void update(ToDoItem item) {
    	
    	for (ToDoItem current : items) {
   		 if (current.getId() == item.getId()) {
   			current.setComplete(true);
   		 }
    	}
			try (FileWriter writer = new FileWriter("todolist.csv");
					CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {
					String[] record = {Integer.toString(item.getId()), item.getText(), Boolean.toString(item.isComplete())};
					printer.printRecord(record); 
			}
   			catch (IOException e) {
   				System.out.println("writing error");
   		 }
    }
}
