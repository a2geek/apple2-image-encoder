package a2geek.apple2.image.encoder.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.sun.from.TableSorter;

import a2geek.apple2.image.encoder.A2Image;
import a2geek.apple2.image.encoder.encode.A2Encoder;
import a2geek.apple2.image.encoder.util.DefaultSwingProgressListener;
import a2geek.apple2.image.encoder.util.ProgressListener;

public class ImageEncoderApp {
	private static final String WORKING_DIRECTORY = "WorkingDirectory";
	private static final String MAXIMUM_COMPRESSED_SIZE = "MaximumCompressedSize";
	private static ImageEncoderFrame appFrame;
	private static ImageFileFilter openFilter;
	private static ImageFileFilter saveFilter;
	private static BufferedImage originalImage;
	private static A2Image a2image;
	private static ListSelectionListener listSelectionListener = null;
	private static Preferences prefs = null;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable ignored) {
		}
		prefs = Preferences.userRoot().node("/a2geek/apple2/ImageEncoderApp");
		openFilter = new ImageFileFilter(ImageIO.getReaderFormatNames());
		saveFilter = new ImageFileFilter(ImageIO.getWriterFormatNames());
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		appFrame = new ImageEncoderFrame();
        		appFrame.getOriginalImageScrollPane().setTransferHandler(new DropHandler());
        		appFrame.getMaxSizeTextField().setText(prefs.get(MAXIMUM_COMPRESSED_SIZE, "8192"));
        		appFrame.setVisible(true);
            }
        });
	}
	
	public static void loadImage() {
		JFileChooser chooser = new JFileChooser(getWorkingDirectory());
		chooser.setFileFilter(openFilter);
		chooser.setDialogTitle("Choose an image to convert");
		int action = chooser.showOpenDialog(appFrame);
		if (action == JFileChooser.APPROVE_OPTION) {
			try {
				File file = chooser.getSelectedFile();
				setWorkingDirectory(file);
				loadImage(file);
			} catch (Throwable t) {
				showErrorDialog("loading an image", t);
			}
		}
	}
	
	public static void loadImage(File file) throws IOException {
		originalImage = ImageIO.read(file);
		ImageIcon imageIcon = new ImageIcon(originalImage);

		JScrollPane pane = appFrame.getOriginalImageScrollPane();
        JLabel imageIconLabel = new JLabel();
        imageIconLabel.setHorizontalAlignment(JLabel.CENTER);
        imageIconLabel.setVerticalAlignment(JLabel.CENTER);
        imageIconLabel.setVerticalTextPosition(JLabel.CENTER);
        imageIconLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageIconLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(5,5,5,5)));
        imageIconLabel.setIcon(imageIcon);
        pane.setViewportView(imageIconLabel);

        enableConvertButton();
        disableSaveConvertedImageButton();
        resetImageScrollPane();
        resetCompressionTable();
        
        StringBuffer buf = new StringBuffer();
        buf.append("This image is ");
        buf.append(originalImage.getWidth());
        buf.append("x");
        buf.append(originalImage.getHeight());
        buf.append(".");
        appFrame.originalInfoLabel.setText(buf.toString());
	}
	
	public static void showErrorDialog(String operation, Throwable t) {
		StringBuffer buf = new StringBuffer();
		buf.append("An error occurred while ");
		buf.append(operation);
		buf.append(".  The Java exception message reported is '");
		buf.append(t.getLocalizedMessage());
		buf.append("' for exception '");
		buf.append(t.getClass().getName());
		buf.append("'.");
		JOptionPane.showMessageDialog(appFrame, buf.toString(), "An error occurred!", JOptionPane.ERROR_MESSAGE);
	}

	public static String getAboutText() {
		StringBuffer buf = new StringBuffer();
		buf.append("\n");
		buf.append("Apple II Image Encoder\n");
		buf.append("Copyright (c) 2005\n");
		buf.append("\n");
		buf.append("The image encoder stems from trying to convert a 'modern' 16 million color image into a ");
		buf.append("IIgs super hires 320x200 color.  As usual, the story quickly turned complicated since the ");
		buf.append("IIgs color palette isn't one set of 256 colors but 16 palettes of 16 colors.  The image ");
		buf.append("tools can generate a beautiful 256 color image or a fairly good 16 color image.  However, ");
		buf.append("I wanted something that could take care of the IIgs palette.\n");
		buf.append("\n");
		buf.append("From there, I started getting carried away...");
		return buf.toString();
	}
	
	public static void convertImage() {
		final ProgressListener listener = new DefaultSwingProgressListener(appFrame, 
				"Converting image - please wait...", "Starting up");
		Thread thread = new Thread() {
			public void run() {
				GraphicsModeComboBoxModel model = (GraphicsModeComboBoxModel)appFrame.getGraphicModeComboBox().getModel();
				Class imageClass = model.getSelectedClass();
				boolean keepAspectRatio = appFrame.getKeepAspectRatioCheckBox().isSelected();
				try {
					Constructor constructor = imageClass.getDeclaredConstructor(
							new Class[] { BufferedImage.class, boolean.class, ProgressListener.class });
					a2image = (A2Image)constructor.newInstance(new Object[] { originalImage, keepAspectRatio, listener });
					if (listener.isCancelled()) {
						a2image = null;
						return;
					}
				} catch (Throwable t) {
					showErrorDialog("Unable to create graphics mode handler.", t);
					return;
				}
				
				BufferedImage displayImage = a2image.getImage();
				if (displayImage.getWidth() < 320) {
					displayImage = new BufferedImage(a2image.getWidth()*5, a2image.getHeight()*5,
							BufferedImage.TYPE_INT_RGB);
					Graphics g = displayImage.getGraphics();
					g.drawImage(a2image.getImage(), 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
					g.dispose();
				}
		
				ImageIcon imageIcon = new ImageIcon(displayImage);
				JScrollPane pane = appFrame.getConvertedImageScrollPane();
		        JLabel imageIconLabel = new JLabel();
		        imageIconLabel.setHorizontalAlignment(JLabel.CENTER);
		        imageIconLabel.setVerticalAlignment(JLabel.CENTER);
		        imageIconLabel.setVerticalTextPosition(JLabel.CENTER);
		        imageIconLabel.setHorizontalTextPosition(JLabel.CENTER);
		        imageIconLabel.setBorder(BorderFactory.createCompoundBorder(
		            BorderFactory.createLoweredBevelBorder(),
		            BorderFactory.createEmptyBorder(5,5,5,5)));
		        imageIconLabel.setIcon(imageIcon);
		        pane.setViewportView(imageIconLabel);
		        
		        enableSaveConvertedImageButton();
		        enableCompressButton();
		        resetCompressionTable();
			}
		};
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}

	public static void saveImage() {
		JFileChooser chooser = new JFileChooser(getWorkingDirectory());
		chooser.setFileFilter(saveFilter);
		chooser.setDialogTitle("Choose where to save your image");
		int action = chooser.showSaveDialog(appFrame);
		if (action == JFileChooser.APPROVE_OPTION) {
			try {
				File file = chooser.getSelectedFile();
				setWorkingDirectory(file);

				FileOutputStream output = new FileOutputStream(file);
				output.write(a2image.getBytes());
				output.close();
			} catch (Throwable t) {
				showErrorDialog("saving an image", t);
			}
		}
	}

	public static void compressAll() {
		final ProgressListener listener = new DefaultSwingProgressListener(appFrame, 
				"Please wait - compressing images...", "Initializing.");
		Thread thread = new Thread() {
			public void run() {
				try {
					int maxSize = Integer.parseInt(appFrame.getMaxSizeTextField().getText());
					prefs.putInt(MAXIMUM_COMPRESSED_SIZE, maxSize);
					JTable table = appFrame.getCompressionTable();
					EncoderTableModel model = new EncoderTableModel(a2image, maxSize, listener);
					TableSorter sorter = new TableSorter(model);
					table.setModel(sorter);
					DefaultTableCellRenderer renderer = new EncoderTableCellRenderer(model, maxSize);
					table.setDefaultRenderer(String.class, renderer);
					table.setDefaultRenderer(Integer.class, renderer);
					sorter.setTableHeader(table.getTableHeader());
					ListSelectionModel listSelectionModel = table.getSelectionModel();
					listSelectionModel.removeListSelectionListener(getListSelectionListener());
					listSelectionModel.addListSelectionListener(getListSelectionListener());
				} catch (Throwable t) {
					showErrorDialog("compressing", t);
				}
			}
		};
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.run();
	}
	
	private static ListSelectionListener getListSelectionListener() {
		if (listSelectionListener == null) {
			listSelectionListener = new ListSelectionListener() {
			    public void valueChanged(ListSelectionEvent e) {
   			        if (e.getValueIsAdjusting()) return; //Ignore extra messages.
			        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		            appFrame.getSaveEncodedImageButton().setEnabled(!lsm.isSelectionEmpty());
			    }
			};
		}
		return listSelectionListener;
	}
	
	public static void saveEncodedImage() {
		JFileChooser chooser = new JFileChooser(getWorkingDirectory());
		chooser.setDialogTitle("Choose where to save your data");
		int action = chooser.showSaveDialog(appFrame);
		if (action == JFileChooser.APPROVE_OPTION) {
			try {
				File file = chooser.getSelectedFile();
				setWorkingDirectory(file);

				TableSorter sorter = (TableSorter)appFrame.getCompressionTable().getModel();
				EncoderTableModel model = (EncoderTableModel)sorter.getTableModel();
				A2Encoder encoder = model.getSelectedEncoder(appFrame.getCompressionTable().getSelectedRow());

				FileOutputStream output = new FileOutputStream(file);
				output.write(encoder.getData(), 0, encoder.getSize());
				output.close();
			} catch (Throwable t) {
				showErrorDialog("saving an image", t);
			}
		}
	}

	public static void enableConvertButton() {
		appFrame.getConvertButton().setEnabled(true);
	}
	public static void disableConvertButton() {
		appFrame.getConvertButton().setEnabled(false);
	}
	public static void enableSaveConvertedImageButton() {
	    appFrame.getSaveConvertedImageButton().setEnabled(true);
	}
	public static void disableSaveConvertedImageButton() {
	    appFrame.getSaveConvertedImageButton().setEnabled(false);
	}
	public static void resetImageScrollPane() {
	    appFrame.getConvertedImageScrollPane().setViewportView(appFrame.noImageConvertedLabel);
	}
	public static void resetCompressionTable() {
		appFrame.getCompressionTable().setModel(new DefaultTableModel());	// empty it out
	}
	public static void enableCompressButton() {
		appFrame.getCompressButton().setEnabled(true);
	}
	public static void disableCompressButton() {
		appFrame.getCompressButton().setEnabled(false);
	}
	private static String getWorkingDirectory() {
		return prefs.get(WORKING_DIRECTORY, null);
	}
	private static void setWorkingDirectory(File file) {
		prefs.put(WORKING_DIRECTORY, file.getParentFile() == null ? 
				null : file.getParentFile().getAbsolutePath());
	}
}
