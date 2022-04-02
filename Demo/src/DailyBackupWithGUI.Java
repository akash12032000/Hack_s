import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

public class DailyBackup extends JFrame {

	private JPanel contentPane;
	private static JButton btnNewButton;
	private static int progress = 90;
	private static JProgressBar progressBar;
	private static JTextArea textArea;
	private SwingWorker<Void, Void> sw;
	private JButton btnNewButton_1;
	private static String MainBackupPath;
	private static String GetBackupPath;
	private static String ConfigurationFilePath;
	private static long filecount = 0;
	private static long filelengthforprogressbar = 0;
	private static Set<String> ExtensionList = new HashSet<String>();
	static Properties prop = new Properties();
	private static boolean fileflag = true;

	static {
		ConfigurationFilePath = System.getProperty("user.home") + "\\SKBackupConfiguration";
	}

	private static void LoadConfigurationFile() {
		try {
			prop.load(new FileReader(ConfigurationFilePath + "\\BackupConfiguration.txt"));
		} catch (IOException e) {
			CreateConfigurationFile();
		}
	}

	// Create UnDeletable File
	@SuppressWarnings("unused")
	private static void UnDeletableFile(String Path) {

		Path file = Paths.get(Path);
		AclFileAttributeView aclAttr = Files.getFileAttributeView(file, AclFileAttributeView.class);
		// System.out.println();

		UserPrincipalLookupService upls = file.getFileSystem().getUserPrincipalLookupService();
		UserPrincipal user = null;
		try {
			user = upls.lookupPrincipalByName(System.getProperty("user.name"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		AclEntry.Builder builder = AclEntry.newBuilder();
		builder.setPermissions(EnumSet.of(AclEntryPermission.APPEND_DATA, AclEntryPermission.DELETE,
				AclEntryPermission.DELETE_CHILD, AclEntryPermission.EXECUTE, AclEntryPermission.READ_ACL,
				AclEntryPermission.READ_ATTRIBUTES, AclEntryPermission.READ_DATA, AclEntryPermission.READ_NAMED_ATTRS,
				AclEntryPermission.SYNCHRONIZE, AclEntryPermission.WRITE_ACL, AclEntryPermission.WRITE_ATTRIBUTES,
				AclEntryPermission.WRITE_DATA, AclEntryPermission.WRITE_NAMED_ATTRS, AclEntryPermission.WRITE_OWNER));
		builder.setPrincipal(user);
		builder.setType(AclEntryType.DENY);
		try {
			aclAttr.setAcl(Collections.singletonList(builder.build()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Delete UnDeletable File
	@SuppressWarnings("unused")
	private static void DeleteUnDeletableFile(String Path) {

		System.out.println("Enter Password : ");
		Scanner scanner = new Scanner(System.in);
		if (scanner.next().equals("LOVE")) {
			Path file = Paths.get(Path);
			AclFileAttributeView aclAttr = Files.getFileAttributeView(file, AclFileAttributeView.class);
			// System.out.println();

			UserPrincipalLookupService upls = file.getFileSystem().getUserPrincipalLookupService();
			UserPrincipal user = null;
			try {
				user = upls.lookupPrincipalByName(System.getProperty("user.name"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			AclEntry.Builder builder = AclEntry.newBuilder();
			builder.setPermissions(EnumSet.of(AclEntryPermission.APPEND_DATA, AclEntryPermission.DELETE,
					AclEntryPermission.DELETE_CHILD, AclEntryPermission.EXECUTE, AclEntryPermission.READ_ACL,
					AclEntryPermission.READ_ATTRIBUTES, AclEntryPermission.READ_DATA,
					AclEntryPermission.READ_NAMED_ATTRS, AclEntryPermission.SYNCHRONIZE, AclEntryPermission.WRITE_ACL,
					AclEntryPermission.WRITE_ATTRIBUTES, AclEntryPermission.WRITE_DATA,
					AclEntryPermission.WRITE_NAMED_ATTRS, AclEntryPermission.WRITE_OWNER));
			builder.setPrincipal(user);
			builder.setType(AclEntryType.ALLOW);
			try {
				aclAttr.setAcl(Collections.singletonList(builder.build()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Wrong Password Try Again!");
		}
		scanner.close();

	}

	// Backup Folder
	private static boolean makeBackupFolder() {
		boolean flag = false;
		File backupFolder = null;
		try {
			backupFolder = new File(MainBackupPath);
		} catch (NullPointerException e) {
		}
		try {
			if (!backupFolder.exists()) {
				flag = backupFolder.mkdirs();
			}
		} catch (Exception e) {
		}
		return flag;
	}

	// Get All Extension
	@SuppressWarnings("unused")
	private static Set<String> getExtension() {
		Set<String> extensionList = new HashSet<String>();
		extensionList.add("JAVA");
		extensionList.add("TXT");
		extensionList.add("CLASS");
		extensionList.add("CSS");
		extensionList.add("XLSM");
		extensionList.add("HTML");
		extensionList.add("XML");
		extensionList.add("DOC");
		extensionList.add("PPTX");
		extensionList.add("MP3");
		extensionList.add("MP4");
		extensionList.add("AIF");
		extensionList.add("MPA");
		extensionList.add("MKV");
		extensionList.add("JS");
//		extensionList.add("ZIP");
		extensionList.add("RAR");
		extensionList.add("SQL");
		extensionList.add("EMAIL");
		extensionList.add("APK");
		extensionList.add("BAT");
		extensionList.add("JAR");
		extensionList.add("EXE");
		extensionList.add("PY");
		extensionList.add("JSP");
		extensionList.add("PPT");
		extensionList.add("XLSX");
		extensionList.add("XLS");
		extensionList.add("MPG");
		extensionList.add("MPEG");
		extensionList.add("JPG");
		extensionList.add("PNG");
		extensionList.add("JPEG");
		extensionList.add("SVG");
		return extensionList;
	}

	// Working With Directory
	private static void getAllDir(String path) {
//		Set<String> extension = getExtension();
		File file = new File(path);
		if (file.isFile()) {
			writeFiles(file.getPath());
		} else if (file.isDirectory()) {
			File[] filelist = file.listFiles();
			if (fileflag) {
				filelengthforprogressbar = filelist.length - 1;
				fileflag = false;
			}

			try {
				for (File file1 : filelist) {
					if (file1.isDirectory()) {
						getAllDir(file1.getPath());
					}
					if (file1.isFile() && !file1.isHidden()) {
						String filename = file1.getName();
						try {
							if (ExtensionList
									.contains(filename.substring(filename.lastIndexOf(".") + 1).toUpperCase())) {
								writeFiles(file1.getPath());
							}
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
			} catch (NullPointerException e) {
			}
		}
	}

	// Create Files For Backup
	private static void writeFiles(String path) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String todate = dateFormat.format(new Date());
		File file = new File(path);
		if (todate.equals(dateFormat.format(file.lastModified()))) {
			String FinalFileName = MainBackupPath + "\\" + file.getName();
			filecount += 1;
			textArea.append(filecount + " : " + FinalFileName + "\n");
//			System.out.println((int) (progressBar.getValue()+(filelengthforprogressbar/100)));
//			progressBar.setValue((int) (progressBar.getValue()+(filelengthforprogressbar/100)));
			try {
				FileOutputStream outputStream = new FileOutputStream(FinalFileName);
				try {
					outputStream.write(Files.readAllBytes(file.toPath()));
				} catch (Exception e) {
				}
				outputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}.run();
		}
	}

	// Create All Backup Files Zip_File
	private static void PackInZip(String sourceDirPath, String newpath) {
		Path p = null;
		try {
			p = Files.createFile(Paths.get(newpath));
		} catch (IOException e1) {
			textArea.setText("Something Went Wrong.. \nTry Again After Few Minutes..");
			btnNewButton.setEnabled(true);
			CreateConfigurationFile();
		}
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(sourceDirPath);
			try {
				Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
					ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
					try {
						zs.putNextEntry(zipEntry);
						Files.copy(path, zs);
						zs.closeEntry();
					} catch (IOException e) {
					}
				});
			} catch (IOException e) {
				textArea.setText(e.getMessage());
				btnNewButton.setEnabled(true);
			}
		} catch (IOException e1) {
			textArea.setText(e1.getMessage());
			btnNewButton.setEnabled(true);
		}
		btnNewButton.setEnabled(true);
		new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (progress < 100) {
						progress += 1;
						progressBar.setValue(progress);
					} else {
						break;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}.run();
		textArea.append("Backup ZipFile Created!\n");
		textArea.append("Backup ZipFile Path : " + newpath + "\n");
		textArea.append("Backup Completed!\n");
		File file = new File(sourceDirPath);
		File[] f1 = file.listFiles();
		for (File f : f1)
			f.delete();
	}

	// For Configuration File Creation
	private static boolean CreateConfigurationFile() {
		boolean flag = false;
		File file = new File(ConfigurationFilePath);
		try {
			if (!file.exists()) {
				flag = file.mkdirs();
			}
		} catch (Exception e) {
		}
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file.getPath() + "\\BackupConfiguration.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String code = "FromBackupFolder=D:\\\\\r\n" + "ToBackupFolder=c:\\\\SKDailyBackup\\\\Backup\\\\\r\n"
				+ "Extensions=JAVA,TXT,CSS,XLSM,HTML,XML,DOC,PPTX,MP3,MP4,AIF";
		try {
			fileOutputStream.write(code.toString().getBytes());
			fileOutputStream.close();
			flag = true;
		} catch (IOException e1) {
			flag = false;
		}
		return flag;
	}

	public static void main(String[] args) {
		new DailyBackup();
	}

	public DailyBackup() {
		setTitle("Daily Backup Software By Akash Aggarwal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);

		contentPane = new JPanel();
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Daily Backup System By Silent Killer");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(105, 11, 220, 14);
		contentPane.add(lblNewLabel);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(false);
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setBackground(Color.white);
		progressBar.setMaximum(100);
		progressBar.setBounds(10, 36, 414, 24);
		contentPane.add(progressBar);

		textArea = new JTextArea();
		textArea.setBounds(10, 71, 414, 195);
		textArea.setFont(new Font("Segoe UI Variable", Font.PLAIN, 13));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 71, 414, 195);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setAutoscrolls(true);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setText("You can modify backup Properties in backup File \nBackup File Path --> "
				+ ConfigurationFilePath + "\\BackupConfiguration.txt");
		contentPane.add(scrollPane);

		btnNewButton = new JButton("Close");
		btnNewButton.setBorder(new LineBorder(new Color(0, 153, 255), 1, true));
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(335, 277, 89, 23);
		contentPane.add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		btnNewButton_1 = new JButton("Start Backup");
		btnNewButton_1.setBorder(new LineBorder(new Color(0, 153, 255), 1, true));
		btnNewButton_1.setBackground(new Color(255, 255, 255));
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		btnNewButton_1.setBounds(226, 277, 99, 23);
		contentPane.add(btnNewButton_1);

		setContentPane(contentPane);
		contentPane.setVisible(true);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);

		sw = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (true) {
					if (filelengthforprogressbar > 0) {
						progressBar.setValue((int) (progressBar.getValue() + (filelengthforprogressbar / 100)));
						if (progressBar.getValue() == 90) {
							break;
						}
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};
		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				progressBar.setStringPainted(true);
				progressBar.setValue(0);
				btnNewButton_1.setEnabled(false);
				btnNewButton.setEnabled(false);
				sw.execute();

				SwingWorker<Void, Void> sw1 = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmm");
						String todate = dateFormat.format(new Date());

						LoadConfigurationFile();
						MainBackupPath = prop.getProperty("ToBackupFolder");
						GetBackupPath = prop.getProperty("FromBackupFolder");
						String exe = prop.getProperty("Extensions");
						if (MainBackupPath == null || GetBackupPath == null || exe == null) {
							CreateConfigurationFile();
							LoadConfigurationFile();
							MainBackupPath = prop.getProperty("ToBackupFolder");
							GetBackupPath = prop.getProperty("FromBackupFolder");
							exe = prop.getProperty("Extensions");
						}
						for (String s : exe.split(",")) {
							try {
								ExtensionList.add(s.toUpperCase());
							} catch (NullPointerException e1) {
							}
						}
						if (!makeBackupFolder()) {
							makeBackupFolder();
						}
						try {
							getAllDir(GetBackupPath);
							PackInZip(MainBackupPath, new File(MainBackupPath).getParent() + "\\" + todate + ".zip");
						} catch (Exception e1) {
							CreateConfigurationFile();
						}
						return null;
					}
				};
				sw1.execute();
			}
		});

	}
}
