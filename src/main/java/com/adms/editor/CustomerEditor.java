package com.adms.editor;

import org.springframework.beans.factory.annotation.Autowired;

import com.adms.model.Customer;
import com.adms.repo.CustomerRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

	private final CustomerRepository repository;

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("Name");
	TextArea address = new TextArea("Address");
	TextField contact = new TextField("Contact");
	TextField emailId = new TextField("EmailId");

	/* Action buttons */
	// TODO why more code?
	Button save = new Button("Add Customer", VaadinIcon.PLUS.create());
	HorizontalLayout actions = new HorizontalLayout(save);

	Binder<Customer> binder = new Binder<>(Customer.class);
	private ChangeHandler changeHandler;

	@Autowired
	public CustomerEditor(CustomerRepository repository) {
		this.repository = repository;

		add(name, address, contact, emailId, actions);
		
		name.setRequired(true);
		name.setRequiredIndicatorVisible(true);
		address.setRequired(true);
		address.setRequiredIndicatorVisible(true);
		contact.setRequired(true);
		contact.setRequiredIndicatorVisible(true);
		emailId.setRequired(true);
		emailId.setRequiredIndicatorVisible(true);

		
		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(false);

		save.getElement().getThemeList().add("primary");		

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
	}

	void save() {
		String v_name = name.getValue();
		String v_address = address.getValue();
		String v_contact = contact.getValue();
		String v_emailId = emailId.getValue();
		Customer customer = new Customer();
		customer.setName(v_name);
		customer.setAddress(v_address);
		customer.setContact(v_contact);
		customer.setEmailId(v_emailId);
		repository.save(customer);
		changeHandler.onChange();
	}

	public interface ChangeHandler {
		void onChange();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}
