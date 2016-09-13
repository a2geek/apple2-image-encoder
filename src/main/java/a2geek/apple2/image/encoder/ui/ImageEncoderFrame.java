package a2geek.apple2.image.encoder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class ImageEncoderFrame extends JFrame {
	private JPanel contentPane = null;
	private JTabbedPane tabbedPane = null;
	private JPanel originalTabPanel = null;
	private JPanel convertTabPanel = null;
	private JPanel compressionTabPanel = null;
	private JPanel loadPanel = null;
	private JButton loadButton = null;
	private JLabel loadLabel = null;
	private JScrollPane originalImageScrollPane = null;
	private JLabel noImageLoadedLabel = null;
	private JPanel aboutTabPanel = null;
	private JLabel aboutImageLabel = null;
	private JTextPane aboutTextPane = null;
	private JScrollPane aboutScrollPane = null;
	private JPanel convertPanel = null;
	private JButton convertButton = null;
	private JLabel convertLabel = null;
	private JPanel saveConvertedImagePanel = null;
	private JButton saveConvertedImageButton = null;
	private JLabel saveConvertedImageLabel = null;
	private JScrollPane convertedImageScrollPane = null;
	public JLabel noImageConvertedLabel = null;
	private JPanel originalInfoPanel = null;
	public JLabel originalInfoLabel = null;
	private JPanel compressionPanel = null;
	private JButton compressButton = null;
	private JLabel compressLabel = null;
	private JTextField maxSizeTextField = null;
	private JScrollPane compressionScrollPane = null;
	private JTable compressionTable = null;
	private JLabel bytesLabel = null;
	private JPanel saveEncodedImagePanel = null;
	private JButton saveEncodedImageButton = null;
	private JLabel encodedImageLabel = null;
	private JCheckBox keepAspectRatioCheckBox = null;
	private JComboBox graphicModeComboBox = null;
	private JLabel formatLabel = null;
	/**
	 * This is the default constructor
	 */
	public ImageEncoderFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(626, 414);
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		Package pkg = getClass().getPackage();
		String title = pkg.getImplementationTitle();
		if (title == null) {
			title = "Apple II Image Encoder";
		}
		String version = pkg.getImplementationVersion();
		if (version == null) {
			version = "PROTOTYPE";
		}
		this.setTitle(String.format("%s - %s", title, version));		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(getTabbedPane(), java.awt.BorderLayout.CENTER);
		}
		return contentPane;
	}

	/**
	 * This method initializes tabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setTabPlacement(javax.swing.JTabbedPane.TOP);
			tabbedPane.setPreferredSize(new java.awt.Dimension(600,300));
			tabbedPane.addTab("Original", null, getOriginalTabPanel(), null);
			tabbedPane.addTab("Convert", null, getConvertTabPanel(), null);
			tabbedPane.addTab("Compression", null, getCompressionTabPanel(), null);
			tabbedPane.addTab("About", null, getAboutTabPanel(), null);
		}
		return tabbedPane;
	}

	/**
	 * This method initializes originalImagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getOriginalTabPanel() {
		if (originalTabPanel == null) {
			originalTabPanel = new JPanel();
			originalTabPanel.setLayout(new BorderLayout());
			originalTabPanel.add(getLoadPanel(), java.awt.BorderLayout.NORTH);
			originalTabPanel.add(getOriginalImageScrollPane(), java.awt.BorderLayout.CENTER);
			originalTabPanel.add(getOriginalInfoPanel(), java.awt.BorderLayout.SOUTH);
		}
		return originalTabPanel;
	}

	/**
	 * This method initializes convertedImagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConvertTabPanel() {
		if (convertTabPanel == null) {
			convertTabPanel = new JPanel();
			convertTabPanel.setLayout(new BorderLayout());
			convertTabPanel.add(getConvertPanel(), java.awt.BorderLayout.NORTH);
			convertTabPanel.add(getSaveConvertedImagePanel(), java.awt.BorderLayout.SOUTH);
			convertTabPanel.add(getConvertedImageScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return convertTabPanel;
	}

	/**
	 * This method initializes compressionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCompressionTabPanel() {
		if (compressionTabPanel == null) {
			compressionTabPanel = new JPanel();
			compressionTabPanel.setLayout(new BorderLayout());
			compressionTabPanel.add(getCompressionPanel(), java.awt.BorderLayout.NORTH);
			compressionTabPanel.add(getCompressionScrollPane(), java.awt.BorderLayout.CENTER);
			compressionTabPanel.add(getSaveEncodedImagePanel(), java.awt.BorderLayout.SOUTH);
		}
		return compressionTabPanel;
	}

	/**
	 * This method initializes loadPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLoadPanel() {
		if (loadPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			loadLabel = new JLabel();
			loadLabel.setText("Load an image to be converted into an Apple II format.");
			loadPanel = new JPanel();
			loadPanel.setLayout(flowLayout);
			loadPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
			loadPanel.add(getLoadButton(), null);
			loadPanel.add(loadLabel, null);
		}
		return loadPanel;
	}

	/**
	 * This method initializes loadButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLoadButton() {
		if (loadButton == null) {
			loadButton = new JButton();
			loadButton.setText("Load");
			loadButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ImageEncoderApp.loadImage();
				}
			});
		}
		return loadButton;
	}

	/**
	 * This method initializes originalImageScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	public JScrollPane getOriginalImageScrollPane() {
		if (originalImageScrollPane == null) {
			noImageLoadedLabel = new JLabel();
			noImageLoadedLabel.setText("No image currently loaded.");
			noImageLoadedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			noImageLoadedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			originalImageScrollPane = new JScrollPane();
			originalImageScrollPane.setViewportView(noImageLoadedLabel);
		}
		return originalImageScrollPane;
	}

	/**
	 * This method initializes aboutPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAboutTabPanel() {
		if (aboutTabPanel == null) {
			aboutImageLabel = new JLabel();
			aboutImageLabel.setText("");
			aboutImageLabel.setIcon(new ImageIcon(getClass().getResource("/images/a2gssmall.jpg")));
			aboutImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			aboutImageLabel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			aboutImageLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
			aboutImageLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			aboutImageLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			aboutTabPanel = new JPanel();
			aboutTabPanel.setLayout(new BorderLayout());
			aboutTabPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			aboutTabPanel.add(aboutImageLabel, java.awt.BorderLayout.WEST);
			aboutTabPanel.add(getAboutScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return aboutTabPanel;
	}

	/**
	 * This method initializes aboutTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getAboutTextPane() {
		if (aboutTextPane == null) {
			aboutTextPane = new JTextPane();
			aboutTextPane.setText(ImageEncoderApp.getAboutText());
			aboutTextPane.setBackground(java.awt.SystemColor.control);
			aboutTextPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14));
			aboutTextPane.setEditable(false);
		}
		return aboutTextPane;
	}

	/**
	 * This method initializes aboutScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAboutScrollPane() {
		if (aboutScrollPane == null) {
			aboutScrollPane = new JScrollPane();
			aboutScrollPane.setViewportView(getAboutTextPane());
		}
		return aboutScrollPane;
	}

	/**
	 * This method initializes convertPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getConvertPanel() {
		if (convertPanel == null) {
			formatLabel = new JLabel();
			formatLabel.setText("format.");
			convertLabel = new JLabel();
			convertLabel.setText("Convert image to");
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			convertPanel = new JPanel();
			convertPanel.setLayout(flowLayout1);
			convertPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			convertPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
			convertPanel.add(getConvertButton(), null);
			convertPanel.add(convertLabel, null);
			convertPanel.add(getGraphicModeComboBox(), null);
			convertPanel.add(formatLabel, null);
			convertPanel.add(getKeepAspectRatioCheckBox(), null);
		}
		return convertPanel;
	}

	/**
	 * This method initializes convertButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getConvertButton() {
		if (convertButton == null) {
			convertButton = new JButton();
			convertButton.setText("Convert");
			convertButton.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			convertButton.setEnabled(false);
			convertButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			convertButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ImageEncoderApp.convertImage();
				}
			});
		}
		return convertButton;
	}

	/**
	 * This method initializes savePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSaveConvertedImagePanel() {
		if (saveConvertedImagePanel == null) {
			saveConvertedImageLabel = new JLabel();
			saveConvertedImageLabel.setText("You may save this file in its native Apple II format.");
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(java.awt.FlowLayout.RIGHT);
			saveConvertedImagePanel = new JPanel();
			saveConvertedImagePanel.setBackground(java.awt.SystemColor.controlLtHighlight);
			saveConvertedImagePanel.setLayout(flowLayout2);
			saveConvertedImagePanel.add(saveConvertedImageLabel, null);
			saveConvertedImagePanel.add(getSaveConvertedImageButton(), null);
		}
		return saveConvertedImagePanel;
	}

	/**
	 * This method initializes saveConvertedImageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getSaveConvertedImageButton() {
		if (saveConvertedImageButton == null) {
			saveConvertedImageButton = new JButton();
			saveConvertedImageButton.setText("Save");
			saveConvertedImageButton.setEnabled(false);
			saveConvertedImageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ImageEncoderApp.saveImage();
				}
			});
		}
		return saveConvertedImageButton;
	}

	/**
	 * This method initializes convertedImageScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	public JScrollPane getConvertedImageScrollPane() {
		if (convertedImageScrollPane == null) {
			noImageConvertedLabel = new JLabel();
			noImageConvertedLabel.setText("No image has been converted.");
			noImageConvertedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			noImageConvertedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			convertedImageScrollPane = new JScrollPane();
			convertedImageScrollPane.setViewportView(noImageConvertedLabel);
		}
		return convertedImageScrollPane;
	}

	/**
	 * This method initializes originalInfoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getOriginalInfoPanel() {
		if (originalInfoPanel == null) {
			originalInfoLabel = new JLabel();
			originalInfoLabel.setText("");
			originalInfoPanel = new JPanel();
			originalInfoPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
			originalInfoPanel.add(originalInfoLabel, null);
		}
		return originalInfoPanel;
	}

	/**
	 * This method initializes compressionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCompressionPanel() {
		if (compressionPanel == null) {
			bytesLabel = new JLabel();
			bytesLabel.setText("bytes.");
			compressLabel = new JLabel();
			compressLabel.setText("Try all known compression methods. Maximum size allowed is");
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(java.awt.FlowLayout.LEFT);
			compressionPanel = new JPanel();
			compressionPanel.setLayout(flowLayout3);
			compressionPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
			compressionPanel.add(getCompressButton(), null);
			compressionPanel.add(compressLabel, null);
			compressionPanel.add(getMaxSizeTextField(), null);
			compressionPanel.add(bytesLabel, null);
		}
		return compressionPanel;
	}

	/**
	 * This method initializes compressButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getCompressButton() {
		if (compressButton == null) {
			compressButton = new JButton();
			compressButton.setText("Compress");
			compressButton.setEnabled(false);
			compressButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ImageEncoderApp.compressAll();
				}
			});
		}
		return compressButton;
	}

	/**
	 * This method initializes maxSizeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getMaxSizeTextField() {
		if (maxSizeTextField == null) {
			maxSizeTextField = new JTextField();
			maxSizeTextField.setColumns(5);
			maxSizeTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			maxSizeTextField.setText("8192");
		}
		return maxSizeTextField;
	}

	/**
	 * This method initializes compressionScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getCompressionScrollPane() {
		if (compressionScrollPane == null) {
			compressionScrollPane = new JScrollPane();
			compressionScrollPane.setViewportView(getCompressionTable());
		}
		return compressionScrollPane;
	}

	/**
	 * This method initializes compressionTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	public JTable getCompressionTable() {
		if (compressionTable == null) {
			compressionTable = new JTable();
			compressionTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			compressionTable.setEnabled(true);
		}
		return compressionTable;
	}

	/**
	 * This method initializes saveEncodedImagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSaveEncodedImagePanel() {
		if (saveEncodedImagePanel == null) {
			encodedImageLabel = new JLabel();
			encodedImageLabel.setText("You may save the compressed image.");
			FlowLayout flowLayout4 = new FlowLayout();
			flowLayout4.setAlignment(java.awt.FlowLayout.RIGHT);
			saveEncodedImagePanel = new JPanel();
			saveEncodedImagePanel.setLayout(flowLayout4);
			saveEncodedImagePanel.setBackground(java.awt.SystemColor.controlLtHighlight);
			saveEncodedImagePanel.add(encodedImageLabel, null);
			saveEncodedImagePanel.add(getSaveEncodedImageButton(), null);
		}
		return saveEncodedImagePanel;
	}

	/**
	 * This method initializes saveEncodedImageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getSaveEncodedImageButton() {
		if (saveEncodedImageButton == null) {
			saveEncodedImageButton = new JButton();
			saveEncodedImageButton.setText("Save");
			saveEncodedImageButton.setEnabled(false);
			saveEncodedImageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ImageEncoderApp.saveEncodedImage();
				}
			});
		}
		return saveEncodedImageButton;
	}

	/**
	 * This method initializes keepAspectRatioCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	public JCheckBox getKeepAspectRatioCheckBox() {
		if (keepAspectRatioCheckBox == null) {
			keepAspectRatioCheckBox = new JCheckBox();
			keepAspectRatioCheckBox.setText("Keep image aspect ratio.");
			keepAspectRatioCheckBox.setSelected(true);
			keepAspectRatioCheckBox.setBackground(java.awt.SystemColor.controlLtHighlight);
		}
		return keepAspectRatioCheckBox;
	}

	/**
	 * This method initializes graphicModeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getGraphicModeComboBox() {
		if (graphicModeComboBox == null) {
			graphicModeComboBox = new JComboBox();
			graphicModeComboBox.setModel(new GraphicsModeComboBoxModel());
		}
		return graphicModeComboBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
