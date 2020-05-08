import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class UnzipDARs {
    
    public static void extractFile(Path zipFile, String fileName, Path outputFile) throws IOException {
        // Wrap the file system in a try-with-resources statement
        // to auto-close it when finished and prevent a memory leak
        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null)) {
            Path fileToExtract = fileSystem.getPath(fileName);
//            if(fileToExtract. endsWith(".orderedFeatures"))
            Files.copy(fileToExtract, outputFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        
        catch (Exception ex) {
            // some errors occurred
            ex.printStackTrace();
        }   
    }
     
public static void displayIt(File node, String dest) throws IOException{
        
        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
//                if(filename.equals(".orderedFeatures"))
//                    continue;
                displayIt(new File(node, filename), dest);
            }
        }
        else if(node.getName().endsWith(".dar")) {
//            System.out.println(node.getAbsoluteFile());
            int ind = node.getName().lastIndexOf('.');
            if(ind != -1) {
                dest += File.separator + node.getName().substring(0, ind) + ".xml";
            }
            
            //try new method
//            extract(".orderedFeatures", node.getAbsoluteFile().toString(),dest);
            
            //old method to extract
//            if(node.getAbsoluteFile().toString())
            extractFile(Paths.get(node.getAbsoluteFile().toString()) , ".orderedFeatures" , Paths.get(dest));
        }
        
    }
}