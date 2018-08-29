package com.adms.view;

import com.adms.editor.CustomerEditor;
import com.adms.model.Customer;
import com.adms.repo.CustomerRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class CustomerView extends HorizontalLayout {

	private final CustomerRepository repo;

	private final CustomerEditor editor;

	private final Grid<Customer> grid;

	public CustomerView(CustomerRepository repo, CustomerEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>();
		// this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

		// build layout			
		add(editor, grid);
		editor.setWidth("250px");
		this.setHeight("100%");
		grid.addColumn(Customer::getId).setHeader("Id");
		grid.addColumn(Customer::getName).setHeader("Name");
		grid.addColumn(Customer::getAddress).setHeader("Address");
		grid.addColumn(Customer::getContact).setHeader("Contact");
		grid.addColumn(Customer::getEmailId).setHeader("EmailId");		
		grid.setSelectionMode(SelectionMode.SINGLE);
		// grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);


		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			listCustomers();
		});

		// Initialize listing
		listCustomers();
	}

	// tag::listCustomers[]
	void listCustomers() {
		grid.setItems(repo.findAll());
	}
	// end::listCustomers[]

}
