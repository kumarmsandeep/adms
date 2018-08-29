package com.adms.view;

import com.adms.editor.AdvertisementEditor;
import com.adms.model.Advertisement;
import com.adms.repo.AdvertisementRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class AdvertisementView extends HorizontalLayout {

	private final AdvertisementRepository repo;

	private final AdvertisementEditor editor;

	private final Grid<Advertisement> grid;

	public AdvertisementView(AdvertisementRepository repo, AdvertisementEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>();
		// this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

		// build layout			
		add(this.editor, grid);
		editor.setWidth("250px");
		this.setHeight("100%");
		grid.setPageSize(20);
		grid.addColumn(Advertisement::getId).setHeader("Id");
		grid.addColumn(Advertisement::getName).setHeader("Name");
		grid.addColumn(Advertisement::getUrl).setHeader("Url");
		grid.addColumn(Advertisement::getCost).setHeader("Cost");
		grid.addColumn(Advertisement::getDuration).setHeader("Duration");
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
