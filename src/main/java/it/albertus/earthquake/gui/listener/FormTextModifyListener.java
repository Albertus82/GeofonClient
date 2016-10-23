package it.albertus.earthquake.gui.listener;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import it.albertus.earthquake.gui.SearchForm;

public class FormTextModifyListener implements ModifyListener {

	private final SearchForm form;

	public FormTextModifyListener(final SearchForm form) {
		this.form = form;
	}

	@Override
	public void modifyText(final ModifyEvent me) {
		form.updateButtons();
	}

}
