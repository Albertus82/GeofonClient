package it.albertus.geofon.client.gui;

import it.albertus.geofon.client.GeofonClient;
import it.albertus.geofon.client.gui.listener.CloseListener;
import it.albertus.geofon.client.gui.listener.RestoreShellListener;
import it.albertus.geofon.client.model.Earthquake;
import it.albertus.geofon.client.resources.Messages;
import it.albertus.util.Configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class TrayIcon {

	public interface Defaults {
		boolean MINIMIZE_TRAY = true;
	}

	private final GeofonClientGui gui;
	private final Configuration configuration = GeofonClient.configuration;

	private Tray tray;
	private TrayItem trayItem;
	private ToolTip toolTip;

	private Menu trayMenu;
	private MenuItem showMenuItem;
	private MenuItem exitMenuItem;

	/* To be accessed only from this class */
	private Image trayIcon;

	public TrayIcon(final GeofonClientGui gui) {
		this.gui = gui;
		gui.getShell().addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(final ShellEvent se) {
				if (configuration.getBoolean("minimize.tray", Defaults.MINIMIZE_TRAY)) {
					iconify();
				}
			}
		});
	}

	private Image getTrayIcon() {
		return gui.getFavicon();
	}

	private void iconify() {
		if (tray == null || trayItem == null || trayItem.isDisposed()) {
			/* Inizializzazione */
			try {
				tray = gui.getShell().getDisplay().getSystemTray();

				if (tray != null) {
					trayItem = new TrayItem(tray, SWT.NONE);
					trayIcon = getTrayIcon();
					trayItem.setImage(trayIcon);
					trayItem.setToolTipText(Messages.get("lbl.tray.tooltip"));

					toolTip = new ToolTip(gui.getShell(), SWT.BALLOON | SWT.ICON_WARNING);
					toolTip.setVisible(false);
					toolTip.setAutoHide(true);
					toolTip.addListener(SWT.Selection, new RestoreShellListener(gui));
					trayItem.setToolTip(toolTip);

					trayMenu = new Menu(gui.getShell(), SWT.POP_UP);
					showMenuItem = new MenuItem(trayMenu, SWT.PUSH);
					showMenuItem.setText(Messages.get("lbl.tray.show"));
					showMenuItem.addListener(SWT.Selection, new RestoreShellListener(gui));
					trayMenu.setDefaultItem(showMenuItem);

					new MenuItem(trayMenu, SWT.SEPARATOR);

					exitMenuItem = new MenuItem(trayMenu, SWT.PUSH);
					exitMenuItem.setText(Messages.get("lbl.tray.close"));
					exitMenuItem.addSelectionListener(new CloseListener(gui));
					trayItem.addMenuDetectListener(new MenuDetectListener() {
						@Override
						public void menuDetected(MenuDetectEvent e) {
							trayMenu.setVisible(true);
						}
					});

					trayItem.addListener(SWT.DefaultSelection, new RestoreShellListener(gui));
				}
			}
			catch (Exception e) {
				e.printStackTrace();//Logger.getInstance().log(e);
			}
		}

		if (tray != null && !tray.isDisposed() && trayItem != null && !trayItem.isDisposed()) {
			gui.getShell().setVisible(false);
			trayItem.setVisible(true);
			trayItem.setImage(trayIcon); // Update icon
			gui.getShell().setMinimized(false);
		}
	}

	public void showBalloonToolTip(final Earthquake earthquake) {
		final StringBuilder text = new StringBuilder();
		text.append("M ").append(earthquake.getMagnitudo()).append(", ").append(earthquake.getRegion());

		final StringBuilder message = new StringBuilder();
		message.append(ResultTable.formatDate(earthquake.getTime())).append(" ");
		message.append(earthquake.getLatitude()).append(" ");
		message.append(earthquake.getLongitude()).append(" ");
		message.append(earthquake.getDepth()).append(" ");
		message.append(earthquake.getStatus());

		try {
			trayItem.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					toolTip.setText(text.toString().trim());
					toolTip.setMessage(message.toString().trim());
					toolTip.setVisible(true);
				}
			});
		}
		catch (SWTException se) {}
	}

	public Tray getTray() {
		return tray;
	}

	public TrayItem getTrayItem() {
		return trayItem;
	}

	public ToolTip getToolTip() {
		return toolTip;
	}

	public Menu getTrayMenu() {
		return trayMenu;
	}

	public MenuItem getShowMenuItem() {
		return showMenuItem;
	}

	public MenuItem getExitMenuItem() {
		return exitMenuItem;
	}

}