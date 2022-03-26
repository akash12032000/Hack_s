import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Neemo {
	private static String MainBackupPath;
	private static String GetBackupPath;
	private static String ConfigurationFilePath;
	private static Set<String> ExtensionList = new HashSet<String>();
	static Properties prop = new Properties();

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
//			System.out.println(path);
			String FinalFileName = MainBackupPath + "\\" + file.getName();
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
		}

	}

	// Create All Backup Files Zip_File
	private static void PackInZip(String sourceDirPath, String newpath) {
		Path p = null;
		try {
			p = Files.createFile(Paths.get(newpath));
		} catch (IOException e1) {
			CreateConfigurationFile();
			e1.printStackTrace();
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
			}
		} catch (IOException e1) {
		}
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

	// Main Method
	public static void main(String[] args) {
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
			} catch (NullPointerException e) {
			}
		}
		if (!makeBackupFolder()) {
			makeBackupFolder();
		}
		try {
			getAllDir(GetBackupPath);
			PackInZip(MainBackupPath, new File(MainBackupPath).getParent() + "\\" + todate + ".zip");
		} catch (Exception e) {
			CreateConfigurationFile();
		}
		JFrame frame = new JFrame("Swing Tester");
		JOptionPane.showMessageDialog(frame, "Today Backup Completed!");
		System.exit(0);
	}

}
