package meloApp;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

public class Doors {
	private LinkedList<String> list = new LinkedList<>(); 
	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Doors window = new Doors();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("Melo");
		GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		shell.setLayout(gridlayout);
		final Label lbl = new Label(shell, SWT.NONE);
		lbl.setBounds(120, 10, 0, 15);
		
		Button addRoomButton = new Button(shell, SWT.CENTER);
		GridData gd_addRoomButton = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		addRoomButton.setLayoutData(gd_addRoomButton);
		addRoomButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lbl.setText("room1");
				InputDialog roomName = new InputDialog(Display.getCurrent().getActiveShell(), "Roomname", "Set the roomname", lbl.getText(), new IInputValidator() {
					
					public String isValid(String arg0) {
						if(!list.contains(arg0)) {
							lbl.setVisible(false);
							return null;
						}
						else {
							return "Room name already exists";
						}
					}
					
				});
				if(roomName.open() == Window.OK) {
					System.out.println("done");
					addRoom(roomName.getValue());
				}
				
			}
		});
		addRoomButton.setText("Add room");

	}
	
	public void addRoom(String roomName) {
		System.out.println("done2");
		Button roomButton = new Button(shell, SWT.NONE);
		roomButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			}
		});
		GridData gd_roomButton = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		roomButton.setLayoutData(gd_roomButton);
		roomButton.setText(roomName);
		shell.layout(true);
	}
}
