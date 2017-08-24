package com.libertymutual.goforcode.todolist.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	// creating variables to be reused in the entire class. 
	private ArrayList<ToDoItem> items;
	private int nextId = 1;

	/**
	 * Assigns a new id to the ToDoItem and saves it to the file.
	 * 
	 * @param item
	 *            The to-do item to save to the file.
	 */
	
	public void create(ToDoItem item) {
		// condition to determine the item size, and if greater than zero, get the ID from the last item and increment by 1.
		if (items.size() > 0) {
			nextId = items.get(items.size() - 1).getId() + 1;
		// if nothing already on the list, set the id to 1.
		} else
			nextId = 1;

		item.setId(nextId);
		// create the file writer and print the new item to the file
		try (FileWriter writer = new FileWriter("todolist.csv", true);
				CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {
			String[] record = { Integer.toString(item.getId()), item.getText(), Boolean.toString(item.isComplete()) };
			printer.printRecord(record);

		} catch (IOException e) {
			System.out.println("writing error");
		}
	}

	/**
	 * Get all the items from the file.
	 * 
	 * @return A list of the items. If no items exist, returns an empty list.
	 */

	public List<ToDoItem> getAll() {
		// create file reader and parser and passing the reader into the parser.
		try (FileReader reader = new FileReader("todolist.csv"); CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {

			// getting records read with parser and save to list of records.
			List<CSVRecord> record = CSVFormat.DEFAULT.parse(reader).getRecords();
			// creating an instance of our ToDoItem Array list.
			items = new ArrayList<ToDoItem>();

			// doing a loop through the list of records, and creating a new ToDoItem for each record.
			for (CSVRecord current : record) {
				ToDoItem item = new ToDoItem();
				// setting the ID for the ToDoItem to equal the current record index item 0. 
				// needed to convert the string to and integer.
				item.setId(Integer.parseInt(current.get(0)));
				// setting the text for the ToDoItem to equal the current record index item 1.
				item.setText(current.get(1));
				// setting the isComplete field for the ToDoItem to equal the current record index item 0.
				// needed to convert the string to boolean.
				item.setComplete(Boolean.parseBoolean(current.get(2)));
				items.add(item);
			}
			// returns and empty list if the items list is empty.
			if (items.isEmpty()) {
				return Collections.emptyList();
			}

		} catch (IOException e) {
			System.out.println("reading error");
		}
		// returns the items we collected through the method
		return items;
	}

	/**
	 * Gets a specific ToDoItem by its id.
	 * 
	 * @param id
	 *            The id of the ToDoItem.
	 * @return The ToDoItem with the specified id or null if none is found.
	 */
	public ToDoItem getById(int id) {
		// looping through our ToDoItems to identify the record with a matching Id.
		for (ToDoItem current : items) {
			if (current.getId() == id) {
				// returning the record with matching id.
				return current;

			}
		}
		// if no matching id, return nothing
		return null;
	}

	/**
	 * Updates the given to-do item in the file.
	 * 
	 * @param item
	 *            The item to update.
	 */
	public void update(ToDoItem item) {
		// loop through the ToDoList to find the record with matching id and set complete value to true.
		for (ToDoItem current : items) {
			if (current.getId() == item.getId()) {
				current.setComplete(true);
			}
		}
		// create new instance of writer and printer
		try (FileWriter writer = new FileWriter("todolist.csv"); CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {
			// loop through list of ToDoItems, grab all values and print to file
			for (ToDoItem current : items) {
				String[] record = { Integer.toString(current.getId()), current.getText(),
						Boolean.toString(current.isComplete()) };
				printer.printRecord(record);
			}
		} catch (IOException e) {
			System.out.println("writing error");
		}
	}
}
