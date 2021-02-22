import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import dao.AuthorsDAO;
import dao.LinksDAO;
import lombok.extern.slf4j.Slf4j;
import model.Author;
import model.Link;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;

@Slf4j
public class Application {
    private static final String INPUTDIRECTORY= "src/main/resources/sources/";
    private static final String OUTPUTFILEPATH = "src/main/resources/output.csv";
    private AuthorsDAO authorsDAO;
    private LinksDAO linksDAO;
    public static void main(String[] args) {
        long lStartTime = System.currentTimeMillis();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
        Application app = new Application();
        app.run();
        long lEndTime = System.currentTimeMillis();
        long diff = lEndTime - lStartTime;
        log.info("Temps d'exécution : {}s", diff/1000);
    }

    private void run() {
        authorsDAO = new AuthorsDAO();
        linksDAO = new LinksDAO();
        ArrayList<Author> authorArrayList = new ArrayList<>();
        try {
            for(File inputfile : new File(INPUTDIRECTORY).listFiles()){
                BufferedReader reader = new BufferedReader(new FileReader(inputfile));
                FileWriter writer = new FileWriter(OUTPUTFILEPATH,true);
                log.info("Lecture : {}", inputfile.getPath());
                log.info("Ecriture : {}", OUTPUTFILEPATH);


                reader.readLine();// On élimine la première ligne (en-tête)
                ArrayList<Link> linksList = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {//1 tour correspond à un article
                    String[] values = line.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    String[] authorArray = values[5].split(";"); //array contenant la liste des auteurs pour l'article
                    String publicationYear = values[44];
                    List<String> authorsList = Arrays.asList(authorArray);
                    for (int i = 0; i < authorsList.size(); i++) {
                        Author currentAuthor = getAuthorAndInsertIfUnknown(authorsList.get(i));
                        authorArrayList.add(currentAuthor);
                        for (int j = i+1; j < authorsList.size(); j++) {
                            Author relatedAuthor = getAuthorAndInsertIfUnknown(authorsList.get(j));
                            Link link = getLinkAndInsertIfUnknown(currentAuthor.getName(),relatedAuthor.getName(),publicationYear);
                            writer.append(link.join());
                            writer.append("\n");
                            linksList.add(link);
                        }
                    }
                }
                writer.flush();
                writer.close();
                log.info("Noeuds : {} | Liens : {}",authorArrayList.size(), linksList.size());
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Author getAuthorAndInsertIfUnknown(String n) {
        String[] authorName = n.replace("\"","").split(",");
        Author author = null;
        if (authorName.length >= 2)
            author = new Author(null, authorName[1].trim(),authorName[0].trim());
        else
            author = new Author(null, authorName[0].trim(),"");
        Author currentAuthor = authorsDAO.findByFirstnameAndLastname(author.getFirstname(), author.getLastname());
        if (currentAuthor == null)
            currentAuthor = authorsDAO.insert(author);
        return currentAuthor;
    }
    private Link getLinkAndInsertIfUnknown(String node1, String node2, String publicationYear) {
        Link link = new Link(node1, node2,publicationYear);
        Link currentLink = linksDAO.find(link);
        if (currentLink == null)
            currentLink = linksDAO.insert(link);
        return currentLink;
    }
}
