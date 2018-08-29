package com.adms.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adms.editor.ReportSearchEditor;
import com.adms.model.Advertisement;
import com.adms.model.Customer;
import com.adms.model.PricingFactor;
import com.adms.model.ReportItem;
import com.adms.model.RolloutPlan;
import com.adms.repo.RolloutPlanRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ReportView extends HorizontalLayout {

	private final RolloutPlanRepository repo;

	private final ReportSearchEditor editor;

	private final Grid<ReportItem> grid;

	public ReportView(RolloutPlanRepository repo, ReportSearchEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>();
		// this.addNewBtn = new Button("New customer",
		// VaadinIcon.PLUS.create());

		// build layout
		add(this.editor, grid);
		this.editor.setWidth("250px");
		this.setHeight("100%");
		grid.addColumn(ReportItem::getAdvtisement).setHeader("Advertisement");
		grid.addColumn(ReportItem::getCost).setHeader("Cost");
		grid.setSelectionMode(SelectionMode.SINGLE);

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler((customer, from, to) -> {
			listReportItems(customer, from, to);
		});
	}

	// tag::listCustomers[]
	void listReportItems(Customer customer, Date from, Date to) {
		Map<Long, List<RolloutPlan>> map = new HashMap<>();
		List<ReportItem> items = new ArrayList<>();
		List<RolloutPlan> findByCustomer = repo.findByCustomer(customer);
		if (findByCustomer != null) {
			for (RolloutPlan rolloutPlan : findByCustomer) {
				if (!(rolloutPlan.getFromDate().after(from) && rolloutPlan.getFromDate().before(to))) {
					continue;
				}
				Long advtId = rolloutPlan.getAdvertisement().getId();
				List<RolloutPlan> list = map.get(advtId);
				if (list == null) {
					list = new ArrayList<>();
					map.put(advtId, list);
				}
				list.add(rolloutPlan);
			}
		}
		if (map.size() > 0) {
			for (Map.Entry<Long, List<RolloutPlan>> entry : map.entrySet()) {
				List<RolloutPlan> value = entry.getValue();
				int cost = 0;
				Advertisement advt = null;
				if (value != null) {
					for (RolloutPlan rolloutPlan : value) {
						advt = rolloutPlan.getAdvertisement();
						List<PricingFactor> pricingFactors = advt.getPricingFactors();
						int priceH = 0;
						if (pricingFactors != null && pricingFactors.size() > 0) {
							for (PricingFactor pricingFactor : pricingFactors) {
								int hhmmFromDate = getHHMMFromDate(rolloutPlan.getFromDate());
								if (hhmmFromDate >= pricingFactor.getFromTime()
										&& hhmmFromDate <= pricingFactor.getToTime()) {
									if ("Premium".equalsIgnoreCase(pricingFactor.getType())) {
										priceH = rolloutPlan.getAdvertisement().getCost() * 3;
									}
									if ("Priority".equalsIgnoreCase(pricingFactor.getType())) {
										priceH = rolloutPlan.getAdvertisement().getCost() * 2;
									}
								}
							}
						}
						if (priceH > 0) {
							cost += priceH;
						} else {
							cost += rolloutPlan.getAdvertisement().getCost();
						}
					}
				}
				if (advt != null) {
					ReportItem item = new ReportItem();
					item.setAdvtisement(advt.getName());
					item.setCost(cost);
					items.add(item);
				}
			}
		}
		grid.setItems(items);
	}

	private int getHHMMFromDate(Date date) {
		return date.getHours() * 100 + date.getMinutes();
	}
	// end::listCustomers[]

	public void refresh() {
		this.editor.reloadValues();
	}

}
