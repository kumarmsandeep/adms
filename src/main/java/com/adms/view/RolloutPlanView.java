package com.adms.view;

import com.adms.editor.RolloutPlanEditor;
import com.adms.model.RolloutPlan;
import com.adms.repo.RolloutPlanRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RolloutPlanView extends HorizontalLayout {

	private final RolloutPlanRepository repo;

	private final RolloutPlanEditor editor;

	private final Grid<RolloutPlan> grid;
	
	public RolloutPlanView(RolloutPlanRepository repo, RolloutPlanEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>();
		// this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

		// build layout	
		add(this.editor, grid);
		this.editor.setWidth("250px");
		this.setHeight("100%");
		grid.addColumn(RolloutPlan::getId).setHeader("Id");
		grid.addColumn(RolloutPlan::getFromDate).setHeader("From");
		grid.addColumn(RolloutPlan::getToDate).setHeader("To");
		grid.addColumn(RolloutPlan::getCustomerName).setHeader("Customer");
		grid.addColumn(RolloutPlan::getAdvertisementName).setHeader("Advertisement");
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

	public void refresh() {
		this.editor.reloadValues();
	}

}
