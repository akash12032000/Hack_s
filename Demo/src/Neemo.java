import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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

public class Neemo {
	private static String MainBackupPath;
	private static String GetBackupPath;
	private static Set<String> ExtensionList;
	static Properties prop = new Properties();
	
	static {
		try {
			prop.load(new FileReader(System.getProperty("user.dir")+"\\src\\BackupConfiguration.txt"));
		} catch (FileNotFoundException e) {
		System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	// Create UnDeletable File
	@SuppressWarnings("unused")
	private static void UnDeletableFile(String Path) {

		 Path file = Paths.get(Path);
		    AclFileAttributeView aclAttr = Files.getFileAttributeView(file, AclFileAttributeView.class);
		    //System.out.println();

		    UserPrincipalLookupService upls = file.getFileSystem().getUserPrincipalLookupService();
		    UserPrincipal user = null;
			try {
				user = upls.lookupPrincipalByName(System.getProperty("user.name"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    AclEntry.Builder builder = AclEntry.newBuilder();       
		    builder.setPermissions(EnumSet.of(AclEntryPermission.APPEND_DATA, AclEntryPermission.DELETE, AclEntryPermission.DELETE_CHILD, AclEntryPermission.EXECUTE, AclEntryPermission.READ_ACL, AclEntryPermission.READ_ATTRIBUTES, AclEntryPermission.READ_DATA, AclEntryPermission.READ_NAMED_ATTRS, AclEntryPermission.SYNCHRONIZE, AclEntryPermission.WRITE_ACL, AclEntryPermission.WRITE_ATTRIBUTES, AclEntryPermission.WRITE_DATA, AclEntryPermission.WRITE_NAMED_ATTRS, AclEntryPermission.WRITE_OWNER));
		    builder.setPrincipal(user);
		    builder.setType(AclEntryType.DENY);
		    try {
				aclAttr.setAcl(Collections.singletonList(builder.build()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	
	// Delete UnDeletable File
		@SuppressWarnings("unused")
	private static void DeleteUnDeletableFile(String Path) {

			System.out.println("Enter Password : ");
			Scanner scanner = new Scanner(System.in);
			if(scanner.next().equals("LOVE")) {
			 Path file = Paths.get(Path);
			    AclFileAttributeView aclAttr = Files.getFileAttributeView(file, AclFileAttributeView.class);
			    //System.out.println();

			    UserPrincipalLookupService upls = file.getFileSystem().getUserPrincipalLookupService();
			    UserPrincipal user = null;
				try {
					user = upls.lookupPrincipalByName(System.getProperty("user.name"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			    AclEntry.Builder builder = AclEntry.newBuilder();       
			    builder.setPermissions(EnumSet.of(AclEntryPermission.APPEND_DATA, AclEntryPermission.DELETE, AclEntryPermission.DELETE_CHILD, AclEntryPermission.EXECUTE, AclEntryPermission.READ_ACL, AclEntryPermission.READ_ATTRIBUTES, AclEntryPermission.READ_DATA, AclEntryPermission.READ_NAMED_ATTRS, AclEntryPermission.SYNCHRONIZE, AclEntryPermission.WRITE_ACL, AclEntryPermission.WRITE_ATTRIBUTES, AclEntryPermission.WRITE_DATA, AclEntryPermission.WRITE_NAMED_ATTRS, AclEntryPermission.WRITE_OWNER));
			    builder.setPrincipal(user);
			    builder.setType(AclEntryType.ALLOW);
			    try {
					aclAttr.setAcl(Collections.singletonList(builder.build()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Wrong Password Try Again!");
			}
			scanner.close();
			    
		}
	
	
	// Backup Folder
 	private static boolean makeBackupFolder() {

		File backupFolder = new File(MainBackupPath);
		MainBackupPath = backupFolder.getPath();
		try {
			if (!backupFolder.exists()) {
				backupFolder.mkdirs();
			}
		} catch (Exception e) {
		}
		return backupFolder.exists();
	}

	// Get All Extension
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
		Set<String> extension = getExtension();
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
							if (extension.contains(filename.substring(filename.lastIndexOf(".") + 1).toUpperCase())) {
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
		if (file.isFile()) {
			if (todate.equals(dateFormat.format(file.lastModified()))) {
				System.out.println(path);
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

	}

	// Create All Backup Files Zip_File
	private static void PackInZip(String sourceDirPath, String newpath) { 
	Path p = null;
	try {
		p = Files.createFile(Paths.get(newpath));
	} catch (IOException e1) {
		e1.printStackTrace();
	}
    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
        Path pp = Paths.get(sourceDirPath);
        try {
			Files.walk(pp)
			  .filter(path -> !Files.isDirectory(path))
			  .forEach(path -> {
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
	}}

	
	// For Configuration File Creation
	@SuppressWarnings("unused")
	private static void CreateConfigurationFile() throws IOException {
		File file  = new File(System.getenv("ProgramFiles")+"\\BackupConfigurationFolder\\BackupConfiguration.txt");
		if(!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		String code = "FromBackupFolder=D:\\\r\n" + 
				"ToBackupFolder=c:\\DailyBackup\\DailyCodeBackup\\Backup\r\n" + 
				"Extensions=JAVA,TXT,CLASS,CSS,XLSM,HTML,XML,DOC,PPTX,MP3,MP4,AIF,MPA";
		fileOutputStream.write(code.toString().getBytes());
		fileOutputStream.close();
		
		
	}
	
	public static void main(String[] args) {
		
		try {
		MainBackupPath = prop.getProperty("ToBackupFolder");
		GetBackupPath = prop.getProperty("FromBackupFolder");
		
		}catch (NullPointerException e) {
			// TODO: handle exception
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmm");
		String todate = dateFormat.format(new Date());
		if (!makeBackupFolder()) {
			makeBackupFolder();
		}
		getAllDir(GetBackupPath);
		System.out.println(new File(MainBackupPath).getParent());
		PackInZip(MainBackupPath+"\\",new File(MainBackupPath).getParent()+"\\"+todate+".zip");

	}

}
