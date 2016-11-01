package it.albertus.earthquake.gui.listener;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import it.albertus.earthquake.gui.AboutDialog;
import it.albertus.earthquake.resources.Messages;
import it.albertus.util.Version;

public class AboutSelectionListener extends SelectionAdapter implements Listener {

	private final IShellProvider gui;

	public AboutSelectionListener(final IShellProvider gui) {
		this.gui = gui;
	}

	@Override
	public void widgetSelected(final SelectionEvent se) {
		final AboutDialog aboutDialog = new AboutDialog(gui.getShell());
		aboutDialog.setText(Messages.get("lbl.about.title"));
		aboutDialog.setMessage(Messages.get("msg.application.name") + ' ' + Messages.get("msg.version", Version.getInstance().getNumber(), Version.getInstance().getDate()));
		aboutDialog.setApplicationUrl(Messages.get("msg.website"));
		aboutDialog.setIconUrl(Messages.get("msg.info.icon.site"));
		aboutDialog.open();
	}

	@Override
	public void handleEvent(final Event event) {
		widgetSelected(null);
	}

}
